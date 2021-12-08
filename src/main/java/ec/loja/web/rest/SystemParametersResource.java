package ec.loja.web.rest;

import ec.loja.repository.SystemParametersRepository;
import ec.loja.service.SystemParametersService;
import ec.loja.service.dto.SystemParametersDTO;
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
 * REST controller for managing {@link ec.loja.domain.SystemParameters}.
 */
@RestController
@RequestMapping("/api")
public class SystemParametersResource {

    private final Logger log = LoggerFactory.getLogger(SystemParametersResource.class);

    private static final String ENTITY_NAME = "systemParameters";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemParametersService systemParametersService;

    private final SystemParametersRepository systemParametersRepository;

    public SystemParametersResource(
        SystemParametersService systemParametersService,
        SystemParametersRepository systemParametersRepository
    ) {
        this.systemParametersService = systemParametersService;
        this.systemParametersRepository = systemParametersRepository;
    }

    /**
     * {@code POST  /system-parameters} : Create a new systemParameters.
     *
     * @param systemParametersDTO the systemParametersDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemParametersDTO, or with status {@code 400 (Bad Request)} if the systemParameters has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/system-parameters")
    public ResponseEntity<SystemParametersDTO> createSystemParameters(@Valid @RequestBody SystemParametersDTO systemParametersDTO)
        throws URISyntaxException {
        log.debug("REST request to save SystemParameters : {}", systemParametersDTO);
        if (systemParametersDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemParameters cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemParametersDTO result = systemParametersService.save(systemParametersDTO);
        return ResponseEntity
            .created(new URI("/api/system-parameters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /system-parameters/:id} : Updates an existing systemParameters.
     *
     * @param id the id of the systemParametersDTO to save.
     * @param systemParametersDTO the systemParametersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemParametersDTO,
     * or with status {@code 400 (Bad Request)} if the systemParametersDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemParametersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/system-parameters/{id}")
    public ResponseEntity<SystemParametersDTO> updateSystemParameters(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SystemParametersDTO systemParametersDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SystemParameters : {}, {}", id, systemParametersDTO);
        if (systemParametersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemParametersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemParametersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SystemParametersDTO result = systemParametersService.save(systemParametersDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemParametersDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /system-parameters/:id} : Partial updates given fields of an existing systemParameters, field will ignore if it is null
     *
     * @param id the id of the systemParametersDTO to save.
     * @param systemParametersDTO the systemParametersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemParametersDTO,
     * or with status {@code 400 (Bad Request)} if the systemParametersDTO is not valid,
     * or with status {@code 404 (Not Found)} if the systemParametersDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemParametersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/system-parameters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemParametersDTO> partialUpdateSystemParameters(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SystemParametersDTO systemParametersDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SystemParameters partially : {}, {}", id, systemParametersDTO);
        if (systemParametersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemParametersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemParametersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemParametersDTO> result = systemParametersService.partialUpdate(systemParametersDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemParametersDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /system-parameters} : get all the systemParameters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemParameters in body.
     */
    @GetMapping("/system-parameters")
    public ResponseEntity<List<SystemParametersDTO>> getAllSystemParameters(Pageable pageable) {
        log.debug("REST request to get a page of SystemParameters");
        Page<SystemParametersDTO> page = systemParametersService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /system-parameters/:id} : get the "id" systemParameters.
     *
     * @param id the id of the systemParametersDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemParametersDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/system-parameters/{id}")
    public ResponseEntity<SystemParametersDTO> getSystemParameters(@PathVariable Long id) {
        log.debug("REST request to get SystemParameters : {}", id);
        Optional<SystemParametersDTO> systemParametersDTO = systemParametersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemParametersDTO);
    }

    /**
     * {@code DELETE  /system-parameters/:id} : delete the "id" systemParameters.
     *
     * @param id the id of the systemParametersDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/system-parameters/{id}")
    public ResponseEntity<Void> deleteSystemParameters(@PathVariable Long id) {
        log.debug("REST request to delete SystemParameters : {}", id);
        systemParametersService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
