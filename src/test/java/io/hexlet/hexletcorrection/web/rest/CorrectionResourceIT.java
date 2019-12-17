package io.hexlet.hexletcorrection.web.rest;

import io.hexlet.hexletcorrection.HexletCorrectionApp;
import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.domain.Comment;
import io.hexlet.hexletcorrection.domain.Correcter;
import io.hexlet.hexletcorrection.repository.CorrectionRepository;
import io.hexlet.hexletcorrection.service.CorrectionService;
import io.hexlet.hexletcorrection.service.dto.CorrectionDTO;
import io.hexlet.hexletcorrection.service.mapper.CorrectionMapper;
import io.hexlet.hexletcorrection.web.rest.errors.ExceptionTranslator;
import io.hexlet.hexletcorrection.service.dto.CorrectionCriteria;
import io.hexlet.hexletcorrection.service.CorrectionQueryService;

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
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static io.hexlet.hexletcorrection.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.hexlet.hexletcorrection.domain.enumeration.CorrectionStatus;
/**
 * Integration tests for the {@link CorrectionResource} REST controller.
 */
@SpringBootTest(classes = HexletCorrectionApp.class)
public class CorrectionResourceIT {

    private static final String DEFAULT_REPORTER_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REPORTER_REMARK = "BBBBBBBBBB";

    private static final String DEFAULT_CORRECTER_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_CORRECTER_REMARK = "BBBBBBBBBB";

    private static final String DEFAULT_RESOLVER_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_RESOLVER_REMARK = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_BEFORE_CORRECTION = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_BEFORE_CORRECTION = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_CORRECTION = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_CORRECTION = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_AFTER_CORRECTION = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_AFTER_CORRECTION = "BBBBBBBBBB";

    private static final String DEFAULT_REPORTER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPORTER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PAGE_URL = "BBBBBBBBBB";

    private static final CorrectionStatus DEFAULT_CORRECTION_STATUS = CorrectionStatus.REPORTED;
    private static final CorrectionStatus UPDATED_CORRECTION_STATUS = CorrectionStatus.IN_PROGRESS;

    @Autowired
    private CorrectionRepository correctionRepository;

    @Autowired
    private CorrectionMapper correctionMapper;

    @Autowired
    private CorrectionService correctionService;

    @Autowired
    private CorrectionQueryService correctionQueryService;

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

    private MockMvc restCorrectionMockMvc;

    private Correction correction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CorrectionResource correctionResource = new CorrectionResource(correctionService, correctionQueryService);
        this.restCorrectionMockMvc = MockMvcBuilders.standaloneSetup(correctionResource)
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
    public static Correction createEntity(EntityManager em) {
        Correction correction = new Correction()
            .reporterRemark(DEFAULT_REPORTER_REMARK)
            .correcterRemark(DEFAULT_CORRECTER_REMARK)
            .resolverRemark(DEFAULT_RESOLVER_REMARK)
            .textBeforeCorrection(DEFAULT_TEXT_BEFORE_CORRECTION)
            .textCorrection(DEFAULT_TEXT_CORRECTION)
            .textAfterCorrection(DEFAULT_TEXT_AFTER_CORRECTION)
            .reporterName(DEFAULT_REPORTER_NAME)
            .pageURL(DEFAULT_PAGE_URL)
            .correctionStatus(DEFAULT_CORRECTION_STATUS);
        return correction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Correction createUpdatedEntity(EntityManager em) {
        Correction correction = new Correction()
            .reporterRemark(UPDATED_REPORTER_REMARK)
            .correcterRemark(UPDATED_CORRECTER_REMARK)
            .resolverRemark(UPDATED_RESOLVER_REMARK)
            .textBeforeCorrection(UPDATED_TEXT_BEFORE_CORRECTION)
            .textCorrection(UPDATED_TEXT_CORRECTION)
            .textAfterCorrection(UPDATED_TEXT_AFTER_CORRECTION)
            .reporterName(UPDATED_REPORTER_NAME)
            .pageURL(UPDATED_PAGE_URL)
            .correctionStatus(UPDATED_CORRECTION_STATUS);
        return correction;
    }

    @BeforeEach
    public void initTest() {
        correction = createEntity(em);
    }

    @Test
    @Transactional
    public void createCorrection() throws Exception {
        int databaseSizeBeforeCreate = correctionRepository.findAll().size();

        // Create the Correction
        CorrectionDTO correctionDTO = correctionMapper.toDto(correction);
        restCorrectionMockMvc.perform(post("/api/corrections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correctionDTO)))
            .andExpect(status().isCreated());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeCreate + 1);
        Correction testCorrection = correctionList.get(correctionList.size() - 1);
        assertThat(testCorrection.getReporterRemark()).isEqualTo(DEFAULT_REPORTER_REMARK);
        assertThat(testCorrection.getCorrecterRemark()).isEqualTo(DEFAULT_CORRECTER_REMARK);
        assertThat(testCorrection.getResolverRemark()).isEqualTo(DEFAULT_RESOLVER_REMARK);
        assertThat(testCorrection.getTextBeforeCorrection()).isEqualTo(DEFAULT_TEXT_BEFORE_CORRECTION);
        assertThat(testCorrection.getTextCorrection()).isEqualTo(DEFAULT_TEXT_CORRECTION);
        assertThat(testCorrection.getTextAfterCorrection()).isEqualTo(DEFAULT_TEXT_AFTER_CORRECTION);
        assertThat(testCorrection.getReporterName()).isEqualTo(DEFAULT_REPORTER_NAME);
        assertThat(testCorrection.getPageURL()).isEqualTo(DEFAULT_PAGE_URL);
        assertThat(testCorrection.getCorrectionStatus()).isEqualTo(DEFAULT_CORRECTION_STATUS);
    }

    @Test
    @Transactional
    public void createCorrectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = correctionRepository.findAll().size();

        // Create the Correction with an existing ID
        correction.setId(1L);
        CorrectionDTO correctionDTO = correctionMapper.toDto(correction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorrectionMockMvc.perform(post("/api/corrections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correctionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTextCorrectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = correctionRepository.findAll().size();
        // set the field null
        correction.setTextCorrection(null);

        // Create the Correction, which fails.
        CorrectionDTO correctionDTO = correctionMapper.toDto(correction);

        restCorrectionMockMvc.perform(post("/api/corrections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correctionDTO)))
            .andExpect(status().isBadRequest());

        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReporterNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = correctionRepository.findAll().size();
        // set the field null
        correction.setReporterName(null);

        // Create the Correction, which fails.
        CorrectionDTO correctionDTO = correctionMapper.toDto(correction);

        restCorrectionMockMvc.perform(post("/api/corrections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correctionDTO)))
            .andExpect(status().isBadRequest());

        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPageURLIsRequired() throws Exception {
        int databaseSizeBeforeTest = correctionRepository.findAll().size();
        // set the field null
        correction.setPageURL(null);

        // Create the Correction, which fails.
        CorrectionDTO correctionDTO = correctionMapper.toDto(correction);

        restCorrectionMockMvc.perform(post("/api/corrections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correctionDTO)))
            .andExpect(status().isBadRequest());

        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCorrectionStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = correctionRepository.findAll().size();
        // set the field null
        correction.setCorrectionStatus(null);

        // Create the Correction, which fails.
        CorrectionDTO correctionDTO = correctionMapper.toDto(correction);

        restCorrectionMockMvc.perform(post("/api/corrections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correctionDTO)))
            .andExpect(status().isBadRequest());

        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCorrections() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList
        restCorrectionMockMvc.perform(get("/api/corrections?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correction.getId().intValue())))
            .andExpect(jsonPath("$.[*].reporterRemark").value(hasItem(DEFAULT_REPORTER_REMARK)))
            .andExpect(jsonPath("$.[*].correcterRemark").value(hasItem(DEFAULT_CORRECTER_REMARK)))
            .andExpect(jsonPath("$.[*].resolverRemark").value(hasItem(DEFAULT_RESOLVER_REMARK)))
            .andExpect(jsonPath("$.[*].textBeforeCorrection").value(hasItem(DEFAULT_TEXT_BEFORE_CORRECTION)))
            .andExpect(jsonPath("$.[*].textCorrection").value(hasItem(DEFAULT_TEXT_CORRECTION)))
            .andExpect(jsonPath("$.[*].textAfterCorrection").value(hasItem(DEFAULT_TEXT_AFTER_CORRECTION)))
            .andExpect(jsonPath("$.[*].reporterName").value(hasItem(DEFAULT_REPORTER_NAME)))
            .andExpect(jsonPath("$.[*].pageURL").value(hasItem(DEFAULT_PAGE_URL)))
            .andExpect(jsonPath("$.[*].correctionStatus").value(hasItem(DEFAULT_CORRECTION_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getCorrection() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get the correction
        restCorrectionMockMvc.perform(get("/api/corrections/{id}", correction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(correction.getId().intValue()))
            .andExpect(jsonPath("$.reporterRemark").value(DEFAULT_REPORTER_REMARK))
            .andExpect(jsonPath("$.correcterRemark").value(DEFAULT_CORRECTER_REMARK))
            .andExpect(jsonPath("$.resolverRemark").value(DEFAULT_RESOLVER_REMARK))
            .andExpect(jsonPath("$.textBeforeCorrection").value(DEFAULT_TEXT_BEFORE_CORRECTION))
            .andExpect(jsonPath("$.textCorrection").value(DEFAULT_TEXT_CORRECTION))
            .andExpect(jsonPath("$.textAfterCorrection").value(DEFAULT_TEXT_AFTER_CORRECTION))
            .andExpect(jsonPath("$.reporterName").value(DEFAULT_REPORTER_NAME))
            .andExpect(jsonPath("$.pageURL").value(DEFAULT_PAGE_URL))
            .andExpect(jsonPath("$.correctionStatus").value(DEFAULT_CORRECTION_STATUS.toString()));
    }


    @Test
    @Transactional
    public void getCorrectionsByIdFiltering() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        Long id = correction.getId();

        defaultCorrectionShouldBeFound("id.equals=" + id);
        defaultCorrectionShouldNotBeFound("id.notEquals=" + id);

        defaultCorrectionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCorrectionShouldNotBeFound("id.greaterThan=" + id);

        defaultCorrectionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCorrectionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCorrectionsByReporterRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where reporterRemark equals to DEFAULT_REPORTER_REMARK
        defaultCorrectionShouldBeFound("reporterRemark.equals=" + DEFAULT_REPORTER_REMARK);

        // Get all the correctionList where reporterRemark equals to UPDATED_REPORTER_REMARK
        defaultCorrectionShouldNotBeFound("reporterRemark.equals=" + UPDATED_REPORTER_REMARK);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByReporterRemarkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where reporterRemark not equals to DEFAULT_REPORTER_REMARK
        defaultCorrectionShouldNotBeFound("reporterRemark.notEquals=" + DEFAULT_REPORTER_REMARK);

        // Get all the correctionList where reporterRemark not equals to UPDATED_REPORTER_REMARK
        defaultCorrectionShouldBeFound("reporterRemark.notEquals=" + UPDATED_REPORTER_REMARK);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByReporterRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where reporterRemark in DEFAULT_REPORTER_REMARK or UPDATED_REPORTER_REMARK
        defaultCorrectionShouldBeFound("reporterRemark.in=" + DEFAULT_REPORTER_REMARK + "," + UPDATED_REPORTER_REMARK);

        // Get all the correctionList where reporterRemark equals to UPDATED_REPORTER_REMARK
        defaultCorrectionShouldNotBeFound("reporterRemark.in=" + UPDATED_REPORTER_REMARK);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByReporterRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where reporterRemark is not null
        defaultCorrectionShouldBeFound("reporterRemark.specified=true");

        // Get all the correctionList where reporterRemark is null
        defaultCorrectionShouldNotBeFound("reporterRemark.specified=false");
    }
                @Test
    @Transactional
    public void getAllCorrectionsByReporterRemarkContainsSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where reporterRemark contains DEFAULT_REPORTER_REMARK
        defaultCorrectionShouldBeFound("reporterRemark.contains=" + DEFAULT_REPORTER_REMARK);

        // Get all the correctionList where reporterRemark contains UPDATED_REPORTER_REMARK
        defaultCorrectionShouldNotBeFound("reporterRemark.contains=" + UPDATED_REPORTER_REMARK);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByReporterRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where reporterRemark does not contain DEFAULT_REPORTER_REMARK
        defaultCorrectionShouldNotBeFound("reporterRemark.doesNotContain=" + DEFAULT_REPORTER_REMARK);

        // Get all the correctionList where reporterRemark does not contain UPDATED_REPORTER_REMARK
        defaultCorrectionShouldBeFound("reporterRemark.doesNotContain=" + UPDATED_REPORTER_REMARK);
    }


    @Test
    @Transactional
    public void getAllCorrectionsByCorrecterRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where correcterRemark equals to DEFAULT_CORRECTER_REMARK
        defaultCorrectionShouldBeFound("correcterRemark.equals=" + DEFAULT_CORRECTER_REMARK);

        // Get all the correctionList where correcterRemark equals to UPDATED_CORRECTER_REMARK
        defaultCorrectionShouldNotBeFound("correcterRemark.equals=" + UPDATED_CORRECTER_REMARK);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByCorrecterRemarkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where correcterRemark not equals to DEFAULT_CORRECTER_REMARK
        defaultCorrectionShouldNotBeFound("correcterRemark.notEquals=" + DEFAULT_CORRECTER_REMARK);

        // Get all the correctionList where correcterRemark not equals to UPDATED_CORRECTER_REMARK
        defaultCorrectionShouldBeFound("correcterRemark.notEquals=" + UPDATED_CORRECTER_REMARK);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByCorrecterRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where correcterRemark in DEFAULT_CORRECTER_REMARK or UPDATED_CORRECTER_REMARK
        defaultCorrectionShouldBeFound("correcterRemark.in=" + DEFAULT_CORRECTER_REMARK + "," + UPDATED_CORRECTER_REMARK);

        // Get all the correctionList where correcterRemark equals to UPDATED_CORRECTER_REMARK
        defaultCorrectionShouldNotBeFound("correcterRemark.in=" + UPDATED_CORRECTER_REMARK);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByCorrecterRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where correcterRemark is not null
        defaultCorrectionShouldBeFound("correcterRemark.specified=true");

        // Get all the correctionList where correcterRemark is null
        defaultCorrectionShouldNotBeFound("correcterRemark.specified=false");
    }
                @Test
    @Transactional
    public void getAllCorrectionsByCorrecterRemarkContainsSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where correcterRemark contains DEFAULT_CORRECTER_REMARK
        defaultCorrectionShouldBeFound("correcterRemark.contains=" + DEFAULT_CORRECTER_REMARK);

        // Get all the correctionList where correcterRemark contains UPDATED_CORRECTER_REMARK
        defaultCorrectionShouldNotBeFound("correcterRemark.contains=" + UPDATED_CORRECTER_REMARK);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByCorrecterRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where correcterRemark does not contain DEFAULT_CORRECTER_REMARK
        defaultCorrectionShouldNotBeFound("correcterRemark.doesNotContain=" + DEFAULT_CORRECTER_REMARK);

        // Get all the correctionList where correcterRemark does not contain UPDATED_CORRECTER_REMARK
        defaultCorrectionShouldBeFound("correcterRemark.doesNotContain=" + UPDATED_CORRECTER_REMARK);
    }


    @Test
    @Transactional
    public void getAllCorrectionsByResolverRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where resolverRemark equals to DEFAULT_RESOLVER_REMARK
        defaultCorrectionShouldBeFound("resolverRemark.equals=" + DEFAULT_RESOLVER_REMARK);

        // Get all the correctionList where resolverRemark equals to UPDATED_RESOLVER_REMARK
        defaultCorrectionShouldNotBeFound("resolverRemark.equals=" + UPDATED_RESOLVER_REMARK);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByResolverRemarkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where resolverRemark not equals to DEFAULT_RESOLVER_REMARK
        defaultCorrectionShouldNotBeFound("resolverRemark.notEquals=" + DEFAULT_RESOLVER_REMARK);

        // Get all the correctionList where resolverRemark not equals to UPDATED_RESOLVER_REMARK
        defaultCorrectionShouldBeFound("resolverRemark.notEquals=" + UPDATED_RESOLVER_REMARK);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByResolverRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where resolverRemark in DEFAULT_RESOLVER_REMARK or UPDATED_RESOLVER_REMARK
        defaultCorrectionShouldBeFound("resolverRemark.in=" + DEFAULT_RESOLVER_REMARK + "," + UPDATED_RESOLVER_REMARK);

        // Get all the correctionList where resolverRemark equals to UPDATED_RESOLVER_REMARK
        defaultCorrectionShouldNotBeFound("resolverRemark.in=" + UPDATED_RESOLVER_REMARK);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByResolverRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where resolverRemark is not null
        defaultCorrectionShouldBeFound("resolverRemark.specified=true");

        // Get all the correctionList where resolverRemark is null
        defaultCorrectionShouldNotBeFound("resolverRemark.specified=false");
    }
                @Test
    @Transactional
    public void getAllCorrectionsByResolverRemarkContainsSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where resolverRemark contains DEFAULT_RESOLVER_REMARK
        defaultCorrectionShouldBeFound("resolverRemark.contains=" + DEFAULT_RESOLVER_REMARK);

        // Get all the correctionList where resolverRemark contains UPDATED_RESOLVER_REMARK
        defaultCorrectionShouldNotBeFound("resolverRemark.contains=" + UPDATED_RESOLVER_REMARK);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByResolverRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where resolverRemark does not contain DEFAULT_RESOLVER_REMARK
        defaultCorrectionShouldNotBeFound("resolverRemark.doesNotContain=" + DEFAULT_RESOLVER_REMARK);

        // Get all the correctionList where resolverRemark does not contain UPDATED_RESOLVER_REMARK
        defaultCorrectionShouldBeFound("resolverRemark.doesNotContain=" + UPDATED_RESOLVER_REMARK);
    }


    @Test
    @Transactional
    public void getAllCorrectionsByTextBeforeCorrectionIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textBeforeCorrection equals to DEFAULT_TEXT_BEFORE_CORRECTION
        defaultCorrectionShouldBeFound("textBeforeCorrection.equals=" + DEFAULT_TEXT_BEFORE_CORRECTION);

        // Get all the correctionList where textBeforeCorrection equals to UPDATED_TEXT_BEFORE_CORRECTION
        defaultCorrectionShouldNotBeFound("textBeforeCorrection.equals=" + UPDATED_TEXT_BEFORE_CORRECTION);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByTextBeforeCorrectionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textBeforeCorrection not equals to DEFAULT_TEXT_BEFORE_CORRECTION
        defaultCorrectionShouldNotBeFound("textBeforeCorrection.notEquals=" + DEFAULT_TEXT_BEFORE_CORRECTION);

        // Get all the correctionList where textBeforeCorrection not equals to UPDATED_TEXT_BEFORE_CORRECTION
        defaultCorrectionShouldBeFound("textBeforeCorrection.notEquals=" + UPDATED_TEXT_BEFORE_CORRECTION);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByTextBeforeCorrectionIsInShouldWork() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textBeforeCorrection in DEFAULT_TEXT_BEFORE_CORRECTION or UPDATED_TEXT_BEFORE_CORRECTION
        defaultCorrectionShouldBeFound("textBeforeCorrection.in=" + DEFAULT_TEXT_BEFORE_CORRECTION + "," + UPDATED_TEXT_BEFORE_CORRECTION);

        // Get all the correctionList where textBeforeCorrection equals to UPDATED_TEXT_BEFORE_CORRECTION
        defaultCorrectionShouldNotBeFound("textBeforeCorrection.in=" + UPDATED_TEXT_BEFORE_CORRECTION);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByTextBeforeCorrectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textBeforeCorrection is not null
        defaultCorrectionShouldBeFound("textBeforeCorrection.specified=true");

        // Get all the correctionList where textBeforeCorrection is null
        defaultCorrectionShouldNotBeFound("textBeforeCorrection.specified=false");
    }
                @Test
    @Transactional
    public void getAllCorrectionsByTextBeforeCorrectionContainsSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textBeforeCorrection contains DEFAULT_TEXT_BEFORE_CORRECTION
        defaultCorrectionShouldBeFound("textBeforeCorrection.contains=" + DEFAULT_TEXT_BEFORE_CORRECTION);

        // Get all the correctionList where textBeforeCorrection contains UPDATED_TEXT_BEFORE_CORRECTION
        defaultCorrectionShouldNotBeFound("textBeforeCorrection.contains=" + UPDATED_TEXT_BEFORE_CORRECTION);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByTextBeforeCorrectionNotContainsSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textBeforeCorrection does not contain DEFAULT_TEXT_BEFORE_CORRECTION
        defaultCorrectionShouldNotBeFound("textBeforeCorrection.doesNotContain=" + DEFAULT_TEXT_BEFORE_CORRECTION);

        // Get all the correctionList where textBeforeCorrection does not contain UPDATED_TEXT_BEFORE_CORRECTION
        defaultCorrectionShouldBeFound("textBeforeCorrection.doesNotContain=" + UPDATED_TEXT_BEFORE_CORRECTION);
    }


    @Test
    @Transactional
    public void getAllCorrectionsByTextCorrectionIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textCorrection equals to DEFAULT_TEXT_CORRECTION
        defaultCorrectionShouldBeFound("textCorrection.equals=" + DEFAULT_TEXT_CORRECTION);

        // Get all the correctionList where textCorrection equals to UPDATED_TEXT_CORRECTION
        defaultCorrectionShouldNotBeFound("textCorrection.equals=" + UPDATED_TEXT_CORRECTION);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByTextCorrectionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textCorrection not equals to DEFAULT_TEXT_CORRECTION
        defaultCorrectionShouldNotBeFound("textCorrection.notEquals=" + DEFAULT_TEXT_CORRECTION);

        // Get all the correctionList where textCorrection not equals to UPDATED_TEXT_CORRECTION
        defaultCorrectionShouldBeFound("textCorrection.notEquals=" + UPDATED_TEXT_CORRECTION);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByTextCorrectionIsInShouldWork() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textCorrection in DEFAULT_TEXT_CORRECTION or UPDATED_TEXT_CORRECTION
        defaultCorrectionShouldBeFound("textCorrection.in=" + DEFAULT_TEXT_CORRECTION + "," + UPDATED_TEXT_CORRECTION);

        // Get all the correctionList where textCorrection equals to UPDATED_TEXT_CORRECTION
        defaultCorrectionShouldNotBeFound("textCorrection.in=" + UPDATED_TEXT_CORRECTION);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByTextCorrectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textCorrection is not null
        defaultCorrectionShouldBeFound("textCorrection.specified=true");

        // Get all the correctionList where textCorrection is null
        defaultCorrectionShouldNotBeFound("textCorrection.specified=false");
    }
                @Test
    @Transactional
    public void getAllCorrectionsByTextCorrectionContainsSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textCorrection contains DEFAULT_TEXT_CORRECTION
        defaultCorrectionShouldBeFound("textCorrection.contains=" + DEFAULT_TEXT_CORRECTION);

        // Get all the correctionList where textCorrection contains UPDATED_TEXT_CORRECTION
        defaultCorrectionShouldNotBeFound("textCorrection.contains=" + UPDATED_TEXT_CORRECTION);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByTextCorrectionNotContainsSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textCorrection does not contain DEFAULT_TEXT_CORRECTION
        defaultCorrectionShouldNotBeFound("textCorrection.doesNotContain=" + DEFAULT_TEXT_CORRECTION);

        // Get all the correctionList where textCorrection does not contain UPDATED_TEXT_CORRECTION
        defaultCorrectionShouldBeFound("textCorrection.doesNotContain=" + UPDATED_TEXT_CORRECTION);
    }


    @Test
    @Transactional
    public void getAllCorrectionsByTextAfterCorrectionIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textAfterCorrection equals to DEFAULT_TEXT_AFTER_CORRECTION
        defaultCorrectionShouldBeFound("textAfterCorrection.equals=" + DEFAULT_TEXT_AFTER_CORRECTION);

        // Get all the correctionList where textAfterCorrection equals to UPDATED_TEXT_AFTER_CORRECTION
        defaultCorrectionShouldNotBeFound("textAfterCorrection.equals=" + UPDATED_TEXT_AFTER_CORRECTION);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByTextAfterCorrectionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textAfterCorrection not equals to DEFAULT_TEXT_AFTER_CORRECTION
        defaultCorrectionShouldNotBeFound("textAfterCorrection.notEquals=" + DEFAULT_TEXT_AFTER_CORRECTION);

        // Get all the correctionList where textAfterCorrection not equals to UPDATED_TEXT_AFTER_CORRECTION
        defaultCorrectionShouldBeFound("textAfterCorrection.notEquals=" + UPDATED_TEXT_AFTER_CORRECTION);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByTextAfterCorrectionIsInShouldWork() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textAfterCorrection in DEFAULT_TEXT_AFTER_CORRECTION or UPDATED_TEXT_AFTER_CORRECTION
        defaultCorrectionShouldBeFound("textAfterCorrection.in=" + DEFAULT_TEXT_AFTER_CORRECTION + "," + UPDATED_TEXT_AFTER_CORRECTION);

        // Get all the correctionList where textAfterCorrection equals to UPDATED_TEXT_AFTER_CORRECTION
        defaultCorrectionShouldNotBeFound("textAfterCorrection.in=" + UPDATED_TEXT_AFTER_CORRECTION);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByTextAfterCorrectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textAfterCorrection is not null
        defaultCorrectionShouldBeFound("textAfterCorrection.specified=true");

        // Get all the correctionList where textAfterCorrection is null
        defaultCorrectionShouldNotBeFound("textAfterCorrection.specified=false");
    }
                @Test
    @Transactional
    public void getAllCorrectionsByTextAfterCorrectionContainsSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textAfterCorrection contains DEFAULT_TEXT_AFTER_CORRECTION
        defaultCorrectionShouldBeFound("textAfterCorrection.contains=" + DEFAULT_TEXT_AFTER_CORRECTION);

        // Get all the correctionList where textAfterCorrection contains UPDATED_TEXT_AFTER_CORRECTION
        defaultCorrectionShouldNotBeFound("textAfterCorrection.contains=" + UPDATED_TEXT_AFTER_CORRECTION);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByTextAfterCorrectionNotContainsSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where textAfterCorrection does not contain DEFAULT_TEXT_AFTER_CORRECTION
        defaultCorrectionShouldNotBeFound("textAfterCorrection.doesNotContain=" + DEFAULT_TEXT_AFTER_CORRECTION);

        // Get all the correctionList where textAfterCorrection does not contain UPDATED_TEXT_AFTER_CORRECTION
        defaultCorrectionShouldBeFound("textAfterCorrection.doesNotContain=" + UPDATED_TEXT_AFTER_CORRECTION);
    }


    @Test
    @Transactional
    public void getAllCorrectionsByReporterNameIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where reporterName equals to DEFAULT_REPORTER_NAME
        defaultCorrectionShouldBeFound("reporterName.equals=" + DEFAULT_REPORTER_NAME);

        // Get all the correctionList where reporterName equals to UPDATED_REPORTER_NAME
        defaultCorrectionShouldNotBeFound("reporterName.equals=" + UPDATED_REPORTER_NAME);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByReporterNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where reporterName not equals to DEFAULT_REPORTER_NAME
        defaultCorrectionShouldNotBeFound("reporterName.notEquals=" + DEFAULT_REPORTER_NAME);

        // Get all the correctionList where reporterName not equals to UPDATED_REPORTER_NAME
        defaultCorrectionShouldBeFound("reporterName.notEquals=" + UPDATED_REPORTER_NAME);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByReporterNameIsInShouldWork() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where reporterName in DEFAULT_REPORTER_NAME or UPDATED_REPORTER_NAME
        defaultCorrectionShouldBeFound("reporterName.in=" + DEFAULT_REPORTER_NAME + "," + UPDATED_REPORTER_NAME);

        // Get all the correctionList where reporterName equals to UPDATED_REPORTER_NAME
        defaultCorrectionShouldNotBeFound("reporterName.in=" + UPDATED_REPORTER_NAME);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByReporterNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where reporterName is not null
        defaultCorrectionShouldBeFound("reporterName.specified=true");

        // Get all the correctionList where reporterName is null
        defaultCorrectionShouldNotBeFound("reporterName.specified=false");
    }
                @Test
    @Transactional
    public void getAllCorrectionsByReporterNameContainsSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where reporterName contains DEFAULT_REPORTER_NAME
        defaultCorrectionShouldBeFound("reporterName.contains=" + DEFAULT_REPORTER_NAME);

        // Get all the correctionList where reporterName contains UPDATED_REPORTER_NAME
        defaultCorrectionShouldNotBeFound("reporterName.contains=" + UPDATED_REPORTER_NAME);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByReporterNameNotContainsSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where reporterName does not contain DEFAULT_REPORTER_NAME
        defaultCorrectionShouldNotBeFound("reporterName.doesNotContain=" + DEFAULT_REPORTER_NAME);

        // Get all the correctionList where reporterName does not contain UPDATED_REPORTER_NAME
        defaultCorrectionShouldBeFound("reporterName.doesNotContain=" + UPDATED_REPORTER_NAME);
    }


    @Test
    @Transactional
    public void getAllCorrectionsByPageURLIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where pageURL equals to DEFAULT_PAGE_URL
        defaultCorrectionShouldBeFound("pageURL.equals=" + DEFAULT_PAGE_URL);

        // Get all the correctionList where pageURL equals to UPDATED_PAGE_URL
        defaultCorrectionShouldNotBeFound("pageURL.equals=" + UPDATED_PAGE_URL);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByPageURLIsNotEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where pageURL not equals to DEFAULT_PAGE_URL
        defaultCorrectionShouldNotBeFound("pageURL.notEquals=" + DEFAULT_PAGE_URL);

        // Get all the correctionList where pageURL not equals to UPDATED_PAGE_URL
        defaultCorrectionShouldBeFound("pageURL.notEquals=" + UPDATED_PAGE_URL);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByPageURLIsInShouldWork() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where pageURL in DEFAULT_PAGE_URL or UPDATED_PAGE_URL
        defaultCorrectionShouldBeFound("pageURL.in=" + DEFAULT_PAGE_URL + "," + UPDATED_PAGE_URL);

        // Get all the correctionList where pageURL equals to UPDATED_PAGE_URL
        defaultCorrectionShouldNotBeFound("pageURL.in=" + UPDATED_PAGE_URL);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByPageURLIsNullOrNotNull() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where pageURL is not null
        defaultCorrectionShouldBeFound("pageURL.specified=true");

        // Get all the correctionList where pageURL is null
        defaultCorrectionShouldNotBeFound("pageURL.specified=false");
    }
                @Test
    @Transactional
    public void getAllCorrectionsByPageURLContainsSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where pageURL contains DEFAULT_PAGE_URL
        defaultCorrectionShouldBeFound("pageURL.contains=" + DEFAULT_PAGE_URL);

        // Get all the correctionList where pageURL contains UPDATED_PAGE_URL
        defaultCorrectionShouldNotBeFound("pageURL.contains=" + UPDATED_PAGE_URL);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByPageURLNotContainsSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where pageURL does not contain DEFAULT_PAGE_URL
        defaultCorrectionShouldNotBeFound("pageURL.doesNotContain=" + DEFAULT_PAGE_URL);

        // Get all the correctionList where pageURL does not contain UPDATED_PAGE_URL
        defaultCorrectionShouldBeFound("pageURL.doesNotContain=" + UPDATED_PAGE_URL);
    }


    @Test
    @Transactional
    public void getAllCorrectionsByCorrectionStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where correctionStatus equals to DEFAULT_CORRECTION_STATUS
        defaultCorrectionShouldBeFound("correctionStatus.equals=" + DEFAULT_CORRECTION_STATUS);

        // Get all the correctionList where correctionStatus equals to UPDATED_CORRECTION_STATUS
        defaultCorrectionShouldNotBeFound("correctionStatus.equals=" + UPDATED_CORRECTION_STATUS);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByCorrectionStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where correctionStatus not equals to DEFAULT_CORRECTION_STATUS
        defaultCorrectionShouldNotBeFound("correctionStatus.notEquals=" + DEFAULT_CORRECTION_STATUS);

        // Get all the correctionList where correctionStatus not equals to UPDATED_CORRECTION_STATUS
        defaultCorrectionShouldBeFound("correctionStatus.notEquals=" + UPDATED_CORRECTION_STATUS);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByCorrectionStatusIsInShouldWork() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where correctionStatus in DEFAULT_CORRECTION_STATUS or UPDATED_CORRECTION_STATUS
        defaultCorrectionShouldBeFound("correctionStatus.in=" + DEFAULT_CORRECTION_STATUS + "," + UPDATED_CORRECTION_STATUS);

        // Get all the correctionList where correctionStatus equals to UPDATED_CORRECTION_STATUS
        defaultCorrectionShouldNotBeFound("correctionStatus.in=" + UPDATED_CORRECTION_STATUS);
    }

    @Test
    @Transactional
    public void getAllCorrectionsByCorrectionStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList where correctionStatus is not null
        defaultCorrectionShouldBeFound("correctionStatus.specified=true");

        // Get all the correctionList where correctionStatus is null
        defaultCorrectionShouldNotBeFound("correctionStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllCorrectionsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);
        Comment comments = CommentResourceIT.createEntity(em);
        em.persist(comments);
        em.flush();
        correction.addComments(comments);
        correctionRepository.saveAndFlush(correction);
        Long commentsId = comments.getId();

        // Get all the correctionList where comments equals to commentsId
        defaultCorrectionShouldBeFound("commentsId.equals=" + commentsId);

        // Get all the correctionList where comments equals to commentsId + 1
        defaultCorrectionShouldNotBeFound("commentsId.equals=" + (commentsId + 1));
    }


    @Test
    @Transactional
    public void getAllCorrectionsByCorrecterIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);
        Correcter correcter = CorrecterResourceIT.createEntity(em);
        em.persist(correcter);
        em.flush();
        correction.setCorrecter(correcter);
        correctionRepository.saveAndFlush(correction);
        Long correcterId = correcter.getId();

        // Get all the correctionList where correcter equals to correcterId
        defaultCorrectionShouldBeFound("correcterId.equals=" + correcterId);

        // Get all the correctionList where correcter equals to correcterId + 1
        defaultCorrectionShouldNotBeFound("correcterId.equals=" + (correcterId + 1));
    }


    @Test
    @Transactional
    public void getAllCorrectionsByResolverIsEqualToSomething() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);
        Correcter resolver = CorrecterResourceIT.createEntity(em);
        em.persist(resolver);
        em.flush();
        correction.setResolver(resolver);
        correctionRepository.saveAndFlush(correction);
        Long resolverId = resolver.getId();

        // Get all the correctionList where resolver equals to resolverId
        defaultCorrectionShouldBeFound("resolverId.equals=" + resolverId);

        // Get all the correctionList where resolver equals to resolverId + 1
        defaultCorrectionShouldNotBeFound("resolverId.equals=" + (resolverId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCorrectionShouldBeFound(String filter) throws Exception {
        restCorrectionMockMvc.perform(get("/api/corrections?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correction.getId().intValue())))
            .andExpect(jsonPath("$.[*].reporterRemark").value(hasItem(DEFAULT_REPORTER_REMARK)))
            .andExpect(jsonPath("$.[*].correcterRemark").value(hasItem(DEFAULT_CORRECTER_REMARK)))
            .andExpect(jsonPath("$.[*].resolverRemark").value(hasItem(DEFAULT_RESOLVER_REMARK)))
            .andExpect(jsonPath("$.[*].textBeforeCorrection").value(hasItem(DEFAULT_TEXT_BEFORE_CORRECTION)))
            .andExpect(jsonPath("$.[*].textCorrection").value(hasItem(DEFAULT_TEXT_CORRECTION)))
            .andExpect(jsonPath("$.[*].textAfterCorrection").value(hasItem(DEFAULT_TEXT_AFTER_CORRECTION)))
            .andExpect(jsonPath("$.[*].reporterName").value(hasItem(DEFAULT_REPORTER_NAME)))
            .andExpect(jsonPath("$.[*].pageURL").value(hasItem(DEFAULT_PAGE_URL)))
            .andExpect(jsonPath("$.[*].correctionStatus").value(hasItem(DEFAULT_CORRECTION_STATUS.toString())));

        // Check, that the count call also returns 1
        restCorrectionMockMvc.perform(get("/api/corrections/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCorrectionShouldNotBeFound(String filter) throws Exception {
        restCorrectionMockMvc.perform(get("/api/corrections?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCorrectionMockMvc.perform(get("/api/corrections/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCorrection() throws Exception {
        // Get the correction
        restCorrectionMockMvc.perform(get("/api/corrections/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCorrection() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        int databaseSizeBeforeUpdate = correctionRepository.findAll().size();

        // Update the correction
        Correction updatedCorrection = correctionRepository.findById(correction.getId()).get();
        // Disconnect from session so that the updates on updatedCorrection are not directly saved in db
        em.detach(updatedCorrection);
        updatedCorrection
            .reporterRemark(UPDATED_REPORTER_REMARK)
            .correcterRemark(UPDATED_CORRECTER_REMARK)
            .resolverRemark(UPDATED_RESOLVER_REMARK)
            .textBeforeCorrection(UPDATED_TEXT_BEFORE_CORRECTION)
            .textCorrection(UPDATED_TEXT_CORRECTION)
            .textAfterCorrection(UPDATED_TEXT_AFTER_CORRECTION)
            .reporterName(UPDATED_REPORTER_NAME)
            .pageURL(UPDATED_PAGE_URL)
            .correctionStatus(UPDATED_CORRECTION_STATUS);
        CorrectionDTO correctionDTO = correctionMapper.toDto(updatedCorrection);

        restCorrectionMockMvc.perform(put("/api/corrections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correctionDTO)))
            .andExpect(status().isOk());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeUpdate);
        Correction testCorrection = correctionList.get(correctionList.size() - 1);
        assertThat(testCorrection.getReporterRemark()).isEqualTo(UPDATED_REPORTER_REMARK);
        assertThat(testCorrection.getCorrecterRemark()).isEqualTo(UPDATED_CORRECTER_REMARK);
        assertThat(testCorrection.getResolverRemark()).isEqualTo(UPDATED_RESOLVER_REMARK);
        assertThat(testCorrection.getTextBeforeCorrection()).isEqualTo(UPDATED_TEXT_BEFORE_CORRECTION);
        assertThat(testCorrection.getTextCorrection()).isEqualTo(UPDATED_TEXT_CORRECTION);
        assertThat(testCorrection.getTextAfterCorrection()).isEqualTo(UPDATED_TEXT_AFTER_CORRECTION);
        assertThat(testCorrection.getReporterName()).isEqualTo(UPDATED_REPORTER_NAME);
        assertThat(testCorrection.getPageURL()).isEqualTo(UPDATED_PAGE_URL);
        assertThat(testCorrection.getCorrectionStatus()).isEqualTo(UPDATED_CORRECTION_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingCorrection() throws Exception {
        int databaseSizeBeforeUpdate = correctionRepository.findAll().size();

        // Create the Correction
        CorrectionDTO correctionDTO = correctionMapper.toDto(correction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorrectionMockMvc.perform(put("/api/corrections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correctionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCorrection() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        int databaseSizeBeforeDelete = correctionRepository.findAll().size();

        // Delete the correction
        restCorrectionMockMvc.perform(delete("/api/corrections/{id}", correction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
