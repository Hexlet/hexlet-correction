package io.hexlet.hexletcorrection.web.rest;

import io.hexlet.hexletcorrection.HexletCorrectionApp;
import io.hexlet.hexletcorrection.domain.Comment;
import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.domain.Preference;
import io.hexlet.hexletcorrection.domain.User;
import io.hexlet.hexletcorrection.repository.PreferenceRepository;
import io.hexlet.hexletcorrection.service.dto.PreferenceDTO;
import io.hexlet.hexletcorrection.service.mapper.PreferenceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link PreferenceResource} REST controller.
 */
@SpringBootTest(classes = HexletCorrectionApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PreferenceResourceIT {

    private static final byte[] DEFAULT_AVATAR = TestUtil.createByteArray(1, "0");

    private static final byte[] UPDATED_AVATAR = TestUtil.createByteArray(1, "1");

    private static final String DEFAULT_AVATAR_CONTENT_TYPE = "image/jpg";

    private static final String UPDATED_AVATAR_CONTENT_TYPE = "image/png";

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private PreferenceMapper preferenceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPreferenceMockMvc;

    private Preference preference;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Preference createEntity(EntityManager em) {
        Preference preference = new Preference();
        preference.setAvatar(DEFAULT_AVATAR);
        preference.setAvatarContentType(DEFAULT_AVATAR_CONTENT_TYPE);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        preference.setUser(user);
        return preference;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Preference createUpdatedEntity(EntityManager em) {
        Preference preference = new Preference();
        preference.setAvatar(UPDATED_AVATAR);
        preference.setAvatarContentType(UPDATED_AVATAR_CONTENT_TYPE);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        preference.setUser(user);
        return preference;
    }

    @BeforeEach
    public void initTest() {
        preferenceRepository.deleteAll();
        preference = createEntity(em);
    }

    @Test
    @Transactional
    public void createPreference() throws Exception {
        final int databaseSizeBeforeCreate = preferenceRepository.findAll().size();

        // Create the Preference
        PreferenceDTO preferenceDTO = preferenceMapper.toDto(preference);
        restPreferenceMockMvc.perform(post("/api/preferences")
            .contentType(APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(preferenceDTO)))
            .andExpect(status().isCreated());

        // Validate the Preference in the database
        List<Preference> preferenceList = preferenceRepository.findAll();
        assertThat(preferenceList).hasSize(databaseSizeBeforeCreate + 1);
        Preference testPreference = preferenceList.get(preferenceList.size() - 1);
        assertThat(testPreference.getAvatar()).isEqualTo(DEFAULT_AVATAR);
        assertThat(testPreference.getAvatarContentType()).isEqualTo(DEFAULT_AVATAR_CONTENT_TYPE);

        // Validate the id for MapsId, the ids must be same
        assertThat(testPreference.getId()).isEqualTo(testPreference.getUser().getId());
    }

    @Test
    @Transactional
    public void createPreferenceWithExistingId() throws Exception {
        final int databaseSizeBeforeCreate = preferenceRepository.findAll().size();

        // Create the Preference with an existing ID
        preference.setId(1L);
        PreferenceDTO preferenceDTO = preferenceMapper.toDto(preference);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPreferenceMockMvc.perform(post("/api/preferences")
            .contentType(APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(preferenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Preference in the database
        List<Preference> preferenceList = preferenceRepository.findAll();
        assertThat(preferenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void updatePreferenceMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);
        final int databaseSizeBeforeCreate = preferenceRepository.findAll().size();

        // Add a new parent entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();

        // Load the preference
        Preference updatedPreference = preferenceRepository.findById(preference.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPreference are not directly saved in db
        em.detach(updatedPreference);

        // Update the User with new association value
        updatedPreference.setUser(user);
        PreferenceDTO updatedPreferenceDTO = preferenceMapper.toDto(updatedPreference);

        // Update the entity
        restPreferenceMockMvc.perform(put("/api/preferences")
            .contentType(APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPreferenceDTO)))
            .andExpect(status().isOk());

        // Validate the Preference in the database
        List<Preference> preferenceList = preferenceRepository.findAll();
        assertThat(preferenceList).hasSize(databaseSizeBeforeCreate);
        Preference testPreference = preferenceList.get(preferenceList.size() - 1);

        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testPreference.getId()).isEqualTo(testPreference.getUser().getId());
    }

    @Test
    @Transactional
    public void getAllPreferences() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList
        restPreferenceMockMvc.perform(get("/api/preferences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preference.getId().intValue())))
            .andExpect(jsonPath("$.[*].avatarContentType").value(hasItem(DEFAULT_AVATAR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(Base64Utils.encodeToString(DEFAULT_AVATAR))));
    }

    @Test
    @Transactional
    public void getPreference() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get the preference
        restPreferenceMockMvc.perform(get("/api/preferences/{id}", preference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(preference.getId().intValue()))
            .andExpect(jsonPath("$.avatarContentType").value(DEFAULT_AVATAR_CONTENT_TYPE))
            .andExpect(jsonPath("$.avatar").value(Base64Utils.encodeToString(DEFAULT_AVATAR)));
    }


    @Test
    @Transactional
    public void getPreferencesByIdFiltering() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        Long id = preference.getId();

        defaultPreferenceShouldBeFound("id.equals=" + id);
        defaultPreferenceShouldNotBeFound("id.notEquals=" + id);

        defaultPreferenceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPreferenceShouldNotBeFound("id.greaterThan=" + id);

        defaultPreferenceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPreferenceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPreferencesByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = preference.getUser();
        preferenceRepository.saveAndFlush(preference);
        Long userId = user.getId();

        // Get all the preferenceList where user equals to userId
        defaultPreferenceShouldBeFound("userId.equals=" + userId);

        // Get all the preferenceList where user equals to userId + 1
        defaultPreferenceShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllPreferencesByCorrectionsInProgressIsEqualToSomething() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);
        Correction correctionInProgresses = CorrectionResourceIT.createEntity();
        em.persist(correctionInProgresses);
        em.flush();
        preference.addCorrectionInProgresses(correctionInProgresses);
        preferenceRepository.saveAndFlush(preference);
        Long correctionsInProgressId = correctionInProgresses.getId();

        // Get all the preferenceList where correctionInProgresses equals to correctionsInProgressId
        defaultPreferenceShouldBeFound("correctionsInProgressId.equals=" + correctionsInProgressId);

        // Get all the preferenceList where correctionInProgresses equals to correctionsInProgressId + 1
        defaultPreferenceShouldNotBeFound("correctionsInProgressId.equals=" + (correctionsInProgressId + 1));
    }


    @Test
    @Transactional
    public void getAllPreferencesByResolvedCorrectionsIsEqualToSomething() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);
        Correction resolvedCorrections = CorrectionResourceIT.createEntity();
        em.persist(resolvedCorrections);
        em.flush();
        preference.addResolvedCorrections(resolvedCorrections);
        preferenceRepository.saveAndFlush(preference);
        Long resolvedCorrectionsId = resolvedCorrections.getId();

        // Get all the preferenceList where resolvedCorrections equals to resolvedCorrectionsId
        defaultPreferenceShouldBeFound("resolvedCorrectionsId.equals=" + resolvedCorrectionsId);

        // Get all the preferenceList where resolvedCorrections equals to resolvedCorrectionsId + 1
        defaultPreferenceShouldNotBeFound("resolvedCorrectionsId.equals=" + (resolvedCorrectionsId + 1));
    }


    @Test
    @Transactional
    public void getAllPreferencesByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);
        Comment comment = CommentResourceIT.createEntity();
        em.persist(comment);
        em.flush();
        preference.addComment(comment);
        preferenceRepository.saveAndFlush(preference);
        Long commentsId = comment.getId();

        // Get all the preferenceList where comment equals to commentsId
        defaultPreferenceShouldBeFound("commentsId.equals=" + commentsId);

        // Get all the preferenceList where comment equals to commentsId + 1
        defaultPreferenceShouldNotBeFound("commentsId.equals=" + (commentsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPreferenceShouldBeFound(String filter) throws Exception {
        restPreferenceMockMvc.perform(get("/api/preferences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preference.getId().intValue())))
            .andExpect(jsonPath("$.[*].avatarContentType").value(hasItem(DEFAULT_AVATAR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(Base64Utils.encodeToString(DEFAULT_AVATAR))));

        // Check, that the count call also returns 1
        restPreferenceMockMvc.perform(get("/api/preferences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPreferenceShouldNotBeFound(String filter) throws Exception {
        restPreferenceMockMvc.perform(get("/api/preferences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(not(hasItem(preference.getId().intValue()))))
            .andExpect(jsonPath("$.[*].avatarContentType").value(not(hasItem(DEFAULT_AVATAR_CONTENT_TYPE))))
            .andExpect(jsonPath("$.[*].avatar").value(not(hasItem(Base64Utils.encodeToString(DEFAULT_AVATAR)))));

        // Check, that the count call also returns 0
        restPreferenceMockMvc.perform(get("/api/preferences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPreference() throws Exception {
        // Get the preference
        restPreferenceMockMvc.perform(get("/api/preferences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePreference() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        final int databaseSizeBeforeUpdate = preferenceRepository.findAll().size();

        // Update the preference
        Preference updatedPreference = preferenceRepository.findById(preference.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPreference are not directly saved in db
        em.detach(updatedPreference);
        updatedPreference.setAvatar(UPDATED_AVATAR);
        updatedPreference.setAvatarContentType(UPDATED_AVATAR_CONTENT_TYPE);
        PreferenceDTO preferenceDTO = preferenceMapper.toDto(updatedPreference);

        restPreferenceMockMvc.perform(put("/api/preferences")
            .contentType(APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(preferenceDTO)))
            .andExpect(status().isOk());

        // Validate the Preference in the database
        List<Preference> preferenceList = preferenceRepository.findAll();
        assertThat(preferenceList).hasSize(databaseSizeBeforeUpdate);
        Preference testPreference = preferenceList.get(preferenceList.size() - 1);
        assertThat(testPreference.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testPreference.getAvatarContentType()).isEqualTo(UPDATED_AVATAR_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingPreference() throws Exception {
        final int databaseSizeBeforeUpdate = preferenceRepository.findAll().size();

        // Create the Preference
        PreferenceDTO preferenceDTO = preferenceMapper.toDto(preference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreferenceMockMvc.perform(put("/api/preferences")
            .contentType(APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(preferenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Preference in the database
        List<Preference> preferenceList = preferenceRepository.findAll();
        assertThat(preferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePreference() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        final int databaseSizeBeforeDelete = preferenceRepository.findAll().size();

        // Delete the preference
        restPreferenceMockMvc.perform(delete("/api/preferences/{id}", preference.getId())
            .accept(APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Preference> preferenceList = preferenceRepository.findAll();
        assertThat(preferenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
