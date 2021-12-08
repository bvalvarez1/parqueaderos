package ec.loja.web.rest;

import ec.loja.repository.SystemParameterInstitutionRepository;
import ec.loja.service.SystemParameterInstitutionService;
import ec.loja.service.dto.SystemParameterInstitutionDTO;
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
 * REST controller for managing {@link ec.loja.domain.SystemParameterInstitution}.
 */
@RestController
@RequestMapping("/api")
public class SystemParameterInstitutionResource {

    private final Logger log = LoggerFactory.getLogger(SystemParameterInstitutionResource.class);

    private static final String ENTITY_NAME = "systemParameterInstitution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemParameterInstitutionService systemParameterInstitutionService;

    private final SystemParameterInstitutionRepository systemParameterInstitutionRepository;

    public SystemParameterInstitutionResource(
        SystemParameterInstitutionService systemParameterInstitutionService,
        SystemParameterInstitutionRepository systemParameterInstitutionRepository
    ) {
        this.systemParameterInstitutionService = systemParameterInstitutionService;
        this.systemParameterInstitutionRepository = systemParameterInstitutionRepository;
    }

    /**
     * {@code POST  /system-parameter-institutions} : Create a new systemParameterInstitution.
     *
     * @param systemParameterInstitutionDTO the systemParameterInstitutionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemParameterInstitutionDTO, or with status {@code 400 (Bad Request)} if the systemParameterInstitution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/system-parameter-institutions")
    public ResponseEntity<SystemParameterInstitutionDTO> createSystemParameterInstitution(
        @Valid @RequestBody SystemParameterInstitutionDTO systemParameterInstitutionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SystemParameterInstitution : {}", systemParameterInstitutionDTO);
        if (systemParameterInstitutionDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemParameterInstitution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemParameterInstitutionDTO result = systemParameterInstitutionService.save(systemParameterInstitutionDTO);
        return ResponseEntity
            .created(new URI("/api/system-parameter-institutions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /system-parameter-institutions/:id} : Updates an existing systemParameterInstitution.
     *
     * @param id the id of the systemParameterInstitutionDTO to save.
     * @param systemParameterInstitutionDTO the systemParameterInstitutionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemParameterInstitutionDTO,
     * or with status {@code 400 (Bad Request)} if the systemParameterInstitutionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemParameterInstitutionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/system-parameter-institutions/{id}")
    public ResponseEntity<SystemParameterInstitutionDTO> updateSystemParameterInstitution(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SystemParameterInstitutionDTO systemParameterInstitutionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SystemParameterInstitution : {}, {}", id, systemParameterInstitutionDTO);
        if (systemParameterInstitutionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemParameterInstitutionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemParameterInstitutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SystemParameterInstitutionDTO result = systemParameterInstitutionService.save(systemParameterInstitutionDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemParameterInstitutionDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /system-parameter-institutions/:id} : Partial updates given fields of an existing systemParameterInstitution, field will ignore if it is null
     *
     * @param id the id of the systemParameterInstitutionDTO to save.
     * @param systemParameterInstitutionDTO the systemParameterInstitutionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemParameterInstitutionDTO,
     * or with status {@code 400 (Bad Request)} if the systemParameterInstitutionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the systemParameterInstitutionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemParameterInstitutionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/system-parameter-institutions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemParameterInstitutionDTO> partialUpdateSystemParameterInstitution(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SystemParameterInstitutionDTO systemParameterInstitutionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SystemParameterInstitution partially : {}, {}", id, systemParameterInstitutionDTO);
        if (systemParameterInstitutionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemParameterInstitutionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemParameterInstitutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemParameterInstitutionDTO> result = systemParameterInstitutionService.partialUpdate(systemParameterInstitutionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemParameterInstitutionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /system-parameter-institutions} : get all the systemParameterInstitutions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemParameterInstitutions in body.
     */
    @GetMapping("/system-parameter-institutions")
    public ResponseEntity<List<SystemParameterInstitutionDTO>> getAllSystemParameterInstitutions(Pageable pageable) {
        log.debug("REST request to get a page of SystemParameterInstitutions");
        Page<SystemParameterInstitutionDTO> page = systemParameterInstitutionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /system-parameter-institutions/:id} : get the "id" systemParameterInstitution.
     *
     * @param id the id of the systemParameterInstitutionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemParameterInstitutionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/system-parameter-institutions/{id}")
    public ResponseEntity<SystemParameterInstitutionDTO> getSystemParameterInstitution(@PathVariable Long id) {
        log.debug("REST request to get SystemParameterInstitution : {}", id);
        Optional<SystemParameterInstitutionDTO> systemParameterInstitutionDTO = systemParameterInstitutionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemParameterInstitutionDTO);
    }

    /**
     * {@code DELETE  /system-parameter-institutions/:id} : delete the "id" systemParameterInstitution.
     *
     * @param id the id of the systemParameterInstitutionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/system-parameter-institutions/{id}")
    public ResponseEntity<Void> deleteSystemParameterInstitution(@PathVariable Long id) {
        log.debug("REST request to delete SystemParameterInstitution : {}", id);
        systemParameterInstitutionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
