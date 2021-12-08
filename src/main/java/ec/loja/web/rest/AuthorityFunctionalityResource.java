package ec.loja.web.rest;

import ec.loja.repository.AuthorityFunctionalityRepository;
import ec.loja.service.AuthorityFunctionalityService;
import ec.loja.service.dto.AuthorityFunctionalityDTO;
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
 * REST controller for managing {@link ec.loja.domain.AuthorityFunctionality}.
 */
@RestController
@RequestMapping("/api")
public class AuthorityFunctionalityResource {

    private final Logger log = LoggerFactory.getLogger(AuthorityFunctionalityResource.class);

    private static final String ENTITY_NAME = "authorityFunctionality";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthorityFunctionalityService authorityFunctionalityService;

    private final AuthorityFunctionalityRepository authorityFunctionalityRepository;

    public AuthorityFunctionalityResource(
        AuthorityFunctionalityService authorityFunctionalityService,
        AuthorityFunctionalityRepository authorityFunctionalityRepository
    ) {
        this.authorityFunctionalityService = authorityFunctionalityService;
        this.authorityFunctionalityRepository = authorityFunctionalityRepository;
    }

    /**
     * {@code POST  /authority-functionalities} : Create a new authorityFunctionality.
     *
     * @param authorityFunctionalityDTO the authorityFunctionalityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new authorityFunctionalityDTO, or with status {@code 400 (Bad Request)} if the authorityFunctionality has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/authority-functionalities")
    public ResponseEntity<AuthorityFunctionalityDTO> createAuthorityFunctionality(
        @Valid @RequestBody AuthorityFunctionalityDTO authorityFunctionalityDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AuthorityFunctionality : {}", authorityFunctionalityDTO);
        if (authorityFunctionalityDTO.getId() != null) {
            throw new BadRequestAlertException("A new authorityFunctionality cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuthorityFunctionalityDTO result = authorityFunctionalityService.save(authorityFunctionalityDTO);
        return ResponseEntity
            .created(new URI("/api/authority-functionalities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /authority-functionalities/:id} : Updates an existing authorityFunctionality.
     *
     * @param id the id of the authorityFunctionalityDTO to save.
     * @param authorityFunctionalityDTO the authorityFunctionalityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorityFunctionalityDTO,
     * or with status {@code 400 (Bad Request)} if the authorityFunctionalityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authorityFunctionalityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/authority-functionalities/{id}")
    public ResponseEntity<AuthorityFunctionalityDTO> updateAuthorityFunctionality(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AuthorityFunctionalityDTO authorityFunctionalityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AuthorityFunctionality : {}, {}", id, authorityFunctionalityDTO);
        if (authorityFunctionalityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authorityFunctionalityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authorityFunctionalityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AuthorityFunctionalityDTO result = authorityFunctionalityService.save(authorityFunctionalityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authorityFunctionalityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /authority-functionalities/:id} : Partial updates given fields of an existing authorityFunctionality, field will ignore if it is null
     *
     * @param id the id of the authorityFunctionalityDTO to save.
     * @param authorityFunctionalityDTO the authorityFunctionalityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorityFunctionalityDTO,
     * or with status {@code 400 (Bad Request)} if the authorityFunctionalityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the authorityFunctionalityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the authorityFunctionalityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/authority-functionalities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AuthorityFunctionalityDTO> partialUpdateAuthorityFunctionality(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AuthorityFunctionalityDTO authorityFunctionalityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AuthorityFunctionality partially : {}, {}", id, authorityFunctionalityDTO);
        if (authorityFunctionalityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authorityFunctionalityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authorityFunctionalityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AuthorityFunctionalityDTO> result = authorityFunctionalityService.partialUpdate(authorityFunctionalityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authorityFunctionalityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /authority-functionalities} : get all the authorityFunctionalities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authorityFunctionalities in body.
     */
    @GetMapping("/authority-functionalities")
    public ResponseEntity<List<AuthorityFunctionalityDTO>> getAllAuthorityFunctionalities(Pageable pageable) {
        log.debug("REST request to get a page of AuthorityFunctionalities");
        Page<AuthorityFunctionalityDTO> page = authorityFunctionalityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /authority-functionalities/:id} : get the "id" authorityFunctionality.
     *
     * @param id the id of the authorityFunctionalityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the authorityFunctionalityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/authority-functionalities/{id}")
    public ResponseEntity<AuthorityFunctionalityDTO> getAuthorityFunctionality(@PathVariable Long id) {
        log.debug("REST request to get AuthorityFunctionality : {}", id);
        Optional<AuthorityFunctionalityDTO> authorityFunctionalityDTO = authorityFunctionalityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(authorityFunctionalityDTO);
    }

    /**
     * {@code DELETE  /authority-functionalities/:id} : delete the "id" authorityFunctionality.
     *
     * @param id the id of the authorityFunctionalityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/authority-functionalities/{id}")
    public ResponseEntity<Void> deleteAuthorityFunctionality(@PathVariable Long id) {
        log.debug("REST request to delete AuthorityFunctionality : {}", id);
        authorityFunctionalityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
