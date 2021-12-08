package ec.loja.web.rest;

import static ec.loja.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ec.loja.IntegrationTest;
import ec.loja.domain.Institution;
import ec.loja.domain.ItemCatalogue;
import ec.loja.domain.RecordTicket;
import ec.loja.domain.TariffVehicleType;
import ec.loja.repository.RecordTicketRepository;
import ec.loja.service.dto.RecordTicketDTO;
import ec.loja.service.mapper.RecordTicketMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link RecordTicketResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecordTicketResourceIT {

    private static final Instant DEFAULT_INIT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INIT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PLATE = "AAAAAAAAAA";
    private static final String UPDATED_PLATE = "BBBBBBBBBB";

    private static final Instant DEFAULT_PARKING_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PARKING_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TAXABLE_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAXABLE_TOTAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TAXES = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAXES = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL = new BigDecimal(2);

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final String DEFAULT_SEQUENTIAL = "AAAAAAAAAA";
    private static final String UPDATED_SEQUENTIAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/record-tickets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RecordTicketRepository recordTicketRepository;

    @Autowired
    private RecordTicketMapper recordTicketMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecordTicketMockMvc;

    private RecordTicket recordTicket;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecordTicket createEntity(EntityManager em) {
        RecordTicket recordTicket = new RecordTicket()
            .initDate(DEFAULT_INIT_DATE)
            .endDate(DEFAULT_END_DATE)
            .plate(DEFAULT_PLATE)
            .parkingTime(DEFAULT_PARKING_TIME)
            .taxableTotal(DEFAULT_TAXABLE_TOTAL)
            .taxes(DEFAULT_TAXES)
            .total(DEFAULT_TOTAL)
            .observation(DEFAULT_OBSERVATION)
            .sequential(DEFAULT_SEQUENTIAL);
        // Add required entity
        ItemCatalogue itemCatalogue;
        if (TestUtil.findAll(em, ItemCatalogue.class).isEmpty()) {
            itemCatalogue = ItemCatalogueResourceIT.createEntity(em);
            em.persist(itemCatalogue);
            em.flush();
        } else {
            itemCatalogue = TestUtil.findAll(em, ItemCatalogue.class).get(0);
        }
        recordTicket.setStatus(itemCatalogue);
        // Add required entity
        TariffVehicleType tariffVehicleType;
        if (TestUtil.findAll(em, TariffVehicleType.class).isEmpty()) {
            tariffVehicleType = TariffVehicleTypeResourceIT.createEntity(em);
            em.persist(tariffVehicleType);
            em.flush();
        } else {
            tariffVehicleType = TestUtil.findAll(em, TariffVehicleType.class).get(0);
        }
        recordTicket.setTariffVehicle(tariffVehicleType);
        // Add required entity
        Institution institution;
        if (TestUtil.findAll(em, Institution.class).isEmpty()) {
            institution = InstitutionResourceIT.createEntity(em);
            em.persist(institution);
            em.flush();
        } else {
            institution = TestUtil.findAll(em, Institution.class).get(0);
        }
        recordTicket.setInstitution(institution);
        return recordTicket;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecordTicket createUpdatedEntity(EntityManager em) {
        RecordTicket recordTicket = new RecordTicket()
            .initDate(UPDATED_INIT_DATE)
            .endDate(UPDATED_END_DATE)
            .plate(UPDATED_PLATE)
            .parkingTime(UPDATED_PARKING_TIME)
            .taxableTotal(UPDATED_TAXABLE_TOTAL)
            .taxes(UPDATED_TAXES)
            .total(UPDATED_TOTAL)
            .observation(UPDATED_OBSERVATION)
            .sequential(UPDATED_SEQUENTIAL);
        // Add required entity
        ItemCatalogue itemCatalogue;
        if (TestUtil.findAll(em, ItemCatalogue.class).isEmpty()) {
            itemCatalogue = ItemCatalogueResourceIT.createUpdatedEntity(em);
            em.persist(itemCatalogue);
            em.flush();
        } else {
            itemCatalogue = TestUtil.findAll(em, ItemCatalogue.class).get(0);
        }
        recordTicket.setStatus(itemCatalogue);
        // Add required entity
        TariffVehicleType tariffVehicleType;
        if (TestUtil.findAll(em, TariffVehicleType.class).isEmpty()) {
            tariffVehicleType = TariffVehicleTypeResourceIT.createUpdatedEntity(em);
            em.persist(tariffVehicleType);
            em.flush();
        } else {
            tariffVehicleType = TestUtil.findAll(em, TariffVehicleType.class).get(0);
        }
        recordTicket.setTariffVehicle(tariffVehicleType);
        // Add required entity
        Institution institution;
        if (TestUtil.findAll(em, Institution.class).isEmpty()) {
            institution = InstitutionResourceIT.createUpdatedEntity(em);
            em.persist(institution);
            em.flush();
        } else {
            institution = TestUtil.findAll(em, Institution.class).get(0);
        }
        recordTicket.setInstitution(institution);
        return recordTicket;
    }

    @BeforeEach
    public void initTest() {
        recordTicket = createEntity(em);
    }

    @Test
    @Transactional
    void createRecordTicket() throws Exception {
        int databaseSizeBeforeCreate = recordTicketRepository.findAll().size();
        // Create the RecordTicket
        RecordTicketDTO recordTicketDTO = recordTicketMapper.toDto(recordTicket);
        restRecordTicketMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordTicketDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RecordTicket in the database
        List<RecordTicket> recordTicketList = recordTicketRepository.findAll();
        assertThat(recordTicketList).hasSize(databaseSizeBeforeCreate + 1);
        RecordTicket testRecordTicket = recordTicketList.get(recordTicketList.size() - 1);
        assertThat(testRecordTicket.getInitDate()).isEqualTo(DEFAULT_INIT_DATE);
        assertThat(testRecordTicket.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testRecordTicket.getPlate()).isEqualTo(DEFAULT_PLATE);
        assertThat(testRecordTicket.getParkingTime()).isEqualTo(DEFAULT_PARKING_TIME);
        assertThat(testRecordTicket.getTaxableTotal()).isEqualByComparingTo(DEFAULT_TAXABLE_TOTAL);
        assertThat(testRecordTicket.getTaxes()).isEqualByComparingTo(DEFAULT_TAXES);
        assertThat(testRecordTicket.getTotal()).isEqualByComparingTo(DEFAULT_TOTAL);
        assertThat(testRecordTicket.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
        assertThat(testRecordTicket.getSequential()).isEqualTo(DEFAULT_SEQUENTIAL);
    }

    @Test
    @Transactional
    void createRecordTicketWithExistingId() throws Exception {
        // Create the RecordTicket with an existing ID
        recordTicket.setId(1L);
        RecordTicketDTO recordTicketDTO = recordTicketMapper.toDto(recordTicket);

        int databaseSizeBeforeCreate = recordTicketRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecordTicketMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordTicketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecordTicket in the database
        List<RecordTicket> recordTicketList = recordTicketRepository.findAll();
        assertThat(recordTicketList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInitDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordTicketRepository.findAll().size();
        // set the field null
        recordTicket.setInitDate(null);

        // Create the RecordTicket, which fails.
        RecordTicketDTO recordTicketDTO = recordTicketMapper.toDto(recordTicket);

        restRecordTicketMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordTicketDTO))
            )
            .andExpect(status().isBadRequest());

        List<RecordTicket> recordTicketList = recordTicketRepository.findAll();
        assertThat(recordTicketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecordTickets() throws Exception {
        // Initialize the database
        recordTicketRepository.saveAndFlush(recordTicket);

        // Get all the recordTicketList
        restRecordTicketMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recordTicket.getId().intValue())))
            .andExpect(jsonPath("$.[*].initDate").value(hasItem(DEFAULT_INIT_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE)))
            .andExpect(jsonPath("$.[*].parkingTime").value(hasItem(DEFAULT_PARKING_TIME.toString())))
            .andExpect(jsonPath("$.[*].taxableTotal").value(hasItem(sameNumber(DEFAULT_TAXABLE_TOTAL))))
            .andExpect(jsonPath("$.[*].taxes").value(hasItem(sameNumber(DEFAULT_TAXES))))
            .andExpect(jsonPath("$.[*].total").value(hasItem(sameNumber(DEFAULT_TOTAL))))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)))
            .andExpect(jsonPath("$.[*].sequential").value(hasItem(DEFAULT_SEQUENTIAL)));
    }

    @Test
    @Transactional
    void getRecordTicket() throws Exception {
        // Initialize the database
        recordTicketRepository.saveAndFlush(recordTicket);

        // Get the recordTicket
        restRecordTicketMockMvc
            .perform(get(ENTITY_API_URL_ID, recordTicket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recordTicket.getId().intValue()))
            .andExpect(jsonPath("$.initDate").value(DEFAULT_INIT_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.plate").value(DEFAULT_PLATE))
            .andExpect(jsonPath("$.parkingTime").value(DEFAULT_PARKING_TIME.toString()))
            .andExpect(jsonPath("$.taxableTotal").value(sameNumber(DEFAULT_TAXABLE_TOTAL)))
            .andExpect(jsonPath("$.taxes").value(sameNumber(DEFAULT_TAXES)))
            .andExpect(jsonPath("$.total").value(sameNumber(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION))
            .andExpect(jsonPath("$.sequential").value(DEFAULT_SEQUENTIAL));
    }

    @Test
    @Transactional
    void getNonExistingRecordTicket() throws Exception {
        // Get the recordTicket
        restRecordTicketMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRecordTicket() throws Exception {
        // Initialize the database
        recordTicketRepository.saveAndFlush(recordTicket);

        int databaseSizeBeforeUpdate = recordTicketRepository.findAll().size();

        // Update the recordTicket
        RecordTicket updatedRecordTicket = recordTicketRepository.findById(recordTicket.getId()).get();
        // Disconnect from session so that the updates on updatedRecordTicket are not directly saved in db
        em.detach(updatedRecordTicket);
        updatedRecordTicket
            .initDate(UPDATED_INIT_DATE)
            .endDate(UPDATED_END_DATE)
            .plate(UPDATED_PLATE)
            .parkingTime(UPDATED_PARKING_TIME)
            .taxableTotal(UPDATED_TAXABLE_TOTAL)
            .taxes(UPDATED_TAXES)
            .total(UPDATED_TOTAL)
            .observation(UPDATED_OBSERVATION)
            .sequential(UPDATED_SEQUENTIAL);
        RecordTicketDTO recordTicketDTO = recordTicketMapper.toDto(updatedRecordTicket);

        restRecordTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recordTicketDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recordTicketDTO))
            )
            .andExpect(status().isOk());

        // Validate the RecordTicket in the database
        List<RecordTicket> recordTicketList = recordTicketRepository.findAll();
        assertThat(recordTicketList).hasSize(databaseSizeBeforeUpdate);
        RecordTicket testRecordTicket = recordTicketList.get(recordTicketList.size() - 1);
        assertThat(testRecordTicket.getInitDate()).isEqualTo(UPDATED_INIT_DATE);
        assertThat(testRecordTicket.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testRecordTicket.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testRecordTicket.getParkingTime()).isEqualTo(UPDATED_PARKING_TIME);
        assertThat(testRecordTicket.getTaxableTotal()).isEqualTo(UPDATED_TAXABLE_TOTAL);
        assertThat(testRecordTicket.getTaxes()).isEqualTo(UPDATED_TAXES);
        assertThat(testRecordTicket.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testRecordTicket.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testRecordTicket.getSequential()).isEqualTo(UPDATED_SEQUENTIAL);
    }

    @Test
    @Transactional
    void putNonExistingRecordTicket() throws Exception {
        int databaseSizeBeforeUpdate = recordTicketRepository.findAll().size();
        recordTicket.setId(count.incrementAndGet());

        // Create the RecordTicket
        RecordTicketDTO recordTicketDTO = recordTicketMapper.toDto(recordTicket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecordTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recordTicketDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recordTicketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecordTicket in the database
        List<RecordTicket> recordTicketList = recordTicketRepository.findAll();
        assertThat(recordTicketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecordTicket() throws Exception {
        int databaseSizeBeforeUpdate = recordTicketRepository.findAll().size();
        recordTicket.setId(count.incrementAndGet());

        // Create the RecordTicket
        RecordTicketDTO recordTicketDTO = recordTicketMapper.toDto(recordTicket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recordTicketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecordTicket in the database
        List<RecordTicket> recordTicketList = recordTicketRepository.findAll();
        assertThat(recordTicketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecordTicket() throws Exception {
        int databaseSizeBeforeUpdate = recordTicketRepository.findAll().size();
        recordTicket.setId(count.incrementAndGet());

        // Create the RecordTicket
        RecordTicketDTO recordTicketDTO = recordTicketMapper.toDto(recordTicket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordTicketMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordTicketDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecordTicket in the database
        List<RecordTicket> recordTicketList = recordTicketRepository.findAll();
        assertThat(recordTicketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecordTicketWithPatch() throws Exception {
        // Initialize the database
        recordTicketRepository.saveAndFlush(recordTicket);

        int databaseSizeBeforeUpdate = recordTicketRepository.findAll().size();

        // Update the recordTicket using partial update
        RecordTicket partialUpdatedRecordTicket = new RecordTicket();
        partialUpdatedRecordTicket.setId(recordTicket.getId());

        partialUpdatedRecordTicket
            .endDate(UPDATED_END_DATE)
            .parkingTime(UPDATED_PARKING_TIME)
            .taxableTotal(UPDATED_TAXABLE_TOTAL)
            .total(UPDATED_TOTAL)
            .sequential(UPDATED_SEQUENTIAL);

        restRecordTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecordTicket.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecordTicket))
            )
            .andExpect(status().isOk());

        // Validate the RecordTicket in the database
        List<RecordTicket> recordTicketList = recordTicketRepository.findAll();
        assertThat(recordTicketList).hasSize(databaseSizeBeforeUpdate);
        RecordTicket testRecordTicket = recordTicketList.get(recordTicketList.size() - 1);
        assertThat(testRecordTicket.getInitDate()).isEqualTo(DEFAULT_INIT_DATE);
        assertThat(testRecordTicket.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testRecordTicket.getPlate()).isEqualTo(DEFAULT_PLATE);
        assertThat(testRecordTicket.getParkingTime()).isEqualTo(UPDATED_PARKING_TIME);
        assertThat(testRecordTicket.getTaxableTotal()).isEqualByComparingTo(UPDATED_TAXABLE_TOTAL);
        assertThat(testRecordTicket.getTaxes()).isEqualByComparingTo(DEFAULT_TAXES);
        assertThat(testRecordTicket.getTotal()).isEqualByComparingTo(UPDATED_TOTAL);
        assertThat(testRecordTicket.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
        assertThat(testRecordTicket.getSequential()).isEqualTo(UPDATED_SEQUENTIAL);
    }

    @Test
    @Transactional
    void fullUpdateRecordTicketWithPatch() throws Exception {
        // Initialize the database
        recordTicketRepository.saveAndFlush(recordTicket);

        int databaseSizeBeforeUpdate = recordTicketRepository.findAll().size();

        // Update the recordTicket using partial update
        RecordTicket partialUpdatedRecordTicket = new RecordTicket();
        partialUpdatedRecordTicket.setId(recordTicket.getId());

        partialUpdatedRecordTicket
            .initDate(UPDATED_INIT_DATE)
            .endDate(UPDATED_END_DATE)
            .plate(UPDATED_PLATE)
            .parkingTime(UPDATED_PARKING_TIME)
            .taxableTotal(UPDATED_TAXABLE_TOTAL)
            .taxes(UPDATED_TAXES)
            .total(UPDATED_TOTAL)
            .observation(UPDATED_OBSERVATION)
            .sequential(UPDATED_SEQUENTIAL);

        restRecordTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecordTicket.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecordTicket))
            )
            .andExpect(status().isOk());

        // Validate the RecordTicket in the database
        List<RecordTicket> recordTicketList = recordTicketRepository.findAll();
        assertThat(recordTicketList).hasSize(databaseSizeBeforeUpdate);
        RecordTicket testRecordTicket = recordTicketList.get(recordTicketList.size() - 1);
        assertThat(testRecordTicket.getInitDate()).isEqualTo(UPDATED_INIT_DATE);
        assertThat(testRecordTicket.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testRecordTicket.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testRecordTicket.getParkingTime()).isEqualTo(UPDATED_PARKING_TIME);
        assertThat(testRecordTicket.getTaxableTotal()).isEqualByComparingTo(UPDATED_TAXABLE_TOTAL);
        assertThat(testRecordTicket.getTaxes()).isEqualByComparingTo(UPDATED_TAXES);
        assertThat(testRecordTicket.getTotal()).isEqualByComparingTo(UPDATED_TOTAL);
        assertThat(testRecordTicket.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testRecordTicket.getSequential()).isEqualTo(UPDATED_SEQUENTIAL);
    }

    @Test
    @Transactional
    void patchNonExistingRecordTicket() throws Exception {
        int databaseSizeBeforeUpdate = recordTicketRepository.findAll().size();
        recordTicket.setId(count.incrementAndGet());

        // Create the RecordTicket
        RecordTicketDTO recordTicketDTO = recordTicketMapper.toDto(recordTicket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecordTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recordTicketDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recordTicketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecordTicket in the database
        List<RecordTicket> recordTicketList = recordTicketRepository.findAll();
        assertThat(recordTicketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecordTicket() throws Exception {
        int databaseSizeBeforeUpdate = recordTicketRepository.findAll().size();
        recordTicket.setId(count.incrementAndGet());

        // Create the RecordTicket
        RecordTicketDTO recordTicketDTO = recordTicketMapper.toDto(recordTicket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recordTicketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecordTicket in the database
        List<RecordTicket> recordTicketList = recordTicketRepository.findAll();
        assertThat(recordTicketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecordTicket() throws Exception {
        int databaseSizeBeforeUpdate = recordTicketRepository.findAll().size();
        recordTicket.setId(count.incrementAndGet());

        // Create the RecordTicket
        RecordTicketDTO recordTicketDTO = recordTicketMapper.toDto(recordTicket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordTicketMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recordTicketDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecordTicket in the database
        List<RecordTicket> recordTicketList = recordTicketRepository.findAll();
        assertThat(recordTicketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecordTicket() throws Exception {
        // Initialize the database
        recordTicketRepository.saveAndFlush(recordTicket);

        int databaseSizeBeforeDelete = recordTicketRepository.findAll().size();

        // Delete the recordTicket
        restRecordTicketMockMvc
            .perform(delete(ENTITY_API_URL_ID, recordTicket.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RecordTicket> recordTicketList = recordTicketRepository.findAll();
        assertThat(recordTicketList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
