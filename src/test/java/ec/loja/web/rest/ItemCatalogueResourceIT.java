package ec.loja.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ec.loja.IntegrationTest;
import ec.loja.domain.ItemCatalogue;
import ec.loja.repository.ItemCatalogueRepository;
import ec.loja.service.dto.ItemCatalogueDTO;
import ec.loja.service.mapper.ItemCatalogueMapper;
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
 * Integration tests for the {@link ItemCatalogueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemCatalogueResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CATALOG_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CATALOG_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/item-catalogues";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemCatalogueRepository itemCatalogueRepository;

    @Autowired
    private ItemCatalogueMapper itemCatalogueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemCatalogueMockMvc;

    private ItemCatalogue itemCatalogue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemCatalogue createEntity(EntityManager em) {
        ItemCatalogue itemCatalogue = new ItemCatalogue()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .catalogCode(DEFAULT_CATALOG_CODE)
            .active(DEFAULT_ACTIVE);
        return itemCatalogue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemCatalogue createUpdatedEntity(EntityManager em) {
        ItemCatalogue itemCatalogue = new ItemCatalogue()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .catalogCode(UPDATED_CATALOG_CODE)
            .active(UPDATED_ACTIVE);
        return itemCatalogue;
    }

    @BeforeEach
    public void initTest() {
        itemCatalogue = createEntity(em);
    }

    @Test
    @Transactional
    void createItemCatalogue() throws Exception {
        int databaseSizeBeforeCreate = itemCatalogueRepository.findAll().size();
        // Create the ItemCatalogue
        ItemCatalogueDTO itemCatalogueDTO = itemCatalogueMapper.toDto(itemCatalogue);
        restItemCatalogueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemCatalogueDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ItemCatalogue in the database
        List<ItemCatalogue> itemCatalogueList = itemCatalogueRepository.findAll();
        assertThat(itemCatalogueList).hasSize(databaseSizeBeforeCreate + 1);
        ItemCatalogue testItemCatalogue = itemCatalogueList.get(itemCatalogueList.size() - 1);
        assertThat(testItemCatalogue.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItemCatalogue.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testItemCatalogue.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testItemCatalogue.getCatalogCode()).isEqualTo(DEFAULT_CATALOG_CODE);
        assertThat(testItemCatalogue.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createItemCatalogueWithExistingId() throws Exception {
        // Create the ItemCatalogue with an existing ID
        itemCatalogue.setId(1L);
        ItemCatalogueDTO itemCatalogueDTO = itemCatalogueMapper.toDto(itemCatalogue);

        int databaseSizeBeforeCreate = itemCatalogueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemCatalogueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemCatalogueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemCatalogue in the database
        List<ItemCatalogue> itemCatalogueList = itemCatalogueRepository.findAll();
        assertThat(itemCatalogueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemCatalogueRepository.findAll().size();
        // set the field null
        itemCatalogue.setName(null);

        // Create the ItemCatalogue, which fails.
        ItemCatalogueDTO itemCatalogueDTO = itemCatalogueMapper.toDto(itemCatalogue);

        restItemCatalogueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemCatalogueDTO))
            )
            .andExpect(status().isBadRequest());

        List<ItemCatalogue> itemCatalogueList = itemCatalogueRepository.findAll();
        assertThat(itemCatalogueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemCatalogueRepository.findAll().size();
        // set the field null
        itemCatalogue.setCode(null);

        // Create the ItemCatalogue, which fails.
        ItemCatalogueDTO itemCatalogueDTO = itemCatalogueMapper.toDto(itemCatalogue);

        restItemCatalogueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemCatalogueDTO))
            )
            .andExpect(status().isBadRequest());

        List<ItemCatalogue> itemCatalogueList = itemCatalogueRepository.findAll();
        assertThat(itemCatalogueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCatalogCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemCatalogueRepository.findAll().size();
        // set the field null
        itemCatalogue.setCatalogCode(null);

        // Create the ItemCatalogue, which fails.
        ItemCatalogueDTO itemCatalogueDTO = itemCatalogueMapper.toDto(itemCatalogue);

        restItemCatalogueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemCatalogueDTO))
            )
            .andExpect(status().isBadRequest());

        List<ItemCatalogue> itemCatalogueList = itemCatalogueRepository.findAll();
        assertThat(itemCatalogueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItemCatalogues() throws Exception {
        // Initialize the database
        itemCatalogueRepository.saveAndFlush(itemCatalogue);

        // Get all the itemCatalogueList
        restItemCatalogueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemCatalogue.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].catalogCode").value(hasItem(DEFAULT_CATALOG_CODE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getItemCatalogue() throws Exception {
        // Initialize the database
        itemCatalogueRepository.saveAndFlush(itemCatalogue);

        // Get the itemCatalogue
        restItemCatalogueMockMvc
            .perform(get(ENTITY_API_URL_ID, itemCatalogue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemCatalogue.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.catalogCode").value(DEFAULT_CATALOG_CODE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingItemCatalogue() throws Exception {
        // Get the itemCatalogue
        restItemCatalogueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItemCatalogue() throws Exception {
        // Initialize the database
        itemCatalogueRepository.saveAndFlush(itemCatalogue);

        int databaseSizeBeforeUpdate = itemCatalogueRepository.findAll().size();

        // Update the itemCatalogue
        ItemCatalogue updatedItemCatalogue = itemCatalogueRepository.findById(itemCatalogue.getId()).get();
        // Disconnect from session so that the updates on updatedItemCatalogue are not directly saved in db
        em.detach(updatedItemCatalogue);
        updatedItemCatalogue
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .catalogCode(UPDATED_CATALOG_CODE)
            .active(UPDATED_ACTIVE);
        ItemCatalogueDTO itemCatalogueDTO = itemCatalogueMapper.toDto(updatedItemCatalogue);

        restItemCatalogueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemCatalogueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemCatalogueDTO))
            )
            .andExpect(status().isOk());

        // Validate the ItemCatalogue in the database
        List<ItemCatalogue> itemCatalogueList = itemCatalogueRepository.findAll();
        assertThat(itemCatalogueList).hasSize(databaseSizeBeforeUpdate);
        ItemCatalogue testItemCatalogue = itemCatalogueList.get(itemCatalogueList.size() - 1);
        assertThat(testItemCatalogue.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemCatalogue.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testItemCatalogue.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItemCatalogue.getCatalogCode()).isEqualTo(UPDATED_CATALOG_CODE);
        assertThat(testItemCatalogue.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingItemCatalogue() throws Exception {
        int databaseSizeBeforeUpdate = itemCatalogueRepository.findAll().size();
        itemCatalogue.setId(count.incrementAndGet());

        // Create the ItemCatalogue
        ItemCatalogueDTO itemCatalogueDTO = itemCatalogueMapper.toDto(itemCatalogue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemCatalogueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemCatalogueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemCatalogueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemCatalogue in the database
        List<ItemCatalogue> itemCatalogueList = itemCatalogueRepository.findAll();
        assertThat(itemCatalogueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemCatalogue() throws Exception {
        int databaseSizeBeforeUpdate = itemCatalogueRepository.findAll().size();
        itemCatalogue.setId(count.incrementAndGet());

        // Create the ItemCatalogue
        ItemCatalogueDTO itemCatalogueDTO = itemCatalogueMapper.toDto(itemCatalogue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemCatalogueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemCatalogueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemCatalogue in the database
        List<ItemCatalogue> itemCatalogueList = itemCatalogueRepository.findAll();
        assertThat(itemCatalogueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemCatalogue() throws Exception {
        int databaseSizeBeforeUpdate = itemCatalogueRepository.findAll().size();
        itemCatalogue.setId(count.incrementAndGet());

        // Create the ItemCatalogue
        ItemCatalogueDTO itemCatalogueDTO = itemCatalogueMapper.toDto(itemCatalogue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemCatalogueMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemCatalogueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemCatalogue in the database
        List<ItemCatalogue> itemCatalogueList = itemCatalogueRepository.findAll();
        assertThat(itemCatalogueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemCatalogueWithPatch() throws Exception {
        // Initialize the database
        itemCatalogueRepository.saveAndFlush(itemCatalogue);

        int databaseSizeBeforeUpdate = itemCatalogueRepository.findAll().size();

        // Update the itemCatalogue using partial update
        ItemCatalogue partialUpdatedItemCatalogue = new ItemCatalogue();
        partialUpdatedItemCatalogue.setId(itemCatalogue.getId());

        partialUpdatedItemCatalogue
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .catalogCode(UPDATED_CATALOG_CODE)
            .active(UPDATED_ACTIVE);

        restItemCatalogueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemCatalogue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemCatalogue))
            )
            .andExpect(status().isOk());

        // Validate the ItemCatalogue in the database
        List<ItemCatalogue> itemCatalogueList = itemCatalogueRepository.findAll();
        assertThat(itemCatalogueList).hasSize(databaseSizeBeforeUpdate);
        ItemCatalogue testItemCatalogue = itemCatalogueList.get(itemCatalogueList.size() - 1);
        assertThat(testItemCatalogue.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemCatalogue.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testItemCatalogue.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItemCatalogue.getCatalogCode()).isEqualTo(UPDATED_CATALOG_CODE);
        assertThat(testItemCatalogue.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateItemCatalogueWithPatch() throws Exception {
        // Initialize the database
        itemCatalogueRepository.saveAndFlush(itemCatalogue);

        int databaseSizeBeforeUpdate = itemCatalogueRepository.findAll().size();

        // Update the itemCatalogue using partial update
        ItemCatalogue partialUpdatedItemCatalogue = new ItemCatalogue();
        partialUpdatedItemCatalogue.setId(itemCatalogue.getId());

        partialUpdatedItemCatalogue
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .catalogCode(UPDATED_CATALOG_CODE)
            .active(UPDATED_ACTIVE);

        restItemCatalogueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemCatalogue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemCatalogue))
            )
            .andExpect(status().isOk());

        // Validate the ItemCatalogue in the database
        List<ItemCatalogue> itemCatalogueList = itemCatalogueRepository.findAll();
        assertThat(itemCatalogueList).hasSize(databaseSizeBeforeUpdate);
        ItemCatalogue testItemCatalogue = itemCatalogueList.get(itemCatalogueList.size() - 1);
        assertThat(testItemCatalogue.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemCatalogue.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testItemCatalogue.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItemCatalogue.getCatalogCode()).isEqualTo(UPDATED_CATALOG_CODE);
        assertThat(testItemCatalogue.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingItemCatalogue() throws Exception {
        int databaseSizeBeforeUpdate = itemCatalogueRepository.findAll().size();
        itemCatalogue.setId(count.incrementAndGet());

        // Create the ItemCatalogue
        ItemCatalogueDTO itemCatalogueDTO = itemCatalogueMapper.toDto(itemCatalogue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemCatalogueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemCatalogueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemCatalogueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemCatalogue in the database
        List<ItemCatalogue> itemCatalogueList = itemCatalogueRepository.findAll();
        assertThat(itemCatalogueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemCatalogue() throws Exception {
        int databaseSizeBeforeUpdate = itemCatalogueRepository.findAll().size();
        itemCatalogue.setId(count.incrementAndGet());

        // Create the ItemCatalogue
        ItemCatalogueDTO itemCatalogueDTO = itemCatalogueMapper.toDto(itemCatalogue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemCatalogueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemCatalogueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemCatalogue in the database
        List<ItemCatalogue> itemCatalogueList = itemCatalogueRepository.findAll();
        assertThat(itemCatalogueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemCatalogue() throws Exception {
        int databaseSizeBeforeUpdate = itemCatalogueRepository.findAll().size();
        itemCatalogue.setId(count.incrementAndGet());

        // Create the ItemCatalogue
        ItemCatalogueDTO itemCatalogueDTO = itemCatalogueMapper.toDto(itemCatalogue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemCatalogueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemCatalogueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemCatalogue in the database
        List<ItemCatalogue> itemCatalogueList = itemCatalogueRepository.findAll();
        assertThat(itemCatalogueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemCatalogue() throws Exception {
        // Initialize the database
        itemCatalogueRepository.saveAndFlush(itemCatalogue);

        int databaseSizeBeforeDelete = itemCatalogueRepository.findAll().size();

        // Delete the itemCatalogue
        restItemCatalogueMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemCatalogue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemCatalogue> itemCatalogueList = itemCatalogueRepository.findAll();
        assertThat(itemCatalogueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
