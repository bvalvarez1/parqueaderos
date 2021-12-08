package ec.loja.web.rest;

import ec.loja.repository.HoraryRepository;
import ec.loja.service.HoraryService;
import ec.loja.service.dto.HoraryDTO;
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
 * REST controller for managing {@link ec.loja.domain.Horary}.
 */
@RestController
@RequestMapping("/api")
public class HoraryResource {

    private final Logger log = LoggerFactory.getLogger(HoraryResource.class);

    private static final String ENTITY_NAME = "horary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HoraryService horaryService;

    private final HoraryRepository horaryRepository;

    public HoraryResource(HoraryService horaryService, HoraryRepository horaryRepository) {
        this.horaryService = horaryService;
        this.horaryRepository = horaryRepository;
    }

    /**
     * {@code POST  /horaries} : Create a new horary.
     *
     * @param horaryDTO the horaryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new horaryDTO, or with status {@code 400 (Bad Request)} if the horary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/horaries")
    public ResponseEntity<HoraryDTO> createHorary(@Valid @RequestBody HoraryDTO horaryDTO) throws URISyntaxException {
        log.debug("REST request to save Horary : {}", horaryDTO);
        if (horaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new horary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HoraryDTO result = horaryService.save(horaryDTO);
        return ResponseEntity
            .created(new URI("/api/horaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /horaries/:id} : Updates an existing horary.
     *
     * @param id the id of the horaryDTO to save.
     * @param horaryDTO the horaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated horaryDTO,
     * or with status {@code 400 (Bad Request)} if the horaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the horaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/horaries/{id}")
    public ResponseEntity<HoraryDTO> updateHorary(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HoraryDTO horaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Horary : {}, {}", id, horaryDTO);
        if (horaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, horaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!horaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HoraryDTO result = horaryService.save(horaryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, horaryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /horaries/:id} : Partial updates given fields of an existing horary, field will ignore if it is null
     *
     * @param id the id of the horaryDTO to save.
     * @param horaryDTO the horaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated horaryDTO,
     * or with status {@code 400 (Bad Request)} if the horaryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the horaryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the horaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/horaries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HoraryDTO> partialUpdateHorary(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HoraryDTO horaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Horary partially : {}, {}", id, horaryDTO);
        if (horaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, horaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!horaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HoraryDTO> result = horaryService.partialUpdate(horaryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, horaryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /horaries} : get all the horaries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of horaries in body.
     */
    @GetMapping("/horaries")
    public ResponseEntity<List<HoraryDTO>> getAllHoraries(Pageable pageable) {
        log.debug("REST request to get a page of Horaries");
        Page<HoraryDTO> page = horaryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /horaries/:id} : get the "id" horary.
     *
     * @param id the id of the horaryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the horaryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/horaries/{id}")
    public ResponseEntity<HoraryDTO> getHorary(@PathVariable Long id) {
        log.debug("REST request to get Horary : {}", id);
        Optional<HoraryDTO> horaryDTO = horaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(horaryDTO);
    }

    /**
     * {@code DELETE  /horaries/:id} : delete the "id" horary.
     *
     * @param id the id of the horaryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/horaries/{id}")
    public ResponseEntity<Void> deleteHorary(@PathVariable Long id) {
        log.debug("REST request to delete Horary : {}", id);
        horaryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
