package ec.loja.web.rest;

import ec.loja.repository.ItemCatalogueRepository;
import ec.loja.service.ItemCatalogueService;
import ec.loja.service.dto.ItemCatalogueDTO;
import ec.loja.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link ec.loja.domain.ItemCatalogue}.
 */
@RestController
@RequestMapping("/api")
public class ItemCatalogueResource {

    private final Logger log = LoggerFactory.getLogger(ItemCatalogueResource.class);

    private static final String ENTITY_NAME = "itemCatalogue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemCatalogueService itemCatalogueService;

    private final ItemCatalogueRepository itemCatalogueRepository;

    public ItemCatalogueResource(ItemCatalogueService itemCatalogueService, ItemCatalogueRepository itemCatalogueRepository) {
        this.itemCatalogueService = itemCatalogueService;
        this.itemCatalogueRepository = itemCatalogueRepository;
    }

    /**
     * {@code POST  /item-catalogues} : Create a new itemCatalogue.
     *
     * @param itemCatalogueDTO the itemCatalogueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemCatalogueDTO, or with status {@code 400 (Bad Request)} if the itemCatalogue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-catalogues")
    public ResponseEntity<ItemCatalogueDTO> createItemCatalogue(@Valid @RequestBody ItemCatalogueDTO itemCatalogueDTO)
        throws URISyntaxException {
        log.debug("REST request to save ItemCatalogue : {}", itemCatalogueDTO);
        if (itemCatalogueDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemCatalogue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemCatalogueDTO result = itemCatalogueService.save(itemCatalogueDTO);
        return ResponseEntity
            .created(new URI("/api/item-catalogues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-catalogues/:id} : Updates an existing itemCatalogue.
     *
     * @param id the id of the itemCatalogueDTO to save.
     * @param itemCatalogueDTO the itemCatalogueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemCatalogueDTO,
     * or with status {@code 400 (Bad Request)} if the itemCatalogueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemCatalogueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-catalogues/{id}")
    public ResponseEntity<ItemCatalogueDTO> updateItemCatalogue(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ItemCatalogueDTO itemCatalogueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ItemCatalogue : {}, {}", id, itemCatalogueDTO);
        if (itemCatalogueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemCatalogueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemCatalogueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemCatalogueDTO result = itemCatalogueService.save(itemCatalogueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemCatalogueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-catalogues/:id} : Partial updates given fields of an existing itemCatalogue, field will ignore if it is null
     *
     * @param id the id of the itemCatalogueDTO to save.
     * @param itemCatalogueDTO the itemCatalogueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemCatalogueDTO,
     * or with status {@code 400 (Bad Request)} if the itemCatalogueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemCatalogueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemCatalogueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/item-catalogues/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ItemCatalogueDTO> partialUpdateItemCatalogue(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ItemCatalogueDTO itemCatalogueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemCatalogue partially : {}, {}", id, itemCatalogueDTO);
        if (itemCatalogueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemCatalogueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemCatalogueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemCatalogueDTO> result = itemCatalogueService.partialUpdate(itemCatalogueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemCatalogueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /item-catalogues} : get all the itemCatalogues.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemCatalogues in body.
     */
    @GetMapping("/item-catalogues")
    public ResponseEntity<List<ItemCatalogueDTO>> getAllItemCatalogues(Pageable pageable) {
        log.debug("REST request to get a page of ItemCatalogues");
        Page<ItemCatalogueDTO> page = itemCatalogueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-catalogues/:id} : get the "id" itemCatalogue.
     *
     * @param id the id of the itemCatalogueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemCatalogueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-catalogues/{id}")
    public ResponseEntity<ItemCatalogueDTO> getItemCatalogue(@PathVariable Long id) {
        log.debug("REST request to get ItemCatalogue : {}", id);
        Optional<ItemCatalogueDTO> itemCatalogueDTO = itemCatalogueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemCatalogueDTO);
    }

    /**
     * {@code DELETE  /item-catalogues/:id} : delete the "id" itemCatalogue.
     *
     * @param id the id of the itemCatalogueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-catalogues/{id}")
    public ResponseEntity<Void> deleteItemCatalogue(@PathVariable Long id) {
        log.debug("REST request to delete ItemCatalogue : {}", id);
        itemCatalogueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
