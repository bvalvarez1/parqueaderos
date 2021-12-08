package ec.loja.web.rest;

import static ec.loja.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ec.loja.IntegrationTest;
import ec.loja.domain.Contract;
import ec.loja.domain.ItemCatalogue;
import ec.loja.domain.Person;
import ec.loja.repository.ContractRepository;
import ec.loja.service.dto.ContractDTO;
import ec.loja.service.mapper.ContractMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ContractResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContractResourceIT {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INIT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INIT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_HOURS = new BigDecimal(1);
    private static final BigDecimal UPDATED_HOURS = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractMockMvc;

    private Contract contract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createEntity(EntityManager em) {
        Contract contract = new Contract()
            .number(DEFAULT_NUMBER)
            .initDate(DEFAULT_INIT_DATE)
            .endDate(DEFAULT_END_DATE)
            .value(DEFAULT_VALUE)
            .hours(DEFAULT_HOURS);
        // Add required entity
        ItemCatalogue itemCatalogue;
        if (TestUtil.findAll(em, ItemCatalogue.class).isEmpty()) {
            itemCatalogue = ItemCatalogueResourceIT.createEntity(em);
            em.persist(itemCatalogue);
            em.flush();
        } else {
            itemCatalogue = TestUtil.findAll(em, ItemCatalogue.class).get(0);
        }
        contract.setStatus(itemCatalogue);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createEntity(em);
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        contract.setContractor(person);
        return contract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createUpdatedEntity(EntityManager em) {
        Contract contract = new Contract()
            .number(UPDATED_NUMBER)
            .initDate(UPDATED_INIT_DATE)
            .endDate(UPDATED_END_DATE)
            .value(UPDATED_VALUE)
            .hours(UPDATED_HOURS);
        // Add required entity
        ItemCatalogue itemCatalogue;
        if (TestUtil.findAll(em, ItemCatalogue.class).isEmpty()) {
            itemCatalogue = ItemCatalogueResourceIT.createUpdatedEntity(em);
            em.persist(itemCatalogue);
            em.flush();
        } else {
            itemCatalogue = TestUtil.findAll(em, ItemCatalogue.class).get(0);
        }
        contract.setStatus(itemCatalogue);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createUpdatedEntity(em);
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        contract.setContractor(person);
        return contract;
    }

    @BeforeEach
    public void initTest() {
        contract = createEntity(em);
    }

    @Test
    @Transactional
    void createContract() throws Exception {
        int databaseSizeBeforeCreate = contractRepository.findAll().size();
        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);
        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isCreated());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate + 1);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testContract.getInitDate()).isEqualTo(DEFAULT_INIT_DATE);
        assertThat(testContract.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testContract.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
        assertThat(testContract.getHours()).isEqualByComparingTo(DEFAULT_HOURS);
    }

    @Test
    @Transactional
    void createContractWithExistingId() throws Exception {
        // Create the Contract with an existing ID
        contract.setId(1L);
        ContractDTO contractDTO = contractMapper.toDto(contract);

        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setNumber(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInitDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setInitDate(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setEndDate(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setValue(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContracts() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].initDate").value(hasItem(DEFAULT_INIT_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))))
            .andExpect(jsonPath("$.[*].hours").value(hasItem(sameNumber(DEFAULT_HOURS))));
    }

    @Test
    @Transactional
    void getContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc
            .perform(get(ENTITY_API_URL_ID, contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.initDate").value(DEFAULT_INIT_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.value").value(sameNumber(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.hours").value(sameNumber(DEFAULT_HOURS)));
    }

    @Test
    @Transactional
    void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract
        Contract updatedContract = contractRepository.findById(contract.getId()).get();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        updatedContract
            .number(UPDATED_NUMBER)
            .initDate(UPDATED_INIT_DATE)
            .endDate(UPDATED_END_DATE)
            .value(UPDATED_VALUE)
            .hours(UPDATED_HOURS);
        ContractDTO contractDTO = contractMapper.toDto(updatedContract);

        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testContract.getInitDate()).isEqualTo(UPDATED_INIT_DATE);
        assertThat(testContract.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testContract.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testContract.getHours()).isEqualTo(UPDATED_HOURS);
    }

    @Test
    @Transactional
    void putNonExistingContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContractWithPatch() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract.initDate(UPDATED_INIT_DATE).endDate(UPDATED_END_DATE);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testContract.getInitDate()).isEqualTo(UPDATED_INIT_DATE);
        assertThat(testContract.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testContract.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
        assertThat(testContract.getHours()).isEqualByComparingTo(DEFAULT_HOURS);
    }

    @Test
    @Transactional
    void fullUpdateContractWithPatch() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .number(UPDATED_NUMBER)
            .initDate(UPDATED_INIT_DATE)
            .endDate(UPDATED_END_DATE)
            .value(UPDATED_VALUE)
            .hours(UPDATED_HOURS);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testContract.getInitDate()).isEqualTo(UPDATED_INIT_DATE);
        assertThat(testContract.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testContract.getValue()).isEqualByComparingTo(UPDATED_VALUE);
        assertThat(testContract.getHours()).isEqualByComparingTo(UPDATED_HOURS);
    }

    @Test
    @Transactional
    void patchNonExistingContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contractDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contractDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeDelete = contractRepository.findAll().size();

        // Delete the contract
        restContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, contract.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
