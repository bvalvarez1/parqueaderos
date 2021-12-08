package ec.loja.web.rest;

import ec.loja.repository.ReceiptRepository;
import ec.loja.service.ReceiptService;
import ec.loja.service.dto.ReceiptDTO;
import ec.loja.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ec.loja.domain.Receipt}.
 */
@RestController
@RequestMapping("/api")
public class ReceiptResource {

    private final Logger log = LoggerFactory.getLogger(ReceiptResource.class);

    private static final String ENTITY_NAME = "receipt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReceiptService receiptService;

    private final ReceiptRepository receiptRepository;

    public ReceiptResource(ReceiptService receiptService, ReceiptRepository receiptRepository) {
        this.receiptService = receiptService;
        this.receiptRepository = receiptRepository;
    }

    /**
     * {@code POST  /receipts} : Create a new receipt.
     *
     * @param receiptDTO the receiptDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new receiptDTO, or with status {@code 400 (Bad Request)} if the receipt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/receipts")
    public ResponseEntity<ReceiptDTO> createReceipt(@RequestBody ReceiptDTO receiptDTO) throws URISyntaxException {
        log.debug("REST request to save Receipt : {}", receiptDTO);
        if (receiptDTO.getId() != null) {
            throw new BadRequestAlertException("A new receipt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReceiptDTO result = receiptService.save(receiptDTO);
        return ResponseEntity
            .created(new URI("/api/receipts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /receipts/:id} : Updates an existing receipt.
     *
     * @param id the id of the receiptDTO to save.
     * @param receiptDTO the receiptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receiptDTO,
     * or with status {@code 400 (Bad Request)} if the receiptDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the receiptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/receipts/{id}")
    public ResponseEntity<ReceiptDTO> updateReceipt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReceiptDTO receiptDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Receipt : {}, {}", id, receiptDTO);
        if (receiptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, receiptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!receiptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReceiptDTO result = receiptService.save(receiptDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, receiptDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /receipts/:id} : Partial updates given fields of an existing receipt, field will ignore if it is null
     *
     * @param id the id of the receiptDTO to save.
     * @param receiptDTO the receiptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receiptDTO,
     * or with status {@code 400 (Bad Request)} if the receiptDTO is not valid,
     * or with status {@code 404 (Not Found)} if the receiptDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the receiptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/receipts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReceiptDTO> partialUpdateReceipt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReceiptDTO receiptDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Receipt partially : {}, {}", id, receiptDTO);
        if (receiptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, receiptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!receiptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReceiptDTO> result = receiptService.partialUpdate(receiptDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, receiptDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /receipts} : get all the receipts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of receipts in body.
     */
    @GetMapping("/receipts")
    public ResponseEntity<List<ReceiptDTO>> getAllReceipts(Pageable pageable) {
        log.debug("REST request to get a page of Receipts");
        Page<ReceiptDTO> page = receiptService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /receipts/:id} : get the "id" receipt.
     *
     * @param id the id of the receiptDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the receiptDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/receipts/{id}")
    public ResponseEntity<ReceiptDTO> getReceipt(@PathVariable Long id) {
        log.debug("REST request to get Receipt : {}", id);
        Optional<ReceiptDTO> receiptDTO = receiptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(receiptDTO);
    }

    /**
     * {@code DELETE  /receipts/:id} : delete the "id" receipt.
     *
     * @param id the id of the receiptDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/receipts/{id}")
    public ResponseEntity<Void> deleteReceipt(@PathVariable Long id) {
        log.debug("REST request to delete Receipt : {}", id);
        receiptService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
