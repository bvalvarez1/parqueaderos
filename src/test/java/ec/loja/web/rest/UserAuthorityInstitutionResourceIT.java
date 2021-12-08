package ec.loja.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ec.loja.IntegrationTest;
import ec.loja.domain.UserAuthorityInstitution;
import ec.loja.repository.UserAuthorityInstitutionRepository;
import ec.loja.service.dto.UserAuthorityInstitutionDTO;
import ec.loja.service.mapper.UserAuthorityInstitutionMapper;
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
 * Integration tests for the {@link UserAuthorityInstitutionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAuthorityInstitutionResourceIT {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/user-authority-institutions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserAuthorityInstitutionRepository userAuthorityInstitutionRepository;

    @Autowired
    private UserAuthorityInstitutionMapper userAuthorityInstitutionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAuthorityInstitutionMockMvc;

    private UserAuthorityInstitution userAuthorityInstitution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAuthorityInstitution createEntity(EntityManager em) {
        UserAuthorityInstitution userAuthorityInstitution = new UserAuthorityInstitution().active(DEFAULT_ACTIVE);
        return userAuthorityInstitution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAuthorityInstitution createUpdatedEntity(EntityManager em) {
        UserAuthorityInstitution userAuthorityInstitution = new UserAuthorityInstitution().active(UPDATED_ACTIVE);
        return userAuthorityInstitution;
    }

    @BeforeEach
    public void initTest() {
        userAuthorityInstitution = createEntity(em);
    }

    @Test
    @Transactional
    void createUserAuthorityInstitution() throws Exception {
        int databaseSizeBeforeCreate = userAuthorityInstitutionRepository.findAll().size();
        // Create the UserAuthorityInstitution
        UserAuthorityInstitutionDTO userAuthorityInstitutionDTO = userAuthorityInstitutionMapper.toDto(userAuthorityInstitution);
        restUserAuthorityInstitutionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAuthorityInstitutionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserAuthorityInstitution in the database
        List<UserAuthorityInstitution> userAuthorityInstitutionList = userAuthorityInstitutionRepository.findAll();
        assertThat(userAuthorityInstitutionList).hasSize(databaseSizeBeforeCreate + 1);
        UserAuthorityInstitution testUserAuthorityInstitution = userAuthorityInstitutionList.get(userAuthorityInstitutionList.size() - 1);
        assertThat(testUserAuthorityInstitution.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createUserAuthorityInstitutionWithExistingId() throws Exception {
        // Create the UserAuthorityInstitution with an existing ID
        userAuthorityInstitution.setId(1L);
        UserAuthorityInstitutionDTO userAuthorityInstitutionDTO = userAuthorityInstitutionMapper.toDto(userAuthorityInstitution);

        int databaseSizeBeforeCreate = userAuthorityInstitutionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAuthorityInstitutionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAuthorityInstitutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAuthorityInstitution in the database
        List<UserAuthorityInstitution> userAuthorityInstitutionList = userAuthorityInstitutionRepository.findAll();
        assertThat(userAuthorityInstitutionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserAuthorityInstitutions() throws Exception {
        // Initialize the database
        userAuthorityInstitutionRepository.saveAndFlush(userAuthorityInstitution);

        // Get all the userAuthorityInstitutionList
        restUserAuthorityInstitutionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAuthorityInstitution.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getUserAuthorityInstitution() throws Exception {
        // Initialize the database
        userAuthorityInstitutionRepository.saveAndFlush(userAuthorityInstitution);

        // Get the userAuthorityInstitution
        restUserAuthorityInstitutionMockMvc
            .perform(get(ENTITY_API_URL_ID, userAuthorityInstitution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAuthorityInstitution.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserAuthorityInstitution() throws Exception {
        // Get the userAuthorityInstitution
        restUserAuthorityInstitutionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserAuthorityInstitution() throws Exception {
        // Initialize the database
        userAuthorityInstitutionRepository.saveAndFlush(userAuthorityInstitution);

        int databaseSizeBeforeUpdate = userAuthorityInstitutionRepository.findAll().size();

        // Update the userAuthorityInstitution
        UserAuthorityInstitution updatedUserAuthorityInstitution = userAuthorityInstitutionRepository
            .findById(userAuthorityInstitution.getId())
            .get();
        // Disconnect from session so that the updates on updatedUserAuthorityInstitution are not directly saved in db
        em.detach(updatedUserAuthorityInstitution);
        updatedUserAuthorityInstitution.active(UPDATED_ACTIVE);
        UserAuthorityInstitutionDTO userAuthorityInstitutionDTO = userAuthorityInstitutionMapper.toDto(updatedUserAuthorityInstitution);

        restUserAuthorityInstitutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAuthorityInstitutionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAuthorityInstitutionDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserAuthorityInstitution in the database
        List<UserAuthorityInstitution> userAuthorityInstitutionList = userAuthorityInstitutionRepository.findAll();
        assertThat(userAuthorityInstitutionList).hasSize(databaseSizeBeforeUpdate);
        UserAuthorityInstitution testUserAuthorityInstitution = userAuthorityInstitutionList.get(userAuthorityInstitutionList.size() - 1);
        assertThat(testUserAuthorityInstitution.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingUserAuthorityInstitution() throws Exception {
        int databaseSizeBeforeUpdate = userAuthorityInstitutionRepository.findAll().size();
        userAuthorityInstitution.setId(count.incrementAndGet());

        // Create the UserAuthorityInstitution
        UserAuthorityInstitutionDTO userAuthorityInstitutionDTO = userAuthorityInstitutionMapper.toDto(userAuthorityInstitution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAuthorityInstitutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAuthorityInstitutionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAuthorityInstitutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAuthorityInstitution in the database
        List<UserAuthorityInstitution> userAuthorityInstitutionList = userAuthorityInstitutionRepository.findAll();
        assertThat(userAuthorityInstitutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAuthorityInstitution() throws Exception {
        int databaseSizeBeforeUpdate = userAuthorityInstitutionRepository.findAll().size();
        userAuthorityInstitution.setId(count.incrementAndGet());

        // Create the UserAuthorityInstitution
        UserAuthorityInstitutionDTO userAuthorityInstitutionDTO = userAuthorityInstitutionMapper.toDto(userAuthorityInstitution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAuthorityInstitutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAuthorityInstitutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAuthorityInstitution in the database
        List<UserAuthorityInstitution> userAuthorityInstitutionList = userAuthorityInstitutionRepository.findAll();
        assertThat(userAuthorityInstitutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAuthorityInstitution() throws Exception {
        int databaseSizeBeforeUpdate = userAuthorityInstitutionRepository.findAll().size();
        userAuthorityInstitution.setId(count.incrementAndGet());

        // Create the UserAuthorityInstitution
        UserAuthorityInstitutionDTO userAuthorityInstitutionDTO = userAuthorityInstitutionMapper.toDto(userAuthorityInstitution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAuthorityInstitutionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAuthorityInstitutionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAuthorityInstitution in the database
        List<UserAuthorityInstitution> userAuthorityInstitutionList = userAuthorityInstitutionRepository.findAll();
        assertThat(userAuthorityInstitutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAuthorityInstitutionWithPatch() throws Exception {
        // Initialize the database
        userAuthorityInstitutionRepository.saveAndFlush(userAuthorityInstitution);

        int databaseSizeBeforeUpdate = userAuthorityInstitutionRepository.findAll().size();

        // Update the userAuthorityInstitution using partial update
        UserAuthorityInstitution partialUpdatedUserAuthorityInstitution = new UserAuthorityInstitution();
        partialUpdatedUserAuthorityInstitution.setId(userAuthorityInstitution.getId());

        partialUpdatedUserAuthorityInstitution.active(UPDATED_ACTIVE);

        restUserAuthorityInstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAuthorityInstitution.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAuthorityInstitution))
            )
            .andExpect(status().isOk());

        // Validate the UserAuthorityInstitution in the database
        List<UserAuthorityInstitution> userAuthorityInstitutionList = userAuthorityInstitutionRepository.findAll();
        assertThat(userAuthorityInstitutionList).hasSize(databaseSizeBeforeUpdate);
        UserAuthorityInstitution testUserAuthorityInstitution = userAuthorityInstitutionList.get(userAuthorityInstitutionList.size() - 1);
        assertThat(testUserAuthorityInstitution.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateUserAuthorityInstitutionWithPatch() throws Exception {
        // Initialize the database
        userAuthorityInstitutionRepository.saveAndFlush(userAuthorityInstitution);

        int databaseSizeBeforeUpdate = userAuthorityInstitutionRepository.findAll().size();

        // Update the userAuthorityInstitution using partial update
        UserAuthorityInstitution partialUpdatedUserAuthorityInstitution = new UserAuthorityInstitution();
        partialUpdatedUserAuthorityInstitution.setId(userAuthorityInstitution.getId());

        partialUpdatedUserAuthorityInstitution.active(UPDATED_ACTIVE);

        restUserAuthorityInstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAuthorityInstitution.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAuthorityInstitution))
            )
            .andExpect(status().isOk());

        // Validate the UserAuthorityInstitution in the database
        List<UserAuthorityInstitution> userAuthorityInstitutionList = userAuthorityInstitutionRepository.findAll();
        assertThat(userAuthorityInstitutionList).hasSize(databaseSizeBeforeUpdate);
        UserAuthorityInstitution testUserAuthorityInstitution = userAuthorityInstitutionList.get(userAuthorityInstitutionList.size() - 1);
        assertThat(testUserAuthorityInstitution.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingUserAuthorityInstitution() throws Exception {
        int databaseSizeBeforeUpdate = userAuthorityInstitutionRepository.findAll().size();
        userAuthorityInstitution.setId(count.incrementAndGet());

        // Create the UserAuthorityInstitution
        UserAuthorityInstitutionDTO userAuthorityInstitutionDTO = userAuthorityInstitutionMapper.toDto(userAuthorityInstitution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAuthorityInstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAuthorityInstitutionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAuthorityInstitutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAuthorityInstitution in the database
        List<UserAuthorityInstitution> userAuthorityInstitutionList = userAuthorityInstitutionRepository.findAll();
        assertThat(userAuthorityInstitutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAuthorityInstitution() throws Exception {
        int databaseSizeBeforeUpdate = userAuthorityInstitutionRepository.findAll().size();
        userAuthorityInstitution.setId(count.incrementAndGet());

        // Create the UserAuthorityInstitution
        UserAuthorityInstitutionDTO userAuthorityInstitutionDTO = userAuthorityInstitutionMapper.toDto(userAuthorityInstitution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAuthorityInstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAuthorityInstitutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAuthorityInstitution in the database
        List<UserAuthorityInstitution> userAuthorityInstitutionList = userAuthorityInstitutionRepository.findAll();
        assertThat(userAuthorityInstitutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAuthorityInstitution() throws Exception {
        int databaseSizeBeforeUpdate = userAuthorityInstitutionRepository.findAll().size();
        userAuthorityInstitution.setId(count.incrementAndGet());

        // Create the UserAuthorityInstitution
        UserAuthorityInstitutionDTO userAuthorityInstitutionDTO = userAuthorityInstitutionMapper.toDto(userAuthorityInstitution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAuthorityInstitutionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAuthorityInstitutionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAuthorityInstitution in the database
        List<UserAuthorityInstitution> userAuthorityInstitutionList = userAuthorityInstitutionRepository.findAll();
        assertThat(userAuthorityInstitutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAuthorityInstitution() throws Exception {
        // Initialize the database
        userAuthorityInstitutionRepository.saveAndFlush(userAuthorityInstitution);

        int databaseSizeBeforeDelete = userAuthorityInstitutionRepository.findAll().size();

        // Delete the userAuthorityInstitution
        restUserAuthorityInstitutionMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAuthorityInstitution.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserAuthorityInstitution> userAuthorityInstitutionList = userAuthorityInstitutionRepository.findAll();
        assertThat(userAuthorityInstitutionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
