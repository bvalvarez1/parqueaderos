package ec.loja.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ec.loja.IntegrationTest;
import ec.loja.domain.Receipt;
import ec.loja.repository.ReceiptRepository;
import ec.loja.service.dto.ReceiptDTO;
import ec.loja.service.mapper.ReceiptMapper;
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
 * Integration tests for the {@link ReceiptResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReceiptResourceIT {

    private static final String DEFAULT_AUTHORIZATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_AUTHORIZATION_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SEQUENTIAL = "AAAAAAAAAA";
    private static final String UPDATED_SEQUENTIAL = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_SRIACCESSKEY = "AAAAAAAAAA";
    private static final String UPDATED_SRIACCESSKEY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SRIAUTHORIZATIONDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SRIAUTHORIZATIONDATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_RECEIPTDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECEIPTDATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/receipts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ReceiptMapper receiptMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReceiptMockMvc;

    private Receipt receipt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Receipt createEntity(EntityManager em) {
        Receipt receipt = new Receipt()
            .authorizationNumber(DEFAULT_AUTHORIZATION_NUMBER)
            .sequential(DEFAULT_SEQUENTIAL)
            .status(DEFAULT_STATUS)
            .sriaccesskey(DEFAULT_SRIACCESSKEY)
            .sriauthorizationdate(DEFAULT_SRIAUTHORIZATIONDATE)
            .receiptdate(DEFAULT_RECEIPTDATE);
        return receipt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Receipt createUpdatedEntity(EntityManager em) {
        Receipt receipt = new Receipt()
            .authorizationNumber(UPDATED_AUTHORIZATION_NUMBER)
            .sequential(UPDATED_SEQUENTIAL)
            .status(UPDATED_STATUS)
            .sriaccesskey(UPDATED_SRIACCESSKEY)
            .sriauthorizationdate(UPDATED_SRIAUTHORIZATIONDATE)
            .receiptdate(UPDATED_RECEIPTDATE);
        return receipt;
    }

    @BeforeEach
    public void initTest() {
        receipt = createEntity(em);
    }

    @Test
    @Transactional
    void createReceipt() throws Exception {
        int databaseSizeBeforeCreate = receiptRepository.findAll().size();
        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);
        restReceiptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receiptDTO)))
            .andExpect(status().isCreated());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeCreate + 1);
        Receipt testReceipt = receiptList.get(receiptList.size() - 1);
        assertThat(testReceipt.getAuthorizationNumber()).isEqualTo(DEFAULT_AUTHORIZATION_NUMBER);
        assertThat(testReceipt.getSequential()).isEqualTo(DEFAULT_SEQUENTIAL);
        assertThat(testReceipt.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReceipt.getSriaccesskey()).isEqualTo(DEFAULT_SRIACCESSKEY);
        assertThat(testReceipt.getSriauthorizationdate()).isEqualTo(DEFAULT_SRIAUTHORIZATIONDATE);
        assertThat(testReceipt.getReceiptdate()).isEqualTo(DEFAULT_RECEIPTDATE);
    }

    @Test
    @Transactional
    void createReceiptWithExistingId() throws Exception {
        // Create the Receipt with an existing ID
        receipt.setId(1L);
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        int databaseSizeBeforeCreate = receiptRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReceiptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receiptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReceipts() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        // Get all the receiptList
        restReceiptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(receipt.getId().intValue())))
            .andExpect(jsonPath("$.[*].authorizationNumber").value(hasItem(DEFAULT_AUTHORIZATION_NUMBER)))
            .andExpect(jsonPath("$.[*].sequential").value(hasItem(DEFAULT_SEQUENTIAL)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].sriaccesskey").value(hasItem(DEFAULT_SRIACCESSKEY)))
            .andExpect(jsonPath("$.[*].sriauthorizationdate").value(hasItem(DEFAULT_SRIAUTHORIZATIONDATE.toString())))
            .andExpect(jsonPath("$.[*].receiptdate").value(hasItem(DEFAULT_RECEIPTDATE.toString())));
    }

    @Test
    @Transactional
    void getReceipt() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        // Get the receipt
        restReceiptMockMvc
            .perform(get(ENTITY_API_URL_ID, receipt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(receipt.getId().intValue()))
            .andExpect(jsonPath("$.authorizationNumber").value(DEFAULT_AUTHORIZATION_NUMBER))
            .andExpect(jsonPath("$.sequential").value(DEFAULT_SEQUENTIAL))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.sriaccesskey").value(DEFAULT_SRIACCESSKEY))
            .andExpect(jsonPath("$.sriauthorizationdate").value(DEFAULT_SRIAUTHORIZATIONDATE.toString()))
            .andExpect(jsonPath("$.receiptdate").value(DEFAULT_RECEIPTDATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReceipt() throws Exception {
        // Get the receipt
        restReceiptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReceipt() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();

        // Update the receipt
        Receipt updatedReceipt = receiptRepository.findById(receipt.getId()).get();
        // Disconnect from session so that the updates on updatedReceipt are not directly saved in db
        em.detach(updatedReceipt);
        updatedReceipt
            .authorizationNumber(UPDATED_AUTHORIZATION_NUMBER)
            .sequential(UPDATED_SEQUENTIAL)
            .status(UPDATED_STATUS)
            .sriaccesskey(UPDATED_SRIACCESSKEY)
            .sriauthorizationdate(UPDATED_SRIAUTHORIZATIONDATE)
            .receiptdate(UPDATED_RECEIPTDATE);
        ReceiptDTO receiptDTO = receiptMapper.toDto(updatedReceipt);

        restReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, receiptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isOk());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
        Receipt testReceipt = receiptList.get(receiptList.size() - 1);
        assertThat(testReceipt.getAuthorizationNumber()).isEqualTo(UPDATED_AUTHORIZATION_NUMBER);
        assertThat(testReceipt.getSequential()).isEqualTo(UPDATED_SEQUENTIAL);
        assertThat(testReceipt.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReceipt.getSriaccesskey()).isEqualTo(UPDATED_SRIACCESSKEY);
        assertThat(testReceipt.getSriauthorizationdate()).isEqualTo(UPDATED_SRIAUTHORIZATIONDATE);
        assertThat(testReceipt.getReceiptdate()).isEqualTo(UPDATED_RECEIPTDATE);
    }

    @Test
    @Transactional
    void putNonExistingReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, receiptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receiptDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReceiptWithPatch() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();

        // Update the receipt using partial update
        Receipt partialUpdatedReceipt = new Receipt();
        partialUpdatedReceipt.setId(receipt.getId());

        partialUpdatedReceipt
            .authorizationNumber(UPDATED_AUTHORIZATION_NUMBER)
            .status(UPDATED_STATUS)
            .sriaccesskey(UPDATED_SRIACCESSKEY)
            .sriauthorizationdate(UPDATED_SRIAUTHORIZATIONDATE)
            .receiptdate(UPDATED_RECEIPTDATE);

        restReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReceipt))
            )
            .andExpect(status().isOk());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
        Receipt testReceipt = receiptList.get(receiptList.size() - 1);
        assertThat(testReceipt.getAuthorizationNumber()).isEqualTo(UPDATED_AUTHORIZATION_NUMBER);
        assertThat(testReceipt.getSequential()).isEqualTo(DEFAULT_SEQUENTIAL);
        assertThat(testReceipt.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReceipt.getSriaccesskey()).isEqualTo(UPDATED_SRIACCESSKEY);
        assertThat(testReceipt.getSriauthorizationdate()).isEqualTo(UPDATED_SRIAUTHORIZATIONDATE);
        assertThat(testReceipt.getReceiptdate()).isEqualTo(UPDATED_RECEIPTDATE);
    }

    @Test
    @Transactional
    void fullUpdateReceiptWithPatch() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();

        // Update the receipt using partial update
        Receipt partialUpdatedReceipt = new Receipt();
        partialUpdatedReceipt.setId(receipt.getId());

        partialUpdatedReceipt
            .authorizationNumber(UPDATED_AUTHORIZATION_NUMBER)
            .sequential(UPDATED_SEQUENTIAL)
            .status(UPDATED_STATUS)
            .sriaccesskey(UPDATED_SRIACCESSKEY)
            .sriauthorizationdate(UPDATED_SRIAUTHORIZATIONDATE)
            .receiptdate(UPDATED_RECEIPTDATE);

        restReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReceipt))
            )
            .andExpect(status().isOk());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
        Receipt testReceipt = receiptList.get(receiptList.size() - 1);
        assertThat(testReceipt.getAuthorizationNumber()).isEqualTo(UPDATED_AUTHORIZATION_NUMBER);
        assertThat(testReceipt.getSequential()).isEqualTo(UPDATED_SEQUENTIAL);
        assertThat(testReceipt.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReceipt.getSriaccesskey()).isEqualTo(UPDATED_SRIACCESSKEY);
        assertThat(testReceipt.getSriauthorizationdate()).isEqualTo(UPDATED_SRIAUTHORIZATIONDATE);
        assertThat(testReceipt.getReceiptdate()).isEqualTo(UPDATED_RECEIPTDATE);
    }

    @Test
    @Transactional
    void patchNonExistingReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, receiptDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReceipt() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        int databaseSizeBeforeDelete = receiptRepository.findAll().size();

        // Delete the receipt
        restReceiptMockMvc
            .perform(delete(ENTITY_API_URL_ID, receipt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
