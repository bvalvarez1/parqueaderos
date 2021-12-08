package ec.loja.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ec.loja.IntegrationTest;
import ec.loja.domain.AuthorityFunctionality;
import ec.loja.domain.Functionality;
import ec.loja.repository.AuthorityFunctionalityRepository;
import ec.loja.service.dto.AuthorityFunctionalityDTO;
import ec.loja.service.mapper.AuthorityFunctionalityMapper;
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
 * Integration tests for the {@link AuthorityFunctionalityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AuthorityFunctionalityResourceIT {

    private static final String DEFAULT_AUTHORITY = "AAAAAAAAAA";
    private static final String UPDATED_AUTHORITY = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/authority-functionalities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AuthorityFunctionalityRepository authorityFunctionalityRepository;

    @Autowired
    private AuthorityFunctionalityMapper authorityFunctionalityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuthorityFunctionalityMockMvc;

    private AuthorityFunctionality authorityFunctionality;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuthorityFunctionality createEntity(EntityManager em) {
        AuthorityFunctionality authorityFunctionality = new AuthorityFunctionality()
            .authority(DEFAULT_AUTHORITY)
            .priority(DEFAULT_PRIORITY)
            .active(DEFAULT_ACTIVE);
        // Add required entity
        Functionality functionality;
        if (TestUtil.findAll(em, Functionality.class).isEmpty()) {
            functionality = FunctionalityResourceIT.createEntity(em);
            em.persist(functionality);
            em.flush();
        } else {
            functionality = TestUtil.findAll(em, Functionality.class).get(0);
        }
        authorityFunctionality.setFunctionality(functionality);
        return authorityFunctionality;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuthorityFunctionality createUpdatedEntity(EntityManager em) {
        AuthorityFunctionality authorityFunctionality = new AuthorityFunctionality()
            .authority(UPDATED_AUTHORITY)
            .priority(UPDATED_PRIORITY)
            .active(UPDATED_ACTIVE);
        // Add required entity
        Functionality functionality;
        if (TestUtil.findAll(em, Functionality.class).isEmpty()) {
            functionality = FunctionalityResourceIT.createUpdatedEntity(em);
            em.persist(functionality);
            em.flush();
        } else {
            functionality = TestUtil.findAll(em, Functionality.class).get(0);
        }
        authorityFunctionality.setFunctionality(functionality);
        return authorityFunctionality;
    }

    @BeforeEach
    public void initTest() {
        authorityFunctionality = createEntity(em);
    }

    @Test
    @Transactional
    void createAuthorityFunctionality() throws Exception {
        int databaseSizeBeforeCreate = authorityFunctionalityRepository.findAll().size();
        // Create the AuthorityFunctionality
        AuthorityFunctionalityDTO authorityFunctionalityDTO = authorityFunctionalityMapper.toDto(authorityFunctionality);
        restAuthorityFunctionalityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorityFunctionalityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AuthorityFunctionality in the database
        List<AuthorityFunctionality> authorityFunctionalityList = authorityFunctionalityRepository.findAll();
        assertThat(authorityFunctionalityList).hasSize(databaseSizeBeforeCreate + 1);
        AuthorityFunctionality testAuthorityFunctionality = authorityFunctionalityList.get(authorityFunctionalityList.size() - 1);
        assertThat(testAuthorityFunctionality.getAuthority()).isEqualTo(DEFAULT_AUTHORITY);
        assertThat(testAuthorityFunctionality.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testAuthorityFunctionality.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createAuthorityFunctionalityWithExistingId() throws Exception {
        // Create the AuthorityFunctionality with an existing ID
        authorityFunctionality.setId(1L);
        AuthorityFunctionalityDTO authorityFunctionalityDTO = authorityFunctionalityMapper.toDto(authorityFunctionality);

        int databaseSizeBeforeCreate = authorityFunctionalityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthorityFunctionalityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorityFunctionalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuthorityFunctionality in the database
        List<AuthorityFunctionality> authorityFunctionalityList = authorityFunctionalityRepository.findAll();
        assertThat(authorityFunctionalityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAuthorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorityFunctionalityRepository.findAll().size();
        // set the field null
        authorityFunctionality.setAuthority(null);

        // Create the AuthorityFunctionality, which fails.
        AuthorityFunctionalityDTO authorityFunctionalityDTO = authorityFunctionalityMapper.toDto(authorityFunctionality);

        restAuthorityFunctionalityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorityFunctionalityDTO))
            )
            .andExpect(status().isBadRequest());

        List<AuthorityFunctionality> authorityFunctionalityList = authorityFunctionalityRepository.findAll();
        assertThat(authorityFunctionalityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorityFunctionalityRepository.findAll().size();
        // set the field null
        authorityFunctionality.setPriority(null);

        // Create the AuthorityFunctionality, which fails.
        AuthorityFunctionalityDTO authorityFunctionalityDTO = authorityFunctionalityMapper.toDto(authorityFunctionality);

        restAuthorityFunctionalityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorityFunctionalityDTO))
            )
            .andExpect(status().isBadRequest());

        List<AuthorityFunctionality> authorityFunctionalityList = authorityFunctionalityRepository.findAll();
        assertThat(authorityFunctionalityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAuthorityFunctionalities() throws Exception {
        // Initialize the database
        authorityFunctionalityRepository.saveAndFlush(authorityFunctionality);

        // Get all the authorityFunctionalityList
        restAuthorityFunctionalityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authorityFunctionality.getId().intValue())))
            .andExpect(jsonPath("$.[*].authority").value(hasItem(DEFAULT_AUTHORITY)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getAuthorityFunctionality() throws Exception {
        // Initialize the database
        authorityFunctionalityRepository.saveAndFlush(authorityFunctionality);

        // Get the authorityFunctionality
        restAuthorityFunctionalityMockMvc
            .perform(get(ENTITY_API_URL_ID, authorityFunctionality.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(authorityFunctionality.getId().intValue()))
            .andExpect(jsonPath("$.authority").value(DEFAULT_AUTHORITY))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAuthorityFunctionality() throws Exception {
        // Get the authorityFunctionality
        restAuthorityFunctionalityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAuthorityFunctionality() throws Exception {
        // Initialize the database
        authorityFunctionalityRepository.saveAndFlush(authorityFunctionality);

        int databaseSizeBeforeUpdate = authorityFunctionalityRepository.findAll().size();

        // Update the authorityFunctionality
        AuthorityFunctionality updatedAuthorityFunctionality = authorityFunctionalityRepository
            .findById(authorityFunctionality.getId())
            .get();
        // Disconnect from session so that the updates on updatedAuthorityFunctionality are not directly saved in db
        em.detach(updatedAuthorityFunctionality);
        updatedAuthorityFunctionality.authority(UPDATED_AUTHORITY).priority(UPDATED_PRIORITY).active(UPDATED_ACTIVE);
        AuthorityFunctionalityDTO authorityFunctionalityDTO = authorityFunctionalityMapper.toDto(updatedAuthorityFunctionality);

        restAuthorityFunctionalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, authorityFunctionalityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorityFunctionalityDTO))
            )
            .andExpect(status().isOk());

        // Validate the AuthorityFunctionality in the database
        List<AuthorityFunctionality> authorityFunctionalityList = authorityFunctionalityRepository.findAll();
        assertThat(authorityFunctionalityList).hasSize(databaseSizeBeforeUpdate);
        AuthorityFunctionality testAuthorityFunctionality = authorityFunctionalityList.get(authorityFunctionalityList.size() - 1);
        assertThat(testAuthorityFunctionality.getAuthority()).isEqualTo(UPDATED_AUTHORITY);
        assertThat(testAuthorityFunctionality.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testAuthorityFunctionality.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingAuthorityFunctionality() throws Exception {
        int databaseSizeBeforeUpdate = authorityFunctionalityRepository.findAll().size();
        authorityFunctionality.setId(count.incrementAndGet());

        // Create the AuthorityFunctionality
        AuthorityFunctionalityDTO authorityFunctionalityDTO = authorityFunctionalityMapper.toDto(authorityFunctionality);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthorityFunctionalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, authorityFunctionalityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorityFunctionalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuthorityFunctionality in the database
        List<AuthorityFunctionality> authorityFunctionalityList = authorityFunctionalityRepository.findAll();
        assertThat(authorityFunctionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAuthorityFunctionality() throws Exception {
        int databaseSizeBeforeUpdate = authorityFunctionalityRepository.findAll().size();
        authorityFunctionality.setId(count.incrementAndGet());

        // Create the AuthorityFunctionality
        AuthorityFunctionalityDTO authorityFunctionalityDTO = authorityFunctionalityMapper.toDto(authorityFunctionality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorityFunctionalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorityFunctionalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuthorityFunctionality in the database
        List<AuthorityFunctionality> authorityFunctionalityList = authorityFunctionalityRepository.findAll();
        assertThat(authorityFunctionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAuthorityFunctionality() throws Exception {
        int databaseSizeBeforeUpdate = authorityFunctionalityRepository.findAll().size();
        authorityFunctionality.setId(count.incrementAndGet());

        // Create the AuthorityFunctionality
        AuthorityFunctionalityDTO authorityFunctionalityDTO = authorityFunctionalityMapper.toDto(authorityFunctionality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorityFunctionalityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorityFunctionalityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AuthorityFunctionality in the database
        List<AuthorityFunctionality> authorityFunctionalityList = authorityFunctionalityRepository.findAll();
        assertThat(authorityFunctionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAuthorityFunctionalityWithPatch() throws Exception {
        // Initialize the database
        authorityFunctionalityRepository.saveAndFlush(authorityFunctionality);

        int databaseSizeBeforeUpdate = authorityFunctionalityRepository.findAll().size();

        // Update the authorityFunctionality using partial update
        AuthorityFunctionality partialUpdatedAuthorityFunctionality = new AuthorityFunctionality();
        partialUpdatedAuthorityFunctionality.setId(authorityFunctionality.getId());

        partialUpdatedAuthorityFunctionality.authority(UPDATED_AUTHORITY);

        restAuthorityFunctionalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuthorityFunctionality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAuthorityFunctionality))
            )
            .andExpect(status().isOk());

        // Validate the AuthorityFunctionality in the database
        List<AuthorityFunctionality> authorityFunctionalityList = authorityFunctionalityRepository.findAll();
        assertThat(authorityFunctionalityList).hasSize(databaseSizeBeforeUpdate);
        AuthorityFunctionality testAuthorityFunctionality = authorityFunctionalityList.get(authorityFunctionalityList.size() - 1);
        assertThat(testAuthorityFunctionality.getAuthority()).isEqualTo(UPDATED_AUTHORITY);
        assertThat(testAuthorityFunctionality.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testAuthorityFunctionality.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateAuthorityFunctionalityWithPatch() throws Exception {
        // Initialize the database
        authorityFunctionalityRepository.saveAndFlush(authorityFunctionality);

        int databaseSizeBeforeUpdate = authorityFunctionalityRepository.findAll().size();

        // Update the authorityFunctionality using partial update
        AuthorityFunctionality partialUpdatedAuthorityFunctionality = new AuthorityFunctionality();
        partialUpdatedAuthorityFunctionality.setId(authorityFunctionality.getId());

        partialUpdatedAuthorityFunctionality.authority(UPDATED_AUTHORITY).priority(UPDATED_PRIORITY).active(UPDATED_ACTIVE);

        restAuthorityFunctionalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuthorityFunctionality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAuthorityFunctionality))
            )
            .andExpect(status().isOk());

        // Validate the AuthorityFunctionality in the database
        List<AuthorityFunctionality> authorityFunctionalityList = authorityFunctionalityRepository.findAll();
        assertThat(authorityFunctionalityList).hasSize(databaseSizeBeforeUpdate);
        AuthorityFunctionality testAuthorityFunctionality = authorityFunctionalityList.get(authorityFunctionalityList.size() - 1);
        assertThat(testAuthorityFunctionality.getAuthority()).isEqualTo(UPDATED_AUTHORITY);
        assertThat(testAuthorityFunctionality.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testAuthorityFunctionality.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingAuthorityFunctionality() throws Exception {
        int databaseSizeBeforeUpdate = authorityFunctionalityRepository.findAll().size();
        authorityFunctionality.setId(count.incrementAndGet());

        // Create the AuthorityFunctionality
        AuthorityFunctionalityDTO authorityFunctionalityDTO = authorityFunctionalityMapper.toDto(authorityFunctionality);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthorityFunctionalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, authorityFunctionalityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(authorityFunctionalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuthorityFunctionality in the database
        List<AuthorityFunctionality> authorityFunctionalityList = authorityFunctionalityRepository.findAll();
        assertThat(authorityFunctionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAuthorityFunctionality() throws Exception {
        int databaseSizeBeforeUpdate = authorityFunctionalityRepository.findAll().size();
        authorityFunctionality.setId(count.incrementAndGet());

        // Create the AuthorityFunctionality
        AuthorityFunctionalityDTO authorityFunctionalityDTO = authorityFunctionalityMapper.toDto(authorityFunctionality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorityFunctionalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(authorityFunctionalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuthorityFunctionality in the database
        List<AuthorityFunctionality> authorityFunctionalityList = authorityFunctionalityRepository.findAll();
        assertThat(authorityFunctionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAuthorityFunctionality() throws Exception {
        int databaseSizeBeforeUpdate = authorityFunctionalityRepository.findAll().size();
        authorityFunctionality.setId(count.incrementAndGet());

        // Create the AuthorityFunctionality
        AuthorityFunctionalityDTO authorityFunctionalityDTO = authorityFunctionalityMapper.toDto(authorityFunctionality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorityFunctionalityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(authorityFunctionalityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AuthorityFunctionality in the database
        List<AuthorityFunctionality> authorityFunctionalityList = authorityFunctionalityRepository.findAll();
        assertThat(authorityFunctionalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAuthorityFunctionality() throws Exception {
        // Initialize the database
        authorityFunctionalityRepository.saveAndFlush(authorityFunctionality);

        int databaseSizeBeforeDelete = authorityFunctionalityRepository.findAll().size();

        // Delete the authorityFunctionality
        restAuthorityFunctionalityMockMvc
            .perform(delete(ENTITY_API_URL_ID, authorityFunctionality.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AuthorityFunctionality> authorityFunctionalityList = authorityFunctionalityRepository.findAll();
        assertThat(authorityFunctionalityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
