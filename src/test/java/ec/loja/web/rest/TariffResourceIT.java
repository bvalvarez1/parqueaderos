package ec.loja.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ec.loja.IntegrationTest;
import ec.loja.domain.Institution;
import ec.loja.domain.Tariff;
import ec.loja.repository.TariffRepository;
import ec.loja.service.dto.TariffDTO;
import ec.loja.service.mapper.TariffMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link TariffResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TariffResourceIT {

    private static final LocalDate DEFAULT_INITIAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INITIAL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FINAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FINAL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/tariffs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TariffRepository tariffRepository;

    @Autowired
    private TariffMapper tariffMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTariffMockMvc;

    private Tariff tariff;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tariff createEntity(EntityManager em) {
        Tariff tariff = new Tariff().initialDate(DEFAULT_INITIAL_DATE).finalDate(DEFAULT_FINAL_DATE);
        // Add required entity
        Institution institution;
        if (TestUtil.findAll(em, Institution.class).isEmpty()) {
            institution = InstitutionResourceIT.createEntity(em);
            em.persist(institution);
            em.flush();
        } else {
            institution = TestUtil.findAll(em, Institution.class).get(0);
        }
        tariff.setInstitution(institution);
        return tariff;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tariff createUpdatedEntity(EntityManager em) {
        Tariff tariff = new Tariff().initialDate(UPDATED_INITIAL_DATE).finalDate(UPDATED_FINAL_DATE);
        // Add required entity
        Institution institution;
        if (TestUtil.findAll(em, Institution.class).isEmpty()) {
            institution = InstitutionResourceIT.createUpdatedEntity(em);
            em.persist(institution);
            em.flush();
        } else {
            institution = TestUtil.findAll(em, Institution.class).get(0);
        }
        tariff.setInstitution(institution);
        return tariff;
    }

    @BeforeEach
    public void initTest() {
        tariff = createEntity(em);
    }

    @Test
    @Transactional
    void createTariff() throws Exception {
        int databaseSizeBeforeCreate = tariffRepository.findAll().size();
        // Create the Tariff
        TariffDTO tariffDTO = tariffMapper.toDto(tariff);
        restTariffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tariffDTO)))
            .andExpect(status().isCreated());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeCreate + 1);
        Tariff testTariff = tariffList.get(tariffList.size() - 1);
        assertThat(testTariff.getInitialDate()).isEqualTo(DEFAULT_INITIAL_DATE);
        assertThat(testTariff.getFinalDate()).isEqualTo(DEFAULT_FINAL_DATE);
    }

    @Test
    @Transactional
    void createTariffWithExistingId() throws Exception {
        // Create the Tariff with an existing ID
        tariff.setId(1L);
        TariffDTO tariffDTO = tariffMapper.toDto(tariff);

        int databaseSizeBeforeCreate = tariffRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTariffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tariffDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInitialDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tariffRepository.findAll().size();
        // set the field null
        tariff.setInitialDate(null);

        // Create the Tariff, which fails.
        TariffDTO tariffDTO = tariffMapper.toDto(tariff);

        restTariffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tariffDTO)))
            .andExpect(status().isBadRequest());

        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTariffs() throws Exception {
        // Initialize the database
        tariffRepository.saveAndFlush(tariff);

        // Get all the tariffList
        restTariffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tariff.getId().intValue())))
            .andExpect(jsonPath("$.[*].initialDate").value(hasItem(DEFAULT_INITIAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].finalDate").value(hasItem(DEFAULT_FINAL_DATE.toString())));
    }

    @Test
    @Transactional
    void getTariff() throws Exception {
        // Initialize the database
        tariffRepository.saveAndFlush(tariff);

        // Get the tariff
        restTariffMockMvc
            .perform(get(ENTITY_API_URL_ID, tariff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tariff.getId().intValue()))
            .andExpect(jsonPath("$.initialDate").value(DEFAULT_INITIAL_DATE.toString()))
            .andExpect(jsonPath("$.finalDate").value(DEFAULT_FINAL_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTariff() throws Exception {
        // Get the tariff
        restTariffMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTariff() throws Exception {
        // Initialize the database
        tariffRepository.saveAndFlush(tariff);

        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();

        // Update the tariff
        Tariff updatedTariff = tariffRepository.findById(tariff.getId()).get();
        // Disconnect from session so that the updates on updatedTariff are not directly saved in db
        em.detach(updatedTariff);
        updatedTariff.initialDate(UPDATED_INITIAL_DATE).finalDate(UPDATED_FINAL_DATE);
        TariffDTO tariffDTO = tariffMapper.toDto(updatedTariff);

        restTariffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tariffDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tariffDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
        Tariff testTariff = tariffList.get(tariffList.size() - 1);
        assertThat(testTariff.getInitialDate()).isEqualTo(UPDATED_INITIAL_DATE);
        assertThat(testTariff.getFinalDate()).isEqualTo(UPDATED_FINAL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTariff() throws Exception {
        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();
        tariff.setId(count.incrementAndGet());

        // Create the Tariff
        TariffDTO tariffDTO = tariffMapper.toDto(tariff);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTariffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tariffDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tariffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTariff() throws Exception {
        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();
        tariff.setId(count.incrementAndGet());

        // Create the Tariff
        TariffDTO tariffDTO = tariffMapper.toDto(tariff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTariffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tariffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTariff() throws Exception {
        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();
        tariff.setId(count.incrementAndGet());

        // Create the Tariff
        TariffDTO tariffDTO = tariffMapper.toDto(tariff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTariffMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tariffDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTariffWithPatch() throws Exception {
        // Initialize the database
        tariffRepository.saveAndFlush(tariff);

        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();

        // Update the tariff using partial update
        Tariff partialUpdatedTariff = new Tariff();
        partialUpdatedTariff.setId(tariff.getId());

        restTariffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTariff.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTariff))
            )
            .andExpect(status().isOk());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
        Tariff testTariff = tariffList.get(tariffList.size() - 1);
        assertThat(testTariff.getInitialDate()).isEqualTo(DEFAULT_INITIAL_DATE);
        assertThat(testTariff.getFinalDate()).isEqualTo(DEFAULT_FINAL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTariffWithPatch() throws Exception {
        // Initialize the database
        tariffRepository.saveAndFlush(tariff);

        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();

        // Update the tariff using partial update
        Tariff partialUpdatedTariff = new Tariff();
        partialUpdatedTariff.setId(tariff.getId());

        partialUpdatedTariff.initialDate(UPDATED_INITIAL_DATE).finalDate(UPDATED_FINAL_DATE);

        restTariffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTariff.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTariff))
            )
            .andExpect(status().isOk());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
        Tariff testTariff = tariffList.get(tariffList.size() - 1);
        assertThat(testTariff.getInitialDate()).isEqualTo(UPDATED_INITIAL_DATE);
        assertThat(testTariff.getFinalDate()).isEqualTo(UPDATED_FINAL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTariff() throws Exception {
        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();
        tariff.setId(count.incrementAndGet());

        // Create the Tariff
        TariffDTO tariffDTO = tariffMapper.toDto(tariff);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTariffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tariffDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tariffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTariff() throws Exception {
        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();
        tariff.setId(count.incrementAndGet());

        // Create the Tariff
        TariffDTO tariffDTO = tariffMapper.toDto(tariff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTariffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tariffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTariff() throws Exception {
        int databaseSizeBeforeUpdate = tariffRepository.findAll().size();
        tariff.setId(count.incrementAndGet());

        // Create the Tariff
        TariffDTO tariffDTO = tariffMapper.toDto(tariff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTariffMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tariffDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tariff in the database
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTariff() throws Exception {
        // Initialize the database
        tariffRepository.saveAndFlush(tariff);

        int databaseSizeBeforeDelete = tariffRepository.findAll().size();

        // Delete the tariff
        restTariffMockMvc
            .perform(delete(ENTITY_API_URL_ID, tariff.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tariff> tariffList = tariffRepository.findAll();
        assertThat(tariffList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
