package io.hexlet.hexletcorrection.web.rest;

import io.hexlet.hexletcorrection.HexletCorrectionApp;
import io.hexlet.hexletcorrection.domain.Correcter;
import io.hexlet.hexletcorrection.domain.User;
import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.domain.Comment;
import io.hexlet.hexletcorrection.repository.CorrecterRepository;
import io.hexlet.hexletcorrection.service.CorrecterService;
import io.hexlet.hexletcorrection.service.dto.CorrecterDTO;
import io.hexlet.hexletcorrection.service.mapper.CorrecterMapper;
import io.hexlet.hexletcorrection.web.rest.errors.ExceptionTranslator;
import io.hexlet.hexletcorrection.service.dto.CorrecterCriteria;
import io.hexlet.hexletcorrection.service.CorrecterQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static io.hexlet.hexletcorrection.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.hexlet.hexletcorrection.domain.enumeration.CorrecterStatus;
/**
 * Integration tests for the {@link CorrecterResource} REST controller.
 */
@SpringBootTest(classes = HexletCorrectionApp.class)
public class CorrecterResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final CorrecterStatus DEFAULT_STATUS = CorrecterStatus.NON_ACTIVATED;
    private static final CorrecterStatus UPDATED_STATUS = CorrecterStatus.BLOCKED;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_AVATAR = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_AVATAR = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_AVATAR_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_AVATAR_CONTENT_TYPE = "image/png";

    @Autowired
    private CorrecterRepository correcterRepository;

    @Autowired
    private CorrecterMapper correcterMapper;

    @Autowired
    private CorrecterService correcterService;

    @Autowired
    private CorrecterQueryService correcterQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCorrecterMockMvc;

    private Correcter correcter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CorrecterResource correcterResource = new CorrecterResource(correcterService, correcterQueryService);
        this.restCorrecterMockMvc = MockMvcBuilders.standaloneSetup(correcterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Correcter createEntity(EntityManager em) {
        Correcter correcter = new Correcter()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .status(DEFAULT_STATUS)
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD)
            .phone(DEFAULT_PHONE)
            .avatar(DEFAULT_AVATAR)
            .avatarContentType(DEFAULT_AVATAR_CONTENT_TYPE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        correcter.setUser(user);
        return correcter;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Correcter createUpdatedEntity(EntityManager em) {
        Correcter correcter = new Correcter()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .status(UPDATED_STATUS)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .phone(UPDATED_PHONE)
            .avatar(UPDATED_AVATAR)
            .avatarContentType(UPDATED_AVATAR_CONTENT_TYPE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        correcter.setUser(user);
        return correcter;
    }

    @BeforeEach
    public void initTest() {
        correcter = createEntity(em);
    }

    @Test
    @Transactional
    public void createCorrecter() throws Exception {
        int databaseSizeBeforeCreate = correcterRepository.findAll().size();

        // Create the Correcter
        CorrecterDTO correcterDTO = correcterMapper.toDto(correcter);
        restCorrecterMockMvc.perform(post("/api/correcters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correcterDTO)))
            .andExpect(status().isCreated());

        // Validate the Correcter in the database
        List<Correcter> correcterList = correcterRepository.findAll();
        assertThat(correcterList).hasSize(databaseSizeBeforeCreate + 1);
        Correcter testCorrecter = correcterList.get(correcterList.size() - 1);
        assertThat(testCorrecter.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCorrecter.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCorrecter.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCorrecter.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCorrecter.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testCorrecter.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCorrecter.getAvatar()).isEqualTo(DEFAULT_AVATAR);
        assertThat(testCorrecter.getAvatarContentType()).isEqualTo(DEFAULT_AVATAR_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createCorrecterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = correcterRepository.findAll().size();

        // Create the Correcter with an existing ID
        correcter.setId(1L);
        CorrecterDTO correcterDTO = correcterMapper.toDto(correcter);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorrecterMockMvc.perform(post("/api/correcters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correcterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Correcter in the database
        List<Correcter> correcterList = correcterRepository.findAll();
        assertThat(correcterList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = correcterRepository.findAll().size();
        // set the field null
        correcter.setFirstName(null);

        // Create the Correcter, which fails.
        CorrecterDTO correcterDTO = correcterMapper.toDto(correcter);

        restCorrecterMockMvc.perform(post("/api/correcters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correcterDTO)))
            .andExpect(status().isBadRequest());

        List<Correcter> correcterList = correcterRepository.findAll();
        assertThat(correcterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = correcterRepository.findAll().size();
        // set the field null
        correcter.setLastName(null);

        // Create the Correcter, which fails.
        CorrecterDTO correcterDTO = correcterMapper.toDto(correcter);

        restCorrecterMockMvc.perform(post("/api/correcters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correcterDTO)))
            .andExpect(status().isBadRequest());

        List<Correcter> correcterList = correcterRepository.findAll();
        assertThat(correcterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = correcterRepository.findAll().size();
        // set the field null
        correcter.setStatus(null);

        // Create the Correcter, which fails.
        CorrecterDTO correcterDTO = correcterMapper.toDto(correcter);

        restCorrecterMockMvc.perform(post("/api/correcters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correcterDTO)))
            .andExpect(status().isBadRequest());

        List<Correcter> correcterList = correcterRepository.findAll();
        assertThat(correcterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = correcterRepository.findAll().size();
        // set the field null
        correcter.setEmail(null);

        // Create the Correcter, which fails.
        CorrecterDTO correcterDTO = correcterMapper.toDto(correcter);

        restCorrecterMockMvc.perform(post("/api/correcters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correcterDTO)))
            .andExpect(status().isBadRequest());

        List<Correcter> correcterList = correcterRepository.findAll();
        assertThat(correcterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = correcterRepository.findAll().size();
        // set the field null
        correcter.setPassword(null);

        // Create the Correcter, which fails.
        CorrecterDTO correcterDTO = correcterMapper.toDto(correcter);

        restCorrecterMockMvc.perform(post("/api/correcters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correcterDTO)))
            .andExpect(status().isBadRequest());

        List<Correcter> correcterList = correcterRepository.findAll();
        assertThat(correcterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCorrecters() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList
        restCorrecterMockMvc.perform(get("/api/correcters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correcter.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].avatarContentType").value(hasItem(DEFAULT_AVATAR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(Base64Utils.encodeToString(DEFAULT_AVATAR))));
    }
    
    @Test
    @Transactional
    public void getCorrecter() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get the correcter
        restCorrecterMockMvc.perform(get("/api/correcters/{id}", correcter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(correcter.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.avatarContentType").value(DEFAULT_AVATAR_CONTENT_TYPE))
            .andExpect(jsonPath("$.avatar").value(Base64Utils.encodeToString(DEFAULT_AVATAR)));
    }


    @Test
    @Transactional
    public void getCorrectersByIdFiltering() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        Long id = correcter.getId();

        defaultCorrecterShouldBeFound("id.equals=" + id);
        defaultCorrecterShouldNotBeFound("id.notEquals=" + id);

        defaultCorrecterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCorrecterShouldNotBeFound("id.greaterThan=" + id);

        defaultCorrecterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCorrecterShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCorrectersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where firstName equals to DEFAULT_FIRST_NAME
        defaultCorrecterShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the correcterList where firstName equals to UPDATED_FIRST_NAME
        defaultCorrecterShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllCorrectersByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where firstName not equals to DEFAULT_FIRST_NAME
        defaultCorrecterShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the correcterList where firstName not equals to UPDATED_FIRST_NAME
        defaultCorrecterShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllCorrectersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultCorrecterShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the correcterList where firstName equals to UPDATED_FIRST_NAME
        defaultCorrecterShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllCorrectersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where firstName is not null
        defaultCorrecterShouldBeFound("firstName.specified=true");

        // Get all the correcterList where firstName is null
        defaultCorrecterShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllCorrectersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where firstName contains DEFAULT_FIRST_NAME
        defaultCorrecterShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the correcterList where firstName contains UPDATED_FIRST_NAME
        defaultCorrecterShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllCorrectersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where firstName does not contain DEFAULT_FIRST_NAME
        defaultCorrecterShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the correcterList where firstName does not contain UPDATED_FIRST_NAME
        defaultCorrecterShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllCorrectersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where lastName equals to DEFAULT_LAST_NAME
        defaultCorrecterShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the correcterList where lastName equals to UPDATED_LAST_NAME
        defaultCorrecterShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllCorrectersByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where lastName not equals to DEFAULT_LAST_NAME
        defaultCorrecterShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the correcterList where lastName not equals to UPDATED_LAST_NAME
        defaultCorrecterShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllCorrectersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultCorrecterShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the correcterList where lastName equals to UPDATED_LAST_NAME
        defaultCorrecterShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllCorrectersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where lastName is not null
        defaultCorrecterShouldBeFound("lastName.specified=true");

        // Get all the correcterList where lastName is null
        defaultCorrecterShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllCorrectersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where lastName contains DEFAULT_LAST_NAME
        defaultCorrecterShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the correcterList where lastName contains UPDATED_LAST_NAME
        defaultCorrecterShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllCorrectersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where lastName does not contain DEFAULT_LAST_NAME
        defaultCorrecterShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the correcterList where lastName does not contain UPDATED_LAST_NAME
        defaultCorrecterShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllCorrectersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where status equals to DEFAULT_STATUS
        defaultCorrecterShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the correcterList where status equals to UPDATED_STATUS
        defaultCorrecterShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllCorrectersByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where status not equals to DEFAULT_STATUS
        defaultCorrecterShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the correcterList where status not equals to UPDATED_STATUS
        defaultCorrecterShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllCorrectersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultCorrecterShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the correcterList where status equals to UPDATED_STATUS
        defaultCorrecterShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllCorrectersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where status is not null
        defaultCorrecterShouldBeFound("status.specified=true");

        // Get all the correcterList where status is null
        defaultCorrecterShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllCorrectersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where email equals to DEFAULT_EMAIL
        defaultCorrecterShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the correcterList where email equals to UPDATED_EMAIL
        defaultCorrecterShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCorrectersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where email not equals to DEFAULT_EMAIL
        defaultCorrecterShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the correcterList where email not equals to UPDATED_EMAIL
        defaultCorrecterShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCorrectersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCorrecterShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the correcterList where email equals to UPDATED_EMAIL
        defaultCorrecterShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCorrectersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where email is not null
        defaultCorrecterShouldBeFound("email.specified=true");

        // Get all the correcterList where email is null
        defaultCorrecterShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllCorrectersByEmailContainsSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where email contains DEFAULT_EMAIL
        defaultCorrecterShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the correcterList where email contains UPDATED_EMAIL
        defaultCorrecterShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllCorrectersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where email does not contain DEFAULT_EMAIL
        defaultCorrecterShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the correcterList where email does not contain UPDATED_EMAIL
        defaultCorrecterShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllCorrectersByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where password equals to DEFAULT_PASSWORD
        defaultCorrecterShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the correcterList where password equals to UPDATED_PASSWORD
        defaultCorrecterShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllCorrectersByPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where password not equals to DEFAULT_PASSWORD
        defaultCorrecterShouldNotBeFound("password.notEquals=" + DEFAULT_PASSWORD);

        // Get all the correcterList where password not equals to UPDATED_PASSWORD
        defaultCorrecterShouldBeFound("password.notEquals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllCorrectersByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultCorrecterShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the correcterList where password equals to UPDATED_PASSWORD
        defaultCorrecterShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllCorrectersByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where password is not null
        defaultCorrecterShouldBeFound("password.specified=true");

        // Get all the correcterList where password is null
        defaultCorrecterShouldNotBeFound("password.specified=false");
    }
                @Test
    @Transactional
    public void getAllCorrectersByPasswordContainsSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where password contains DEFAULT_PASSWORD
        defaultCorrecterShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the correcterList where password contains UPDATED_PASSWORD
        defaultCorrecterShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllCorrectersByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where password does not contain DEFAULT_PASSWORD
        defaultCorrecterShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the correcterList where password does not contain UPDATED_PASSWORD
        defaultCorrecterShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }


    @Test
    @Transactional
    public void getAllCorrectersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where phone equals to DEFAULT_PHONE
        defaultCorrecterShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the correcterList where phone equals to UPDATED_PHONE
        defaultCorrecterShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCorrectersByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where phone not equals to DEFAULT_PHONE
        defaultCorrecterShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the correcterList where phone not equals to UPDATED_PHONE
        defaultCorrecterShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCorrectersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultCorrecterShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the correcterList where phone equals to UPDATED_PHONE
        defaultCorrecterShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCorrectersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where phone is not null
        defaultCorrecterShouldBeFound("phone.specified=true");

        // Get all the correcterList where phone is null
        defaultCorrecterShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllCorrectersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where phone contains DEFAULT_PHONE
        defaultCorrecterShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the correcterList where phone contains UPDATED_PHONE
        defaultCorrecterShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCorrectersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        // Get all the correcterList where phone does not contain DEFAULT_PHONE
        defaultCorrecterShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the correcterList where phone does not contain UPDATED_PHONE
        defaultCorrecterShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllCorrectersByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = correcter.getUser();
        correcterRepository.saveAndFlush(correcter);
        Long userId = user.getId();

        // Get all the correcterList where user equals to userId
        defaultCorrecterShouldBeFound("userId.equals=" + userId);

        // Get all the correcterList where user equals to userId + 1
        defaultCorrecterShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllCorrectersByCorrectionsInProgressIsEqualToSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);
        Correction correctionsInProgress = CorrectionResourceIT.createEntity(em);
        em.persist(correctionsInProgress);
        em.flush();
        correcter.addCorrectionsInProgress(correctionsInProgress);
        correcterRepository.saveAndFlush(correcter);
        Long correctionsInProgressId = correctionsInProgress.getId();

        // Get all the correcterList where correctionsInProgress equals to correctionsInProgressId
        defaultCorrecterShouldBeFound("correctionsInProgressId.equals=" + correctionsInProgressId);

        // Get all the correcterList where correctionsInProgress equals to correctionsInProgressId + 1
        defaultCorrecterShouldNotBeFound("correctionsInProgressId.equals=" + (correctionsInProgressId + 1));
    }


    @Test
    @Transactional
    public void getAllCorrectersByCorrectionsResolvedIsEqualToSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);
        Correction correctionsResolved = CorrectionResourceIT.createEntity(em);
        em.persist(correctionsResolved);
        em.flush();
        correcter.addCorrectionsResolved(correctionsResolved);
        correcterRepository.saveAndFlush(correcter);
        Long correctionsResolvedId = correctionsResolved.getId();

        // Get all the correcterList where correctionsResolved equals to correctionsResolvedId
        defaultCorrecterShouldBeFound("correctionsResolvedId.equals=" + correctionsResolvedId);

        // Get all the correcterList where correctionsResolved equals to correctionsResolvedId + 1
        defaultCorrecterShouldNotBeFound("correctionsResolvedId.equals=" + (correctionsResolvedId + 1));
    }


    @Test
    @Transactional
    public void getAllCorrectersByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);
        Comment comments = CommentResourceIT.createEntity(em);
        em.persist(comments);
        em.flush();
        correcter.addComments(comments);
        correcterRepository.saveAndFlush(correcter);
        Long commentsId = comments.getId();

        // Get all the correcterList where comments equals to commentsId
        defaultCorrecterShouldBeFound("commentsId.equals=" + commentsId);

        // Get all the correcterList where comments equals to commentsId + 1
        defaultCorrecterShouldNotBeFound("commentsId.equals=" + (commentsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCorrecterShouldBeFound(String filter) throws Exception {
        restCorrecterMockMvc.perform(get("/api/correcters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correcter.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].avatarContentType").value(hasItem(DEFAULT_AVATAR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(Base64Utils.encodeToString(DEFAULT_AVATAR))));

        // Check, that the count call also returns 1
        restCorrecterMockMvc.perform(get("/api/correcters/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCorrecterShouldNotBeFound(String filter) throws Exception {
        restCorrecterMockMvc.perform(get("/api/correcters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCorrecterMockMvc.perform(get("/api/correcters/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCorrecter() throws Exception {
        // Get the correcter
        restCorrecterMockMvc.perform(get("/api/correcters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCorrecter() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        int databaseSizeBeforeUpdate = correcterRepository.findAll().size();

        // Update the correcter
        Correcter updatedCorrecter = correcterRepository.findById(correcter.getId()).get();
        // Disconnect from session so that the updates on updatedCorrecter are not directly saved in db
        em.detach(updatedCorrecter);
        updatedCorrecter
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .status(UPDATED_STATUS)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .phone(UPDATED_PHONE)
            .avatar(UPDATED_AVATAR)
            .avatarContentType(UPDATED_AVATAR_CONTENT_TYPE);
        CorrecterDTO correcterDTO = correcterMapper.toDto(updatedCorrecter);

        restCorrecterMockMvc.perform(put("/api/correcters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correcterDTO)))
            .andExpect(status().isOk());

        // Validate the Correcter in the database
        List<Correcter> correcterList = correcterRepository.findAll();
        assertThat(correcterList).hasSize(databaseSizeBeforeUpdate);
        Correcter testCorrecter = correcterList.get(correcterList.size() - 1);
        assertThat(testCorrecter.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCorrecter.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCorrecter.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCorrecter.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCorrecter.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testCorrecter.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCorrecter.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testCorrecter.getAvatarContentType()).isEqualTo(UPDATED_AVATAR_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingCorrecter() throws Exception {
        int databaseSizeBeforeUpdate = correcterRepository.findAll().size();

        // Create the Correcter
        CorrecterDTO correcterDTO = correcterMapper.toDto(correcter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorrecterMockMvc.perform(put("/api/correcters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correcterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Correcter in the database
        List<Correcter> correcterList = correcterRepository.findAll();
        assertThat(correcterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCorrecter() throws Exception {
        // Initialize the database
        correcterRepository.saveAndFlush(correcter);

        int databaseSizeBeforeDelete = correcterRepository.findAll().size();

        // Delete the correcter
        restCorrecterMockMvc.perform(delete("/api/correcters/{id}", correcter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Correcter> correcterList = correcterRepository.findAll();
        assertThat(correcterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
