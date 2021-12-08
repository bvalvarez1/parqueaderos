package ec.loja.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ec.loja.IntegrationTest;
import ec.loja.domain.Institution;
import ec.loja.domain.SystemParameterInstitution;
import ec.loja.domain.SystemParameters;
import ec.loja.repository.SystemParameterInstitutionRepository;
import ec.loja.service.dto.SystemParameterInstitutionDTO;
import ec.loja.service.mapper.SystemParameterInstitutionMapper;
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
 * Integration tests for the {@link SystemParameterInstitutionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemParameterInstitutionResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/system-parameter-institutions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SystemParameterInstitutionRepository systemParameterInstitutionRepository;

    @Autowired
    private SystemParameterInstitutionMapper systemParameterInstitutionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemParameterInstitutionMockMvc;

    private SystemParameterInstitution systemParameterInstitution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemParameterInstitution createEntity(EntityManager em) {
        SystemParameterInstitution systemParameterInstitution = new SystemParameterInstitution()
            .value(DEFAULT_VALUE)
            .active(DEFAULT_ACTIVE);
        // Add required entity
        SystemParameters systemParameters;
        if (TestUtil.findAll(em, SystemParameters.class).isEmpty()) {
            systemParameters = SystemParametersResourceIT.createEntity(em);
            em.persist(systemParameters);
            em.flush();
        } else {
            systemParameters = TestUtil.findAll(em, SystemParameters.class).get(0);
        }
        systemParameterInstitution.setParameter(systemParameters);
        // Add required entity
        Institution institution;
        if (TestUtil.findAll(em, Institution.class).isEmpty()) {
            institution = InstitutionResourceIT.createEntity(em);
            em.persist(institution);
            em.flush();
        } else {
            institution = TestUtil.findAll(em, Institution.class).get(0);
        }
        systemParameterInstitution.setInstitution(institution);
        return systemParameterInstitution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemParameterInstitution createUpdatedEntity(EntityManager em) {
        SystemParameterInstitution systemParameterInstitution = new SystemParameterInstitution()
            .value(UPDATED_VALUE)
            .active(UPDATED_ACTIVE);
        // Add required entity
        SystemParameters systemParameters;
        if (TestUtil.findAll(em, SystemParameters.class).isEmpty()) {
            systemParameters = SystemParametersResourceIT.createUpdatedEntity(em);
            em.persist(systemParameters);
            em.flush();
        } else {
            systemParameters = TestUtil.findAll(em, SystemParameters.class).get(0);
        }
        systemParameterInstitution.setParameter(systemParameters);
        // Add required entity
        Institution institution;
        if (TestUtil.findAll(em, Institution.class).isEmpty()) {
            institution = InstitutionResourceIT.createUpdatedEntity(em);
            em.persist(institution);
            em.flush();
        } else {
            institution = TestUtil.findAll(em, Institution.class).get(0);
        }
        systemParameterInstitution.setInstitution(institution);
        return systemParameterInstitution;
    }

    @BeforeEach
    public void initTest() {
        systemParameterInstitution = createEntity(em);
    }

    @Test
    @Transactional
    void createSystemParameterInstitution() throws Exception {
        int databaseSizeBeforeCreate = systemParameterInstitutionRepository.findAll().size();
        // Create the SystemParameterInstitution
        SystemParameterInstitutionDTO systemParameterInstitutionDTO = systemParameterInstitutionMapper.toDto(systemParameterInstitution);
        restSystemParameterInstitutionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemParameterInstitutionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SystemParameterInstitution in the database
        List<SystemParameterInstitution> systemParameterInstitutionList = systemParameterInstitutionRepository.findAll();
        assertThat(systemParameterInstitutionList).hasSize(databaseSizeBeforeCreate + 1);
        SystemParameterInstitution testSystemParameterInstitution = systemParameterInstitutionList.get(
            systemParameterInstitutionList.size() - 1
        );
        assertThat(testSystemParameterInstitution.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testSystemParameterInstitution.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createSystemParameterInstitutionWithExistingId() throws Exception {
        // Create the SystemParameterInstitution with an existing ID
        systemParameterInstitution.setId(1L);
        SystemParameterInstitutionDTO systemParameterInstitutionDTO = systemParameterInstitutionMapper.toDto(systemParameterInstitution);

        int databaseSizeBeforeCreate = systemParameterInstitutionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemParameterInstitutionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemParameterInstitutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemParameterInstitution in the database
        List<SystemParameterInstitution> systemParameterInstitutionList = systemParameterInstitutionRepository.findAll();
        assertThat(systemParameterInstitutionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemParameterInstitutionRepository.findAll().size();
        // set the field null
        systemParameterInstitution.setValue(null);

        // Create the SystemParameterInstitution, which fails.
        SystemParameterInstitutionDTO systemParameterInstitutionDTO = systemParameterInstitutionMapper.toDto(systemParameterInstitution);

        restSystemParameterInstitutionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemParameterInstitutionDTO))
            )
            .andExpect(status().isBadRequest());

        List<SystemParameterInstitution> systemParameterInstitutionList = systemParameterInstitutionRepository.findAll();
        assertThat(systemParameterInstitutionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemParameterInstitutions() throws Exception {
        // Initialize the database
        systemParameterInstitutionRepository.saveAndFlush(systemParameterInstitution);

        // Get all the systemParameterInstitutionList
        restSystemParameterInstitutionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemParameterInstitution.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getSystemParameterInstitution() throws Exception {
        // Initialize the database
        systemParameterInstitutionRepository.saveAndFlush(systemParameterInstitution);

        // Get the systemParameterInstitution
        restSystemParameterInstitutionMockMvc
            .perform(get(ENTITY_API_URL_ID, systemParameterInstitution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemParameterInstitution.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSystemParameterInstitution() throws Exception {
        // Get the systemParameterInstitution
        restSystemParameterInstitutionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSystemParameterInstitution() throws Exception {
        // Initialize the database
        systemParameterInstitutionRepository.saveAndFlush(systemParameterInstitution);

        int databaseSizeBeforeUpdate = systemParameterInstitutionRepository.findAll().size();

        // Update the systemParameterInstitution
        SystemParameterInstitution updatedSystemParameterInstitution = systemParameterInstitutionRepository
            .findById(systemParameterInstitution.getId())
            .get();
        // Disconnect from session so that the updates on updatedSystemParameterInstitution are not directly saved in db
        em.detach(updatedSystemParameterInstitution);
        updatedSystemParameterInstitution.value(UPDATED_VALUE).active(UPDATED_ACTIVE);
        SystemParameterInstitutionDTO systemParameterInstitutionDTO = systemParameterInstitutionMapper.toDto(
            updatedSystemParameterInstitution
        );

        restSystemParameterInstitutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemParameterInstitutionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemParameterInstitutionDTO))
            )
            .andExpect(status().isOk());

        // Validate the SystemParameterInstitution in the database
        List<SystemParameterInstitution> systemParameterInstitutionList = systemParameterInstitutionRepository.findAll();
        assertThat(systemParameterInstitutionList).hasSize(databaseSizeBeforeUpdate);
        SystemParameterInstitution testSystemParameterInstitution = systemParameterInstitutionList.get(
            systemParameterInstitutionList.size() - 1
        );
        assertThat(testSystemParameterInstitution.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testSystemParameterInstitution.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingSystemParameterInstitution() throws Exception {
        int databaseSizeBeforeUpdate = systemParameterInstitutionRepository.findAll().size();
        systemParameterInstitution.setId(count.incrementAndGet());

        // Create the SystemParameterInstitution
        SystemParameterInstitutionDTO systemParameterInstitutionDTO = systemParameterInstitutionMapper.toDto(systemParameterInstitution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemParameterInstitutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemParameterInstitutionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemParameterInstitutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemParameterInstitution in the database
        List<SystemParameterInstitution> systemParameterInstitutionList = systemParameterInstitutionRepository.findAll();
        assertThat(systemParameterInstitutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemParameterInstitution() throws Exception {
        int databaseSizeBeforeUpdate = systemParameterInstitutionRepository.findAll().size();
        systemParameterInstitution.setId(count.incrementAndGet());

        // Create the SystemParameterInstitution
        SystemParameterInstitutionDTO systemParameterInstitutionDTO = systemParameterInstitutionMapper.toDto(systemParameterInstitution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemParameterInstitutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemParameterInstitutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemParameterInstitution in the database
        List<SystemParameterInstitution> systemParameterInstitutionList = systemParameterInstitutionRepository.findAll();
        assertThat(systemParameterInstitutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemParameterInstitution() throws Exception {
        int databaseSizeBeforeUpdate = systemParameterInstitutionRepository.findAll().size();
        systemParameterInstitution.setId(count.incrementAndGet());

        // Create the SystemParameterInstitution
        SystemParameterInstitutionDTO systemParameterInstitutionDTO = systemParameterInstitutionMapper.toDto(systemParameterInstitution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemParameterInstitutionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemParameterInstitutionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemParameterInstitution in the database
        List<SystemParameterInstitution> systemParameterInstitutionList = systemParameterInstitutionRepository.findAll();
        assertThat(systemParameterInstitutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemParameterInstitutionWithPatch() throws Exception {
        // Initialize the database
        systemParameterInstitutionRepository.saveAndFlush(systemParameterInstitution);

        int databaseSizeBeforeUpdate = systemParameterInstitutionRepository.findAll().size();

        // Update the systemParameterInstitution using partial update
        SystemParameterInstitution partialUpdatedSystemParameterInstitution = new SystemParameterInstitution();
        partialUpdatedSystemParameterInstitution.setId(systemParameterInstitution.getId());

        partialUpdatedSystemParameterInstitution.value(UPDATED_VALUE);

        restSystemParameterInstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemParameterInstitution.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemParameterInstitution))
            )
            .andExpect(status().isOk());

        // Validate the SystemParameterInstitution in the database
        List<SystemParameterInstitution> systemParameterInstitutionList = systemParameterInstitutionRepository.findAll();
        assertThat(systemParameterInstitutionList).hasSize(databaseSizeBeforeUpdate);
        SystemParameterInstitution testSystemParameterInstitution = systemParameterInstitutionList.get(
            systemParameterInstitutionList.size() - 1
        );
        assertThat(testSystemParameterInstitution.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testSystemParameterInstitution.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateSystemParameterInstitutionWithPatch() throws Exception {
        // Initialize the database
        systemParameterInstitutionRepository.saveAndFlush(systemParameterInstitution);

        int databaseSizeBeforeUpdate = systemParameterInstitutionRepository.findAll().size();

        // Update the systemParameterInstitution using partial update
        SystemParameterInstitution partialUpdatedSystemParameterInstitution = new SystemParameterInstitution();
        partialUpdatedSystemParameterInstitution.setId(systemParameterInstitution.getId());

        partialUpdatedSystemParameterInstitution.value(UPDATED_VALUE).active(UPDATED_ACTIVE);

        restSystemParameterInstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemParameterInstitution.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemParameterInstitution))
            )
            .andExpect(status().isOk());

        // Validate the SystemParameterInstitution in the database
        List<SystemParameterInstitution> systemParameterInstitutionList = systemParameterInstitutionRepository.findAll();
        assertThat(systemParameterInstitutionList).hasSize(databaseSizeBeforeUpdate);
        SystemParameterInstitution testSystemParameterInstitution = systemParameterInstitutionList.get(
            systemParameterInstitutionList.size() - 1
        );
        assertThat(testSystemParameterInstitution.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testSystemParameterInstitution.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingSystemParameterInstitution() throws Exception {
        int databaseSizeBeforeUpdate = systemParameterInstitutionRepository.findAll().size();
        systemParameterInstitution.setId(count.incrementAndGet());

        // Create the SystemParameterInstitution
        SystemParameterInstitutionDTO systemParameterInstitutionDTO = systemParameterInstitutionMapper.toDto(systemParameterInstitution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemParameterInstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemParameterInstitutionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemParameterInstitutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemParameterInstitution in the database
        List<SystemParameterInstitution> systemParameterInstitutionList = systemParameterInstitutionRepository.findAll();
        assertThat(systemParameterInstitutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemParameterInstitution() throws Exception {
        int databaseSizeBeforeUpdate = systemParameterInstitutionRepository.findAll().size();
        systemParameterInstitution.setId(count.incrementAndGet());

        // Create the SystemParameterInstitution
        SystemParameterInstitutionDTO systemParameterInstitutionDTO = systemParameterInstitutionMapper.toDto(systemParameterInstitution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemParameterInstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemParameterInstitutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemParameterInstitution in the database
        List<SystemParameterInstitution> systemParameterInstitutionList = systemParameterInstitutionRepository.findAll();
        assertThat(systemParameterInstitutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemParameterInstitution() throws Exception {
        int databaseSizeBeforeUpdate = systemParameterInstitutionRepository.findAll().size();
        systemParameterInstitution.setId(count.incrementAndGet());

        // Create the SystemParameterInstitution
        SystemParameterInstitutionDTO systemParameterInstitutionDTO = systemParameterInstitutionMapper.toDto(systemParameterInstitution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemParameterInstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemParameterInstitutionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemParameterInstitution in the database
        List<SystemParameterInstitution> systemParameterInstitutionList = systemParameterInstitutionRepository.findAll();
        assertThat(systemParameterInstitutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemParameterInstitution() throws Exception {
        // Initialize the database
        systemParameterInstitutionRepository.saveAndFlush(systemParameterInstitution);

        int databaseSizeBeforeDelete = systemParameterInstitutionRepository.findAll().size();

        // Delete the systemParameterInstitution
        restSystemParameterInstitutionMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemParameterInstitution.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SystemParameterInstitution> systemParameterInstitutionList = systemParameterInstitutionRepository.findAll();
        assertThat(systemParameterInstitutionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
