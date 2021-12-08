package ec.loja.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ec.loja.IntegrationTest;
import ec.loja.domain.SystemParameters;
import ec.loja.repository.SystemParametersRepository;
import ec.loja.service.dto.SystemParametersDTO;
import ec.loja.service.mapper.SystemParametersMapper;
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
 * Integration tests for the {@link SystemParametersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemParametersResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CLASE = "AAAAAAAAAA";
    private static final String UPDATED_CLASE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/system-parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SystemParametersRepository systemParametersRepository;

    @Autowired
    private SystemParametersMapper systemParametersMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemParametersMockMvc;

    private SystemParameters systemParameters;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemParameters createEntity(EntityManager em) {
        SystemParameters systemParameters = new SystemParameters().name(DEFAULT_NAME).code(DEFAULT_CODE).clase(DEFAULT_CLASE);
        return systemParameters;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemParameters createUpdatedEntity(EntityManager em) {
        SystemParameters systemParameters = new SystemParameters().name(UPDATED_NAME).code(UPDATED_CODE).clase(UPDATED_CLASE);
        return systemParameters;
    }

    @BeforeEach
    public void initTest() {
        systemParameters = createEntity(em);
    }

    @Test
    @Transactional
    void createSystemParameters() throws Exception {
        int databaseSizeBeforeCreate = systemParametersRepository.findAll().size();
        // Create the SystemParameters
        SystemParametersDTO systemParametersDTO = systemParametersMapper.toDto(systemParameters);
        restSystemParametersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemParametersDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SystemParameters in the database
        List<SystemParameters> systemParametersList = systemParametersRepository.findAll();
        assertThat(systemParametersList).hasSize(databaseSizeBeforeCreate + 1);
        SystemParameters testSystemParameters = systemParametersList.get(systemParametersList.size() - 1);
        assertThat(testSystemParameters.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSystemParameters.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSystemParameters.getClase()).isEqualTo(DEFAULT_CLASE);
    }

    @Test
    @Transactional
    void createSystemParametersWithExistingId() throws Exception {
        // Create the SystemParameters with an existing ID
        systemParameters.setId(1L);
        SystemParametersDTO systemParametersDTO = systemParametersMapper.toDto(systemParameters);

        int databaseSizeBeforeCreate = systemParametersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemParametersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemParametersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemParameters in the database
        List<SystemParameters> systemParametersList = systemParametersRepository.findAll();
        assertThat(systemParametersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemParametersRepository.findAll().size();
        // set the field null
        systemParameters.setName(null);

        // Create the SystemParameters, which fails.
        SystemParametersDTO systemParametersDTO = systemParametersMapper.toDto(systemParameters);

        restSystemParametersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemParametersDTO))
            )
            .andExpect(status().isBadRequest());

        List<SystemParameters> systemParametersList = systemParametersRepository.findAll();
        assertThat(systemParametersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemParametersRepository.findAll().size();
        // set the field null
        systemParameters.setCode(null);

        // Create the SystemParameters, which fails.
        SystemParametersDTO systemParametersDTO = systemParametersMapper.toDto(systemParameters);

        restSystemParametersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemParametersDTO))
            )
            .andExpect(status().isBadRequest());

        List<SystemParameters> systemParametersList = systemParametersRepository.findAll();
        assertThat(systemParametersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClaseIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemParametersRepository.findAll().size();
        // set the field null
        systemParameters.setClase(null);

        // Create the SystemParameters, which fails.
        SystemParametersDTO systemParametersDTO = systemParametersMapper.toDto(systemParameters);

        restSystemParametersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemParametersDTO))
            )
            .andExpect(status().isBadRequest());

        List<SystemParameters> systemParametersList = systemParametersRepository.findAll();
        assertThat(systemParametersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemParameters() throws Exception {
        // Initialize the database
        systemParametersRepository.saveAndFlush(systemParameters);

        // Get all the systemParametersList
        restSystemParametersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemParameters.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].clase").value(hasItem(DEFAULT_CLASE)));
    }

    @Test
    @Transactional
    void getSystemParameters() throws Exception {
        // Initialize the database
        systemParametersRepository.saveAndFlush(systemParameters);

        // Get the systemParameters
        restSystemParametersMockMvc
            .perform(get(ENTITY_API_URL_ID, systemParameters.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemParameters.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.clase").value(DEFAULT_CLASE));
    }

    @Test
    @Transactional
    void getNonExistingSystemParameters() throws Exception {
        // Get the systemParameters
        restSystemParametersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSystemParameters() throws Exception {
        // Initialize the database
        systemParametersRepository.saveAndFlush(systemParameters);

        int databaseSizeBeforeUpdate = systemParametersRepository.findAll().size();

        // Update the systemParameters
        SystemParameters updatedSystemParameters = systemParametersRepository.findById(systemParameters.getId()).get();
        // Disconnect from session so that the updates on updatedSystemParameters are not directly saved in db
        em.detach(updatedSystemParameters);
        updatedSystemParameters.name(UPDATED_NAME).code(UPDATED_CODE).clase(UPDATED_CLASE);
        SystemParametersDTO systemParametersDTO = systemParametersMapper.toDto(updatedSystemParameters);

        restSystemParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemParametersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemParametersDTO))
            )
            .andExpect(status().isOk());

        // Validate the SystemParameters in the database
        List<SystemParameters> systemParametersList = systemParametersRepository.findAll();
        assertThat(systemParametersList).hasSize(databaseSizeBeforeUpdate);
        SystemParameters testSystemParameters = systemParametersList.get(systemParametersList.size() - 1);
        assertThat(testSystemParameters.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystemParameters.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSystemParameters.getClase()).isEqualTo(UPDATED_CLASE);
    }

    @Test
    @Transactional
    void putNonExistingSystemParameters() throws Exception {
        int databaseSizeBeforeUpdate = systemParametersRepository.findAll().size();
        systemParameters.setId(count.incrementAndGet());

        // Create the SystemParameters
        SystemParametersDTO systemParametersDTO = systemParametersMapper.toDto(systemParameters);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemParametersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemParametersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemParameters in the database
        List<SystemParameters> systemParametersList = systemParametersRepository.findAll();
        assertThat(systemParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemParameters() throws Exception {
        int databaseSizeBeforeUpdate = systemParametersRepository.findAll().size();
        systemParameters.setId(count.incrementAndGet());

        // Create the SystemParameters
        SystemParametersDTO systemParametersDTO = systemParametersMapper.toDto(systemParameters);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemParametersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemParameters in the database
        List<SystemParameters> systemParametersList = systemParametersRepository.findAll();
        assertThat(systemParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemParameters() throws Exception {
        int databaseSizeBeforeUpdate = systemParametersRepository.findAll().size();
        systemParameters.setId(count.incrementAndGet());

        // Create the SystemParameters
        SystemParametersDTO systemParametersDTO = systemParametersMapper.toDto(systemParameters);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemParametersMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemParametersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemParameters in the database
        List<SystemParameters> systemParametersList = systemParametersRepository.findAll();
        assertThat(systemParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemParametersWithPatch() throws Exception {
        // Initialize the database
        systemParametersRepository.saveAndFlush(systemParameters);

        int databaseSizeBeforeUpdate = systemParametersRepository.findAll().size();

        // Update the systemParameters using partial update
        SystemParameters partialUpdatedSystemParameters = new SystemParameters();
        partialUpdatedSystemParameters.setId(systemParameters.getId());

        partialUpdatedSystemParameters.name(UPDATED_NAME).code(UPDATED_CODE);

        restSystemParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemParameters.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemParameters))
            )
            .andExpect(status().isOk());

        // Validate the SystemParameters in the database
        List<SystemParameters> systemParametersList = systemParametersRepository.findAll();
        assertThat(systemParametersList).hasSize(databaseSizeBeforeUpdate);
        SystemParameters testSystemParameters = systemParametersList.get(systemParametersList.size() - 1);
        assertThat(testSystemParameters.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystemParameters.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSystemParameters.getClase()).isEqualTo(DEFAULT_CLASE);
    }

    @Test
    @Transactional
    void fullUpdateSystemParametersWithPatch() throws Exception {
        // Initialize the database
        systemParametersRepository.saveAndFlush(systemParameters);

        int databaseSizeBeforeUpdate = systemParametersRepository.findAll().size();

        // Update the systemParameters using partial update
        SystemParameters partialUpdatedSystemParameters = new SystemParameters();
        partialUpdatedSystemParameters.setId(systemParameters.getId());

        partialUpdatedSystemParameters.name(UPDATED_NAME).code(UPDATED_CODE).clase(UPDATED_CLASE);

        restSystemParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemParameters.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemParameters))
            )
            .andExpect(status().isOk());

        // Validate the SystemParameters in the database
        List<SystemParameters> systemParametersList = systemParametersRepository.findAll();
        assertThat(systemParametersList).hasSize(databaseSizeBeforeUpdate);
        SystemParameters testSystemParameters = systemParametersList.get(systemParametersList.size() - 1);
        assertThat(testSystemParameters.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystemParameters.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSystemParameters.getClase()).isEqualTo(UPDATED_CLASE);
    }

    @Test
    @Transactional
    void patchNonExistingSystemParameters() throws Exception {
        int databaseSizeBeforeUpdate = systemParametersRepository.findAll().size();
        systemParameters.setId(count.incrementAndGet());

        // Create the SystemParameters
        SystemParametersDTO systemParametersDTO = systemParametersMapper.toDto(systemParameters);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemParametersDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemParametersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemParameters in the database
        List<SystemParameters> systemParametersList = systemParametersRepository.findAll();
        assertThat(systemParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemParameters() throws Exception {
        int databaseSizeBeforeUpdate = systemParametersRepository.findAll().size();
        systemParameters.setId(count.incrementAndGet());

        // Create the SystemParameters
        SystemParametersDTO systemParametersDTO = systemParametersMapper.toDto(systemParameters);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemParametersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemParameters in the database
        List<SystemParameters> systemParametersList = systemParametersRepository.findAll();
        assertThat(systemParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemParameters() throws Exception {
        int databaseSizeBeforeUpdate = systemParametersRepository.findAll().size();
        systemParameters.setId(count.incrementAndGet());

        // Create the SystemParameters
        SystemParametersDTO systemParametersDTO = systemParametersMapper.toDto(systemParameters);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemParametersMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemParametersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemParameters in the database
        List<SystemParameters> systemParametersList = systemParametersRepository.findAll();
        assertThat(systemParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemParameters() throws Exception {
        // Initialize the database
        systemParametersRepository.saveAndFlush(systemParameters);

        int databaseSizeBeforeDelete = systemParametersRepository.findAll().size();

        // Delete the systemParameters
        restSystemParametersMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemParameters.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SystemParameters> systemParametersList = systemParametersRepository.findAll();
        assertThat(systemParametersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
