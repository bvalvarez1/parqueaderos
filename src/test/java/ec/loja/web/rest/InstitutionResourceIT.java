package ec.loja.web.rest;

import static ec.loja.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ec.loja.IntegrationTest;
import ec.loja.domain.Institution;
import ec.loja.repository.InstitutionRepository;
import ec.loja.service.dto.InstitutionDTO;
import ec.loja.service.mapper.InstitutionMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InstitutionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InstitutionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Integer DEFAULT_PLACES_NUMBER = 1;
    private static final Integer UPDATED_PLACES_NUMBER = 2;

    private static final String DEFAULT_RUC = "AAAAAAAAAA";
    private static final String UPDATED_RUC = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);

    private static final String DEFAULT_ACRONYM = "AAAAAAAAAA";
    private static final String UPDATED_ACRONYM = "BBBBBBBBBB";

    private static final String DEFAULT_SEQUENCENAME = "AAAAAAAAAA";
    private static final String UPDATED_SEQUENCENAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/institutions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private InstitutionMapper institutionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInstitutionMockMvc;

    private Institution institution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Institution createEntity(EntityManager em) {
        Institution institution = new Institution()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .placesNumber(DEFAULT_PLACES_NUMBER)
            .ruc(DEFAULT_RUC)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .acronym(DEFAULT_ACRONYM)
            .sequencename(DEFAULT_SEQUENCENAME);
        return institution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Institution createUpdatedEntity(EntityManager em) {
        Institution institution = new Institution()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .placesNumber(UPDATED_PLACES_NUMBER)
            .ruc(UPDATED_RUC)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .acronym(UPDATED_ACRONYM)
            .sequencename(UPDATED_SEQUENCENAME);
        return institution;
    }

    @BeforeEach
    public void initTest() {
        institution = createEntity(em);
    }

    @Test
    @Transactional
    void createInstitution() throws Exception {
        int databaseSizeBeforeCreate = institutionRepository.findAll().size();
        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);
        restInstitutionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(institutionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeCreate + 1);
        Institution testInstitution = institutionList.get(institutionList.size() - 1);
        assertThat(testInstitution.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInstitution.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testInstitution.getPlacesNumber()).isEqualTo(DEFAULT_PLACES_NUMBER);
        assertThat(testInstitution.getRuc()).isEqualTo(DEFAULT_RUC);
        assertThat(testInstitution.getLatitude()).isEqualByComparingTo(DEFAULT_LATITUDE);
        assertThat(testInstitution.getLongitude()).isEqualByComparingTo(DEFAULT_LONGITUDE);
        assertThat(testInstitution.getAcronym()).isEqualTo(DEFAULT_ACRONYM);
        assertThat(testInstitution.getSequencename()).isEqualTo(DEFAULT_SEQUENCENAME);
    }

    @Test
    @Transactional
    void createInstitutionWithExistingId() throws Exception {
        // Create the Institution with an existing ID
        institution.setId(1L);
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        int databaseSizeBeforeCreate = institutionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstitutionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(institutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionRepository.findAll().size();
        // set the field null
        institution.setName(null);

        // Create the Institution, which fails.
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        restInstitutionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(institutionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlacesNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionRepository.findAll().size();
        // set the field null
        institution.setPlacesNumber(null);

        // Create the Institution, which fails.
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        restInstitutionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(institutionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRucIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionRepository.findAll().size();
        // set the field null
        institution.setRuc(null);

        // Create the Institution, which fails.
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        restInstitutionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(institutionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAcronymIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionRepository.findAll().size();
        // set the field null
        institution.setAcronym(null);

        // Create the Institution, which fails.
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        restInstitutionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(institutionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSequencenameIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionRepository.findAll().size();
        // set the field null
        institution.setSequencename(null);

        // Create the Institution, which fails.
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        restInstitutionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(institutionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInstitutions() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);

        // Get all the institutionList
        restInstitutionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institution.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].placesNumber").value(hasItem(DEFAULT_PLACES_NUMBER)))
            .andExpect(jsonPath("$.[*].ruc").value(hasItem(DEFAULT_RUC)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(sameNumber(DEFAULT_LATITUDE))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(sameNumber(DEFAULT_LONGITUDE))))
            .andExpect(jsonPath("$.[*].acronym").value(hasItem(DEFAULT_ACRONYM)))
            .andExpect(jsonPath("$.[*].sequencename").value(hasItem(DEFAULT_SEQUENCENAME)));
    }

    @Test
    @Transactional
    void getInstitution() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);

        // Get the institution
        restInstitutionMockMvc
            .perform(get(ENTITY_API_URL_ID, institution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(institution.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.placesNumber").value(DEFAULT_PLACES_NUMBER))
            .andExpect(jsonPath("$.ruc").value(DEFAULT_RUC))
            .andExpect(jsonPath("$.latitude").value(sameNumber(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.longitude").value(sameNumber(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.acronym").value(DEFAULT_ACRONYM))
            .andExpect(jsonPath("$.sequencename").value(DEFAULT_SEQUENCENAME));
    }

    @Test
    @Transactional
    void getNonExistingInstitution() throws Exception {
        // Get the institution
        restInstitutionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInstitution() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);

        int databaseSizeBeforeUpdate = institutionRepository.findAll().size();

        // Update the institution
        Institution updatedInstitution = institutionRepository.findById(institution.getId()).get();
        // Disconnect from session so that the updates on updatedInstitution are not directly saved in db
        em.detach(updatedInstitution);
        updatedInstitution
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .placesNumber(UPDATED_PLACES_NUMBER)
            .ruc(UPDATED_RUC)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .acronym(UPDATED_ACRONYM)
            .sequencename(UPDATED_SEQUENCENAME);
        InstitutionDTO institutionDTO = institutionMapper.toDto(updatedInstitution);

        restInstitutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, institutionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
        Institution testInstitution = institutionList.get(institutionList.size() - 1);
        assertThat(testInstitution.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstitution.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testInstitution.getPlacesNumber()).isEqualTo(UPDATED_PLACES_NUMBER);
        assertThat(testInstitution.getRuc()).isEqualTo(UPDATED_RUC);
        assertThat(testInstitution.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testInstitution.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testInstitution.getAcronym()).isEqualTo(UPDATED_ACRONYM);
        assertThat(testInstitution.getSequencename()).isEqualTo(UPDATED_SEQUENCENAME);
    }

    @Test
    @Transactional
    void putNonExistingInstitution() throws Exception {
        int databaseSizeBeforeUpdate = institutionRepository.findAll().size();
        institution.setId(count.incrementAndGet());

        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstitutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, institutionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInstitution() throws Exception {
        int databaseSizeBeforeUpdate = institutionRepository.findAll().size();
        institution.setId(count.incrementAndGet());

        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstitutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInstitution() throws Exception {
        int databaseSizeBeforeUpdate = institutionRepository.findAll().size();
        institution.setId(count.incrementAndGet());

        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstitutionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(institutionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInstitutionWithPatch() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);

        int databaseSizeBeforeUpdate = institutionRepository.findAll().size();

        // Update the institution using partial update
        Institution partialUpdatedInstitution = new Institution();
        partialUpdatedInstitution.setId(institution.getId());

        partialUpdatedInstitution
            .name(UPDATED_NAME)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .acronym(UPDATED_ACRONYM)
            .sequencename(UPDATED_SEQUENCENAME);

        restInstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstitution.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInstitution))
            )
            .andExpect(status().isOk());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
        Institution testInstitution = institutionList.get(institutionList.size() - 1);
        assertThat(testInstitution.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstitution.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testInstitution.getPlacesNumber()).isEqualTo(DEFAULT_PLACES_NUMBER);
        assertThat(testInstitution.getRuc()).isEqualTo(DEFAULT_RUC);
        assertThat(testInstitution.getLatitude()).isEqualByComparingTo(UPDATED_LATITUDE);
        assertThat(testInstitution.getLongitude()).isEqualByComparingTo(UPDATED_LONGITUDE);
        assertThat(testInstitution.getAcronym()).isEqualTo(UPDATED_ACRONYM);
        assertThat(testInstitution.getSequencename()).isEqualTo(UPDATED_SEQUENCENAME);
    }

    @Test
    @Transactional
    void fullUpdateInstitutionWithPatch() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);

        int databaseSizeBeforeUpdate = institutionRepository.findAll().size();

        // Update the institution using partial update
        Institution partialUpdatedInstitution = new Institution();
        partialUpdatedInstitution.setId(institution.getId());

        partialUpdatedInstitution
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .placesNumber(UPDATED_PLACES_NUMBER)
            .ruc(UPDATED_RUC)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .acronym(UPDATED_ACRONYM)
            .sequencename(UPDATED_SEQUENCENAME);

        restInstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstitution.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInstitution))
            )
            .andExpect(status().isOk());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
        Institution testInstitution = institutionList.get(institutionList.size() - 1);
        assertThat(testInstitution.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstitution.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testInstitution.getPlacesNumber()).isEqualTo(UPDATED_PLACES_NUMBER);
        assertThat(testInstitution.getRuc()).isEqualTo(UPDATED_RUC);
        assertThat(testInstitution.getLatitude()).isEqualByComparingTo(UPDATED_LATITUDE);
        assertThat(testInstitution.getLongitude()).isEqualByComparingTo(UPDATED_LONGITUDE);
        assertThat(testInstitution.getAcronym()).isEqualTo(UPDATED_ACRONYM);
        assertThat(testInstitution.getSequencename()).isEqualTo(UPDATED_SEQUENCENAME);
    }

    @Test
    @Transactional
    void patchNonExistingInstitution() throws Exception {
        int databaseSizeBeforeUpdate = institutionRepository.findAll().size();
        institution.setId(count.incrementAndGet());

        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, institutionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(institutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInstitution() throws Exception {
        int databaseSizeBeforeUpdate = institutionRepository.findAll().size();
        institution.setId(count.incrementAndGet());

        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(institutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInstitution() throws Exception {
        int databaseSizeBeforeUpdate = institutionRepository.findAll().size();
        institution.setId(count.incrementAndGet());

        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(institutionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInstitution() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);

        int databaseSizeBeforeDelete = institutionRepository.findAll().size();

        // Delete the institution
        restInstitutionMockMvc
            .perform(delete(ENTITY_API_URL_ID, institution.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
