package ec.loja.web.rest;

import static ec.loja.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ec.loja.IntegrationTest;
import ec.loja.domain.ItemCatalogue;
import ec.loja.domain.TariffVehicleType;
import ec.loja.repository.TariffVehicleTypeRepository;
import ec.loja.service.dto.TariffVehicleTypeDTO;
import ec.loja.service.mapper.TariffVehicleTypeMapper;
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
 * Integration tests for the {@link TariffVehicleTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TariffVehicleTypeResourceIT {

    private static final Integer DEFAULT_MIN_VALUE = 1;
    private static final Integer UPDATED_MIN_VALUE = 2;

    private static final Integer DEFAULT_MAX_VALUE = 1;
    private static final Integer UPDATED_MAX_VALUE = 2;

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/tariff-vehicle-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TariffVehicleTypeRepository tariffVehicleTypeRepository;

    @Autowired
    private TariffVehicleTypeMapper tariffVehicleTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTariffVehicleTypeMockMvc;

    private TariffVehicleType tariffVehicleType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TariffVehicleType createEntity(EntityManager em) {
        TariffVehicleType tariffVehicleType = new TariffVehicleType()
            .minValue(DEFAULT_MIN_VALUE)
            .maxValue(DEFAULT_MAX_VALUE)
            .value(DEFAULT_VALUE);
        // Add required entity
        ItemCatalogue itemCatalogue;
        if (TestUtil.findAll(em, ItemCatalogue.class).isEmpty()) {
            itemCatalogue = ItemCatalogueResourceIT.createEntity(em);
            em.persist(itemCatalogue);
            em.flush();
        } else {
            itemCatalogue = TestUtil.findAll(em, ItemCatalogue.class).get(0);
        }
        tariffVehicleType.setVehicleType(itemCatalogue);
        return tariffVehicleType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TariffVehicleType createUpdatedEntity(EntityManager em) {
        TariffVehicleType tariffVehicleType = new TariffVehicleType()
            .minValue(UPDATED_MIN_VALUE)
            .maxValue(UPDATED_MAX_VALUE)
            .value(UPDATED_VALUE);
        // Add required entity
        ItemCatalogue itemCatalogue;
        if (TestUtil.findAll(em, ItemCatalogue.class).isEmpty()) {
            itemCatalogue = ItemCatalogueResourceIT.createUpdatedEntity(em);
            em.persist(itemCatalogue);
            em.flush();
        } else {
            itemCatalogue = TestUtil.findAll(em, ItemCatalogue.class).get(0);
        }
        tariffVehicleType.setVehicleType(itemCatalogue);
        return tariffVehicleType;
    }

    @BeforeEach
    public void initTest() {
        tariffVehicleType = createEntity(em);
    }

    @Test
    @Transactional
    void createTariffVehicleType() throws Exception {
        int databaseSizeBeforeCreate = tariffVehicleTypeRepository.findAll().size();
        // Create the TariffVehicleType
        TariffVehicleTypeDTO tariffVehicleTypeDTO = tariffVehicleTypeMapper.toDto(tariffVehicleType);
        restTariffVehicleTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tariffVehicleTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TariffVehicleType in the database
        List<TariffVehicleType> tariffVehicleTypeList = tariffVehicleTypeRepository.findAll();
        assertThat(tariffVehicleTypeList).hasSize(databaseSizeBeforeCreate + 1);
        TariffVehicleType testTariffVehicleType = tariffVehicleTypeList.get(tariffVehicleTypeList.size() - 1);
        assertThat(testTariffVehicleType.getMinValue()).isEqualTo(DEFAULT_MIN_VALUE);
        assertThat(testTariffVehicleType.getMaxValue()).isEqualTo(DEFAULT_MAX_VALUE);
        assertThat(testTariffVehicleType.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createTariffVehicleTypeWithExistingId() throws Exception {
        // Create the TariffVehicleType with an existing ID
        tariffVehicleType.setId(1L);
        TariffVehicleTypeDTO tariffVehicleTypeDTO = tariffVehicleTypeMapper.toDto(tariffVehicleType);

        int databaseSizeBeforeCreate = tariffVehicleTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTariffVehicleTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tariffVehicleTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TariffVehicleType in the database
        List<TariffVehicleType> tariffVehicleTypeList = tariffVehicleTypeRepository.findAll();
        assertThat(tariffVehicleTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMinValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = tariffVehicleTypeRepository.findAll().size();
        // set the field null
        tariffVehicleType.setMinValue(null);

        // Create the TariffVehicleType, which fails.
        TariffVehicleTypeDTO tariffVehicleTypeDTO = tariffVehicleTypeMapper.toDto(tariffVehicleType);

        restTariffVehicleTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tariffVehicleTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<TariffVehicleType> tariffVehicleTypeList = tariffVehicleTypeRepository.findAll();
        assertThat(tariffVehicleTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = tariffVehicleTypeRepository.findAll().size();
        // set the field null
        tariffVehicleType.setMaxValue(null);

        // Create the TariffVehicleType, which fails.
        TariffVehicleTypeDTO tariffVehicleTypeDTO = tariffVehicleTypeMapper.toDto(tariffVehicleType);

        restTariffVehicleTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tariffVehicleTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<TariffVehicleType> tariffVehicleTypeList = tariffVehicleTypeRepository.findAll();
        assertThat(tariffVehicleTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = tariffVehicleTypeRepository.findAll().size();
        // set the field null
        tariffVehicleType.setValue(null);

        // Create the TariffVehicleType, which fails.
        TariffVehicleTypeDTO tariffVehicleTypeDTO = tariffVehicleTypeMapper.toDto(tariffVehicleType);

        restTariffVehicleTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tariffVehicleTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<TariffVehicleType> tariffVehicleTypeList = tariffVehicleTypeRepository.findAll();
        assertThat(tariffVehicleTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTariffVehicleTypes() throws Exception {
        // Initialize the database
        tariffVehicleTypeRepository.saveAndFlush(tariffVehicleType);

        // Get all the tariffVehicleTypeList
        restTariffVehicleTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tariffVehicleType.getId().intValue())))
            .andExpect(jsonPath("$.[*].minValue").value(hasItem(DEFAULT_MIN_VALUE)))
            .andExpect(jsonPath("$.[*].maxValue").value(hasItem(DEFAULT_MAX_VALUE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))));
    }

    @Test
    @Transactional
    void getTariffVehicleType() throws Exception {
        // Initialize the database
        tariffVehicleTypeRepository.saveAndFlush(tariffVehicleType);

        // Get the tariffVehicleType
        restTariffVehicleTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, tariffVehicleType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tariffVehicleType.getId().intValue()))
            .andExpect(jsonPath("$.minValue").value(DEFAULT_MIN_VALUE))
            .andExpect(jsonPath("$.maxValue").value(DEFAULT_MAX_VALUE))
            .andExpect(jsonPath("$.value").value(sameNumber(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getNonExistingTariffVehicleType() throws Exception {
        // Get the tariffVehicleType
        restTariffVehicleTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTariffVehicleType() throws Exception {
        // Initialize the database
        tariffVehicleTypeRepository.saveAndFlush(tariffVehicleType);

        int databaseSizeBeforeUpdate = tariffVehicleTypeRepository.findAll().size();

        // Update the tariffVehicleType
        TariffVehicleType updatedTariffVehicleType = tariffVehicleTypeRepository.findById(tariffVehicleType.getId()).get();
        // Disconnect from session so that the updates on updatedTariffVehicleType are not directly saved in db
        em.detach(updatedTariffVehicleType);
        updatedTariffVehicleType.minValue(UPDATED_MIN_VALUE).maxValue(UPDATED_MAX_VALUE).value(UPDATED_VALUE);
        TariffVehicleTypeDTO tariffVehicleTypeDTO = tariffVehicleTypeMapper.toDto(updatedTariffVehicleType);

        restTariffVehicleTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tariffVehicleTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tariffVehicleTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the TariffVehicleType in the database
        List<TariffVehicleType> tariffVehicleTypeList = tariffVehicleTypeRepository.findAll();
        assertThat(tariffVehicleTypeList).hasSize(databaseSizeBeforeUpdate);
        TariffVehicleType testTariffVehicleType = tariffVehicleTypeList.get(tariffVehicleTypeList.size() - 1);
        assertThat(testTariffVehicleType.getMinValue()).isEqualTo(UPDATED_MIN_VALUE);
        assertThat(testTariffVehicleType.getMaxValue()).isEqualTo(UPDATED_MAX_VALUE);
        assertThat(testTariffVehicleType.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingTariffVehicleType() throws Exception {
        int databaseSizeBeforeUpdate = tariffVehicleTypeRepository.findAll().size();
        tariffVehicleType.setId(count.incrementAndGet());

        // Create the TariffVehicleType
        TariffVehicleTypeDTO tariffVehicleTypeDTO = tariffVehicleTypeMapper.toDto(tariffVehicleType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTariffVehicleTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tariffVehicleTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tariffVehicleTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TariffVehicleType in the database
        List<TariffVehicleType> tariffVehicleTypeList = tariffVehicleTypeRepository.findAll();
        assertThat(tariffVehicleTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTariffVehicleType() throws Exception {
        int databaseSizeBeforeUpdate = tariffVehicleTypeRepository.findAll().size();
        tariffVehicleType.setId(count.incrementAndGet());

        // Create the TariffVehicleType
        TariffVehicleTypeDTO tariffVehicleTypeDTO = tariffVehicleTypeMapper.toDto(tariffVehicleType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTariffVehicleTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tariffVehicleTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TariffVehicleType in the database
        List<TariffVehicleType> tariffVehicleTypeList = tariffVehicleTypeRepository.findAll();
        assertThat(tariffVehicleTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTariffVehicleType() throws Exception {
        int databaseSizeBeforeUpdate = tariffVehicleTypeRepository.findAll().size();
        tariffVehicleType.setId(count.incrementAndGet());

        // Create the TariffVehicleType
        TariffVehicleTypeDTO tariffVehicleTypeDTO = tariffVehicleTypeMapper.toDto(tariffVehicleType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTariffVehicleTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tariffVehicleTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TariffVehicleType in the database
        List<TariffVehicleType> tariffVehicleTypeList = tariffVehicleTypeRepository.findAll();
        assertThat(tariffVehicleTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTariffVehicleTypeWithPatch() throws Exception {
        // Initialize the database
        tariffVehicleTypeRepository.saveAndFlush(tariffVehicleType);

        int databaseSizeBeforeUpdate = tariffVehicleTypeRepository.findAll().size();

        // Update the tariffVehicleType using partial update
        TariffVehicleType partialUpdatedTariffVehicleType = new TariffVehicleType();
        partialUpdatedTariffVehicleType.setId(tariffVehicleType.getId());

        restTariffVehicleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTariffVehicleType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTariffVehicleType))
            )
            .andExpect(status().isOk());

        // Validate the TariffVehicleType in the database
        List<TariffVehicleType> tariffVehicleTypeList = tariffVehicleTypeRepository.findAll();
        assertThat(tariffVehicleTypeList).hasSize(databaseSizeBeforeUpdate);
        TariffVehicleType testTariffVehicleType = tariffVehicleTypeList.get(tariffVehicleTypeList.size() - 1);
        assertThat(testTariffVehicleType.getMinValue()).isEqualTo(DEFAULT_MIN_VALUE);
        assertThat(testTariffVehicleType.getMaxValue()).isEqualTo(DEFAULT_MAX_VALUE);
        assertThat(testTariffVehicleType.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateTariffVehicleTypeWithPatch() throws Exception {
        // Initialize the database
        tariffVehicleTypeRepository.saveAndFlush(tariffVehicleType);

        int databaseSizeBeforeUpdate = tariffVehicleTypeRepository.findAll().size();

        // Update the tariffVehicleType using partial update
        TariffVehicleType partialUpdatedTariffVehicleType = new TariffVehicleType();
        partialUpdatedTariffVehicleType.setId(tariffVehicleType.getId());

        partialUpdatedTariffVehicleType.minValue(UPDATED_MIN_VALUE).maxValue(UPDATED_MAX_VALUE).value(UPDATED_VALUE);

        restTariffVehicleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTariffVehicleType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTariffVehicleType))
            )
            .andExpect(status().isOk());

        // Validate the TariffVehicleType in the database
        List<TariffVehicleType> tariffVehicleTypeList = tariffVehicleTypeRepository.findAll();
        assertThat(tariffVehicleTypeList).hasSize(databaseSizeBeforeUpdate);
        TariffVehicleType testTariffVehicleType = tariffVehicleTypeList.get(tariffVehicleTypeList.size() - 1);
        assertThat(testTariffVehicleType.getMinValue()).isEqualTo(UPDATED_MIN_VALUE);
        assertThat(testTariffVehicleType.getMaxValue()).isEqualTo(UPDATED_MAX_VALUE);
        assertThat(testTariffVehicleType.getValue()).isEqualByComparingTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingTariffVehicleType() throws Exception {
        int databaseSizeBeforeUpdate = tariffVehicleTypeRepository.findAll().size();
        tariffVehicleType.setId(count.incrementAndGet());

        // Create the TariffVehicleType
        TariffVehicleTypeDTO tariffVehicleTypeDTO = tariffVehicleTypeMapper.toDto(tariffVehicleType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTariffVehicleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tariffVehicleTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tariffVehicleTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TariffVehicleType in the database
        List<TariffVehicleType> tariffVehicleTypeList = tariffVehicleTypeRepository.findAll();
        assertThat(tariffVehicleTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTariffVehicleType() throws Exception {
        int databaseSizeBeforeUpdate = tariffVehicleTypeRepository.findAll().size();
        tariffVehicleType.setId(count.incrementAndGet());

        // Create the TariffVehicleType
        TariffVehicleTypeDTO tariffVehicleTypeDTO = tariffVehicleTypeMapper.toDto(tariffVehicleType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTariffVehicleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tariffVehicleTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TariffVehicleType in the database
        List<TariffVehicleType> tariffVehicleTypeList = tariffVehicleTypeRepository.findAll();
        assertThat(tariffVehicleTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTariffVehicleType() throws Exception {
        int databaseSizeBeforeUpdate = tariffVehicleTypeRepository.findAll().size();
        tariffVehicleType.setId(count.incrementAndGet());

        // Create the TariffVehicleType
        TariffVehicleTypeDTO tariffVehicleTypeDTO = tariffVehicleTypeMapper.toDto(tariffVehicleType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTariffVehicleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tariffVehicleTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TariffVehicleType in the database
        List<TariffVehicleType> tariffVehicleTypeList = tariffVehicleTypeRepository.findAll();
        assertThat(tariffVehicleTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTariffVehicleType() throws Exception {
        // Initialize the database
        tariffVehicleTypeRepository.saveAndFlush(tariffVehicleType);

        int databaseSizeBeforeDelete = tariffVehicleTypeRepository.findAll().size();

        // Delete the tariffVehicleType
        restTariffVehicleTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, tariffVehicleType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TariffVehicleType> tariffVehicleTypeList = tariffVehicleTypeRepository.findAll();
        assertThat(tariffVehicleTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
