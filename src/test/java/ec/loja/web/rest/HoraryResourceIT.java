package ec.loja.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ec.loja.IntegrationTest;
import ec.loja.domain.Horary;
import ec.loja.domain.ItemCatalogue;
import ec.loja.repository.HoraryRepository;
import ec.loja.service.dto.HoraryDTO;
import ec.loja.service.mapper.HoraryMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link HoraryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HoraryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FINAL_HOUR = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FINAL_HOUR = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/horaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HoraryRepository horaryRepository;

    @Autowired
    private HoraryMapper horaryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHoraryMockMvc;

    private Horary horary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Horary createEntity(EntityManager em) {
        Horary horary = new Horary().name(DEFAULT_NAME).startTime(DEFAULT_START_TIME).finalHour(DEFAULT_FINAL_HOUR);
        // Add required entity
        ItemCatalogue itemCatalogue;
        if (TestUtil.findAll(em, ItemCatalogue.class).isEmpty()) {
            itemCatalogue = ItemCatalogueResourceIT.createEntity(em);
            em.persist(itemCatalogue);
            em.flush();
        } else {
            itemCatalogue = TestUtil.findAll(em, ItemCatalogue.class).get(0);
        }
        horary.setStatus(itemCatalogue);
        return horary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Horary createUpdatedEntity(EntityManager em) {
        Horary horary = new Horary().name(UPDATED_NAME).startTime(UPDATED_START_TIME).finalHour(UPDATED_FINAL_HOUR);
        // Add required entity
        ItemCatalogue itemCatalogue;
        if (TestUtil.findAll(em, ItemCatalogue.class).isEmpty()) {
            itemCatalogue = ItemCatalogueResourceIT.createUpdatedEntity(em);
            em.persist(itemCatalogue);
            em.flush();
        } else {
            itemCatalogue = TestUtil.findAll(em, ItemCatalogue.class).get(0);
        }
        horary.setStatus(itemCatalogue);
        return horary;
    }

    @BeforeEach
    public void initTest() {
        horary = createEntity(em);
    }

    @Test
    @Transactional
    void createHorary() throws Exception {
        int databaseSizeBeforeCreate = horaryRepository.findAll().size();
        // Create the Horary
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);
        restHoraryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horaryDTO)))
            .andExpect(status().isCreated());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeCreate + 1);
        Horary testHorary = horaryList.get(horaryList.size() - 1);
        assertThat(testHorary.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHorary.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testHorary.getFinalHour()).isEqualTo(DEFAULT_FINAL_HOUR);
    }

    @Test
    @Transactional
    void createHoraryWithExistingId() throws Exception {
        // Create the Horary with an existing ID
        horary.setId(1L);
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        int databaseSizeBeforeCreate = horaryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHoraryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = horaryRepository.findAll().size();
        // set the field null
        horary.setName(null);

        // Create the Horary, which fails.
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        restHoraryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horaryDTO)))
            .andExpect(status().isBadRequest());

        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = horaryRepository.findAll().size();
        // set the field null
        horary.setStartTime(null);

        // Create the Horary, which fails.
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        restHoraryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horaryDTO)))
            .andExpect(status().isBadRequest());

        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFinalHourIsRequired() throws Exception {
        int databaseSizeBeforeTest = horaryRepository.findAll().size();
        // set the field null
        horary.setFinalHour(null);

        // Create the Horary, which fails.
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        restHoraryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horaryDTO)))
            .andExpect(status().isBadRequest());

        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHoraries() throws Exception {
        // Initialize the database
        horaryRepository.saveAndFlush(horary);

        // Get all the horaryList
        restHoraryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(horary.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].finalHour").value(hasItem(DEFAULT_FINAL_HOUR.toString())));
    }

    @Test
    @Transactional
    void getHorary() throws Exception {
        // Initialize the database
        horaryRepository.saveAndFlush(horary);

        // Get the horary
        restHoraryMockMvc
            .perform(get(ENTITY_API_URL_ID, horary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(horary.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.finalHour").value(DEFAULT_FINAL_HOUR.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHorary() throws Exception {
        // Get the horary
        restHoraryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHorary() throws Exception {
        // Initialize the database
        horaryRepository.saveAndFlush(horary);

        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();

        // Update the horary
        Horary updatedHorary = horaryRepository.findById(horary.getId()).get();
        // Disconnect from session so that the updates on updatedHorary are not directly saved in db
        em.detach(updatedHorary);
        updatedHorary.name(UPDATED_NAME).startTime(UPDATED_START_TIME).finalHour(UPDATED_FINAL_HOUR);
        HoraryDTO horaryDTO = horaryMapper.toDto(updatedHorary);

        restHoraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, horaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(horaryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
        Horary testHorary = horaryList.get(horaryList.size() - 1);
        assertThat(testHorary.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHorary.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testHorary.getFinalHour()).isEqualTo(UPDATED_FINAL_HOUR);
    }

    @Test
    @Transactional
    void putNonExistingHorary() throws Exception {
        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();
        horary.setId(count.incrementAndGet());

        // Create the Horary
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, horaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(horaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHorary() throws Exception {
        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();
        horary.setId(count.incrementAndGet());

        // Create the Horary
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(horaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHorary() throws Exception {
        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();
        horary.setId(count.incrementAndGet());

        // Create the Horary
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoraryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(horaryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHoraryWithPatch() throws Exception {
        // Initialize the database
        horaryRepository.saveAndFlush(horary);

        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();

        // Update the horary using partial update
        Horary partialUpdatedHorary = new Horary();
        partialUpdatedHorary.setId(horary.getId());

        partialUpdatedHorary.startTime(UPDATED_START_TIME);

        restHoraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHorary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHorary))
            )
            .andExpect(status().isOk());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
        Horary testHorary = horaryList.get(horaryList.size() - 1);
        assertThat(testHorary.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHorary.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testHorary.getFinalHour()).isEqualTo(DEFAULT_FINAL_HOUR);
    }

    @Test
    @Transactional
    void fullUpdateHoraryWithPatch() throws Exception {
        // Initialize the database
        horaryRepository.saveAndFlush(horary);

        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();

        // Update the horary using partial update
        Horary partialUpdatedHorary = new Horary();
        partialUpdatedHorary.setId(horary.getId());

        partialUpdatedHorary.name(UPDATED_NAME).startTime(UPDATED_START_TIME).finalHour(UPDATED_FINAL_HOUR);

        restHoraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHorary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHorary))
            )
            .andExpect(status().isOk());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
        Horary testHorary = horaryList.get(horaryList.size() - 1);
        assertThat(testHorary.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHorary.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testHorary.getFinalHour()).isEqualTo(UPDATED_FINAL_HOUR);
    }

    @Test
    @Transactional
    void patchNonExistingHorary() throws Exception {
        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();
        horary.setId(count.incrementAndGet());

        // Create the Horary
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, horaryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(horaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHorary() throws Exception {
        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();
        horary.setId(count.incrementAndGet());

        // Create the Horary
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(horaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHorary() throws Exception {
        int databaseSizeBeforeUpdate = horaryRepository.findAll().size();
        horary.setId(count.incrementAndGet());

        // Create the Horary
        HoraryDTO horaryDTO = horaryMapper.toDto(horary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoraryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(horaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Horary in the database
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHorary() throws Exception {
        // Initialize the database
        horaryRepository.saveAndFlush(horary);

        int databaseSizeBeforeDelete = horaryRepository.findAll().size();

        // Delete the horary
        restHoraryMockMvc
            .perform(delete(ENTITY_API_URL_ID, horary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Horary> horaryList = horaryRepository.findAll();
        assertThat(horaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
