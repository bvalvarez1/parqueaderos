package ec.loja.web.rest;

import ec.loja.repository.UserAuthorityInstitutionRepository;
import ec.loja.service.UserAuthorityInstitutionService;
import ec.loja.service.UserAuthorityService;
import ec.loja.service.dto.UserAuthorityDTO;
import ec.loja.service.dto.UserAuthorityInstitutionDTO;
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
 * REST controller for managing {@link ec.loja.domain.UserAuthorityInstitution}.
 */
@RestController
@RequestMapping("/api")
public class UserAuthorityInstitutionResource {

    private final Logger log = LoggerFactory.getLogger(UserAuthorityInstitutionResource.class);

    private static final String ENTITY_NAME = "userAuthorityInstitution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAuthorityInstitutionService userAuthorityInstitutionService;

    private final UserAuthorityInstitutionRepository userAuthorityInstitutionRepository;
    private final UserAuthorityService userAuthorityService;

    public UserAuthorityInstitutionResource(
        UserAuthorityInstitutionService userAuthorityInstitutionService,
        UserAuthorityInstitutionRepository userAuthorityInstitutionRepository,
        UserAuthorityService userAuthorityService
    ) {
        this.userAuthorityInstitutionService = userAuthorityInstitutionService;
        this.userAuthorityInstitutionRepository = userAuthorityInstitutionRepository;
        this.userAuthorityService = userAuthorityService;
    }

    /**
     * {@code POST  /user-authority-institutions} : Create a new userAuthorityInstitution.
     *
     * @param userAuthorityInstitutionDTO the userAuthorityInstitutionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAuthorityInstitutionDTO, or with status {@code 400 (Bad Request)} if the userAuthorityInstitution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-authority-institutions")
    public ResponseEntity<UserAuthorityInstitutionDTO> createUserAuthorityInstitution(
        @RequestBody UserAuthorityInstitutionDTO userAuthorityInstitutionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save UserAuthorityInstitution : {}", userAuthorityInstitutionDTO);
        if (userAuthorityInstitutionDTO.getId() != null) {
            throw new BadRequestAlertException("A new userAuthorityInstitution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserAuthorityInstitutionDTO result = userAuthorityInstitutionService.save(userAuthorityInstitutionDTO);
        return ResponseEntity
            .created(new URI("/api/user-authority-institutions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-authority-institutions/:id} : Updates an existing userAuthorityInstitution.
     *
     * @param id the id of the userAuthorityInstitutionDTO to save.
     * @param userAuthorityInstitutionDTO the userAuthorityInstitutionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAuthorityInstitutionDTO,
     * or with status {@code 400 (Bad Request)} if the userAuthorityInstitutionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAuthorityInstitutionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-authority-institutions/{id}")
    public ResponseEntity<UserAuthorityInstitutionDTO> updateUserAuthorityInstitution(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserAuthorityInstitutionDTO userAuthorityInstitutionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserAuthorityInstitution : {}, {}", id, userAuthorityInstitutionDTO);
        if (userAuthorityInstitutionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAuthorityInstitutionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAuthorityInstitutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserAuthorityInstitutionDTO result = userAuthorityInstitutionService.save(userAuthorityInstitutionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAuthorityInstitutionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-authority-institutions/:id} : Partial updates given fields of an existing userAuthorityInstitution, field will ignore if it is null
     *
     * @param id the id of the userAuthorityInstitutionDTO to save.
     * @param userAuthorityInstitutionDTO the userAuthorityInstitutionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAuthorityInstitutionDTO,
     * or with status {@code 400 (Bad Request)} if the userAuthorityInstitutionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userAuthorityInstitutionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAuthorityInstitutionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-authority-institutions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAuthorityInstitutionDTO> partialUpdateUserAuthorityInstitution(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserAuthorityInstitutionDTO userAuthorityInstitutionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserAuthorityInstitution partially : {}, {}", id, userAuthorityInstitutionDTO);
        if (userAuthorityInstitutionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAuthorityInstitutionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAuthorityInstitutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAuthorityInstitutionDTO> result = userAuthorityInstitutionService.partialUpdate(userAuthorityInstitutionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAuthorityInstitutionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-authority-institutions} : get all the userAuthorityInstitutions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAuthorityInstitutions in body.
     */
    @GetMapping("/user-authority-institutions")
    public ResponseEntity<List<UserAuthorityInstitutionDTO>> getAllUserAuthorityInstitutions(Pageable pageable) {
        log.debug("REST request to get a page of UserAuthorityInstitutions");
        Page<UserAuthorityInstitutionDTO> page = userAuthorityInstitutionService.findAll(pageable);

        for (UserAuthorityInstitutionDTO userAuthorityInstitutionDTO : page.getContent()) {
            UserAuthorityDTO dto = userAuthorityService.findOne(userAuthorityInstitutionDTO.getUserAuthority().getId()).get();
            userAuthorityInstitutionDTO.setUserAuthority(dto);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-authority-institutions/:id} : get the "id" userAuthorityInstitution.
     *
     * @param id the id of the userAuthorityInstitutionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAuthorityInstitutionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-authority-institutions/{id}")
    public ResponseEntity<UserAuthorityInstitutionDTO> getUserAuthorityInstitution(@PathVariable Long id) {
        log.debug("REST request to get UserAuthorityInstitution : {}", id);
        Optional<UserAuthorityInstitutionDTO> userAuthorityInstitutionDTO = userAuthorityInstitutionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAuthorityInstitutionDTO);
    }

    /**
     * {@code DELETE  /user-authority-institutions/:id} : delete the "id" userAuthorityInstitution.
     *
     * @param id the id of the userAuthorityInstitutionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-authority-institutions/{id}")
    public ResponseEntity<Void> deleteUserAuthorityInstitution(@PathVariable Long id) {
        log.debug("REST request to delete UserAuthorityInstitution : {}", id);
        userAuthorityInstitutionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
