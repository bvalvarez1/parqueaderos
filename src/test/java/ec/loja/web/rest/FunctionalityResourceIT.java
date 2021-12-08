package ec.loja.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ec.loja.IntegrationTest;
import ec.loja.domain.Functionality;
import ec.loja.repository.FunctionalityRepository;
import ec.loja.service.dto.FunctionalityDTO;
import ec.loja.service.mapper.FunctionalityMapper;
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
 * Integration tests for the {@link FunctionalityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FunctionalityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/functionalities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FunctionalityRepository functionalityRepository;

    @Autowired
    private FunctionalityMapper functionalityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFunctionalityMockMvc;

    private Functionality functionality;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Functionality createEntity(EntityManager em) {
        Functionality functionality = new Functionality()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .icon(DEFAULT_ICON)
            .url(DEFAULT_URL)
            .active(DEFAULT_ACTIVE);
        return functionality;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Functionality createUpdatedEntity(EntityManager em) {
        Functionality functionality = new Functionality()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .icon(UPDATED_ICON)
            .url(UPDATED_URL)
            .active(UPDATED_ACTIVE);
        return functionality;
    }

    @BeforeEach
    public void initTest() {
        functionality = createEntity(em);
    }

    @Test
    @Transactional
    void createFunctionality() throws Exception {
        int databaseSizeBeforeCreate = functionalityRepository.findAll().size();
        // Create the Functionality
        FunctionalityDTO functionalityDTO = functionalityMapper.toDto(functionality);
        restFunctionalityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(functionalityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Functionality in the database
        List<Functionality> functionalityList = functionalityRepository.findAll();
        assertThat(functionalityList).hasSize(databaseSizeBeforeCreate + 1);
        Functionality testFunctionality = functionalityList.get(functionalityList.size() - 1);
        assertThat(testFunctionality.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFunctionality.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFunctionality.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testFunctionality.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testFunctionality.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createFunctionalityWithExistingId() throws Exception {
        // Create the Functionality with an existing ID
        functionality.setId(1L);
        FunctionalityDTO functionalityDTO = functionalityMapper.toDto(functionality);

        int databaseSizeBeforeCreate = functionalityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFunctionalityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(functionalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Functionality in the database
        List<Functionality> functionalityList = functionalityRepository.findAll();
        assertThat(functionalityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = functionalityRepository.findAll().size();
        // set the field null
        functionality.setName(null);

        // Create the Functionality, which fails.
        FunctionalityDTO functionalityDTO = functionalityMapper.toDto(functionality);

        restFunctionalityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(functionalityDTO))
            )
            .andExpect(status().isBadRequest());

        List<Functionality> functionalityList = functionalityRepository.findAll();
        assertThat(functionalityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = functionalityRepository.findAll().size();
        // set the field null
        functionality.setActive(null);

        // Create the Functionality, which fails.
        FunctionalityDTO functionalityDTO = functionalityMapper.toDto(functionality);

        restFunctionalityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(functionalityDTO))
            )
            .andExpect(status().isBadRequest());

        List<Functionality> functionalityList = functionalityRepository.findAll();
        assertThat(functionalityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFunctionalities() throws Exception {
        // Initialize the database
        functionalityRepository.saveAndFlush(functionality);

        // Get all the functionalityList
        restFunctionalityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(functionality.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getFunctionality() throws Exception {
        // Initialize the database
        functionalityRepository.saveAndFlush(functionality);

        // Get the functionality
        restFunctionalityMockMvc
            .perform(get(ENTITY_API_URL_ID, functionality.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(functionality.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFunctionality() throws Exception {
        // Get the functionality
        restFunctionalityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFunctionality() throws Exception {
        // Initialize the database
        functionalityRepository.saveAndFlush(functionality);

        int databaseSizeBeforeUpdate = functionalityRepository.findAll().size();

        // Update the functionality
        Functionality updatedFunctionality = functionalityRepository.findById(functionality.getId()).get();
        // Disconnect from session so that the updates on updatedFunctionality are not directly saved in db
        em.detach(updatedFunctionality);
        updatedFunctionality.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).icon(UPDATED_ICON).url(UPDATED_URL).active(UPDATED_ACTIVE);
        FunctionalityDTO functionalityDTO = functionalityMapper.toDto(updatedFunctionality);

        restFunctionalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, functionalityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(functionalityDTO))
            )
            .andExpect(status().isOk());

        // Validate the Functionality in the database
        List<Functionality> functionalityList = functionalityRepository.findAll();
        assertThat(functionalityList).hasSize(databaseSizeBeforeUpdate);
        Functionality testFunctionality = functionalityList.get(functionalityList.size() - 1);
        assertThat(testFunctionality.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFunctionality.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFunctionality.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testFunctionality.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testFunctionality.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingFunctionality() throws Exception {
        int databaseSizeBeforeUpdate = functionalityRepository.findAll().size();
        functionality.setId(count.incrementAndGet());

        // Create the Functionality
        FunctionalityDTO functionalityDTO = functionalityMapper.toDto(functionality);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFunctionalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, functionalityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(functionalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Functionality in the database
        List<Functionality> functionalityList = functionalityRepository.findAll();
        assertThat(functionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFunctionality() throws Exception {
        int databaseSizeBeforeUpdate = functionalityRepository.findAll().size();
        functionality.setId(count.incrementAndGet());

        // Create the Functionality
        FunctionalityDTO functionalityDTO = functionalityMapper.toDto(functionality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunctionalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(functionalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Functionality in the database
        List<Functionality> functionalityList = functionalityRepository.findAll();
        assertThat(functionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFunctionality() throws Exception {
        int databaseSizeBeforeUpdate = functionalityRepository.findAll().size();
        functionality.setId(count.incrementAndGet());

        // Create the Functionality
        FunctionalityDTO functionalityDTO = functionalityMapper.toDto(functionality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunctionalityMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(functionalityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Functionality in the database
        List<Functionality> functionalityList = functionalityRepository.findAll();
        assertThat(functionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFunctionalityWithPatch() throws Exception {
        // Initialize the database
        functionalityRepository.saveAndFlush(functionality);

        int databaseSizeBeforeUpdate = functionalityRepository.findAll().size();

        // Update the functionality using partial update
        Functionality partialUpdatedFunctionality = new Functionality();
        partialUpdatedFunctionality.setId(functionality.getId());

        partialUpdatedFunctionality.icon(UPDATED_ICON).url(UPDATED_URL).active(UPDATED_ACTIVE);

        restFunctionalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFunctionality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFunctionality))
            )
            .andExpect(status().isOk());

        // Validate the Functionality in the database
        List<Functionality> functionalityList = functionalityRepository.findAll();
        assertThat(functionalityList).hasSize(databaseSizeBeforeUpdate);
        Functionality testFunctionality = functionalityList.get(functionalityList.size() - 1);
        assertThat(testFunctionality.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFunctionality.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFunctionality.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testFunctionality.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testFunctionality.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateFunctionalityWithPatch() throws Exception {
        // Initialize the database
        functionalityRepository.saveAndFlush(functionality);

        int databaseSizeBeforeUpdate = functionalityRepository.findAll().size();

        // Update the functionality using partial update
        Functionality partialUpdatedFunctionality = new Functionality();
        partialUpdatedFunctionality.setId(functionality.getId());

        partialUpdatedFunctionality
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .icon(UPDATED_ICON)
            .url(UPDATED_URL)
            .active(UPDATED_ACTIVE);

        restFunctionalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFunctionality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFunctionality))
            )
            .andExpect(status().isOk());

        // Validate the Functionality in the database
        List<Functionality> functionalityList = functionalityRepository.findAll();
        assertThat(functionalityList).hasSize(databaseSizeBeforeUpdate);
        Functionality testFunctionality = functionalityList.get(functionalityList.size() - 1);
        assertThat(testFunctionality.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFunctionality.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFunctionality.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testFunctionality.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testFunctionality.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingFunctionality() throws Exception {
        int databaseSizeBeforeUpdate = functionalityRepository.findAll().size();
        functionality.setId(count.incrementAndGet());

        // Create the Functionality
        FunctionalityDTO functionalityDTO = functionalityMapper.toDto(functionality);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFunctionalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, functionalityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(functionalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Functionality in the database
        List<Functionality> functionalityList = functionalityRepository.findAll();
        assertThat(functionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFunctionality() throws Exception {
        int databaseSizeBeforeUpdate = functionalityRepository.findAll().size();
        functionality.setId(count.incrementAndGet());

        // Create the Functionality
        FunctionalityDTO functionalityDTO = functionalityMapper.toDto(functionality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunctionalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(functionalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Functionality in the database
        List<Functionality> functionalityList = functionalityRepository.findAll();
        assertThat(functionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFunctionality() throws Exception {
        int databaseSizeBeforeUpdate = functionalityRepository.findAll().size();
        functionality.setId(count.incrementAndGet());

        // Create the Functionality
        FunctionalityDTO functionalityDTO = functionalityMapper.toDto(functionality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunctionalityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(functionalityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Functionality in the database
        List<Functionality> functionalityList = functionalityRepository.findAll();
        assertThat(functionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFunctionality() throws Exception {
        // Initialize the database
        functionalityRepository.saveAndFlush(functionality);

        int databaseSizeBeforeDelete = functionalityRepository.findAll().size();

        // Delete the functionality
        restFunctionalityMockMvc
            .perform(delete(ENTITY_API_URL_ID, functionality.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Functionality> functionalityList = functionalityRepository.findAll();
        assertThat(functionalityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
