package ec.loja.web.rest;

import ec.loja.repository.UserAuthorityRepository;
import ec.loja.service.UserAuthorityService;
import ec.loja.service.UserService;
import ec.loja.service.dto.JHIUserAuthorityDTO;
import ec.loja.service.dto.UserAuthorityDTO;
import ec.loja.service.dto.UserDTO;
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
 * REST controller for managing {@link ec.loja.domain.UserAuthority}.
 */
@RestController
@RequestMapping("/api")
public class UserAuthorityResource {

    private final Logger log = LoggerFactory.getLogger(UserAuthorityResource.class);

    private static final String ENTITY_NAME = "userAuthority";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAuthorityService userAuthorityService;

    private final UserAuthorityRepository userAuthorityRepository;

    private final UserService userService;

    public UserAuthorityResource(
        UserAuthorityService userAuthorityService,
        UserAuthorityRepository userAuthorityRepository,
        UserService userService
    ) {
        this.userAuthorityService = userAuthorityService;
        this.userAuthorityRepository = userAuthorityRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /user-authorities} : Create a new userAuthority.
     *
     * @param userAuthorityDTO the userAuthorityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAuthorityDTO, or with status {@code 400 (Bad Request)} if the userAuthority has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-authorities")
    public ResponseEntity<UserAuthorityDTO> createUserAuthority(@RequestBody UserAuthorityDTO userAuthorityDTO) throws URISyntaxException {
        log.debug("REST request to save UserAuthority : {}", userAuthorityDTO);
        if (userAuthorityDTO.getId() != null) {
            throw new BadRequestAlertException("A new userAuthority cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserAuthorityDTO result = userAuthorityService.save(userAuthorityDTO);
        return ResponseEntity
            .created(new URI("/api/user-authorities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-authorities/:id} : Updates an existing userAuthority.
     *
     * @param id the id of the userAuthorityDTO to save.
     * @param userAuthorityDTO the userAuthorityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAuthorityDTO,
     * or with status {@code 400 (Bad Request)} if the userAuthorityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAuthorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-authorities/{id}")
    public ResponseEntity<UserAuthorityDTO> updateUserAuthority(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserAuthorityDTO userAuthorityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserAuthority : {}, {}", id, userAuthorityDTO);
        if (userAuthorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAuthorityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAuthorityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserAuthorityDTO result = userAuthorityService.save(userAuthorityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAuthorityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-authorities/:id} : Partial updates given fields of an existing userAuthority, field will ignore if it is null
     *
     * @param id the id of the userAuthorityDTO to save.
     * @param userAuthorityDTO the userAuthorityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAuthorityDTO,
     * or with status {@code 400 (Bad Request)} if the userAuthorityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userAuthorityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAuthorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-authorities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAuthorityDTO> partialUpdateUserAuthority(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserAuthorityDTO userAuthorityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserAuthority partially : {}, {}", id, userAuthorityDTO);
        if (userAuthorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAuthorityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAuthorityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAuthorityDTO> result = userAuthorityService.partialUpdate(userAuthorityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAuthorityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-authorities} : get all the userAuthorities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAuthorities in body.
     */
    @GetMapping("/user-authorities")
    public ResponseEntity<List<UserAuthorityDTO>> getAllUserAuthorities(Pageable pageable) {
        log.debug("REST request to get a page of UserAuthorities");
        Page<UserAuthorityDTO> page = userAuthorityService.findAll(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-authorities/:id} : get the "id" userAuthority.
     *
     * @param id the id of the userAuthorityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAuthorityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-authorities/{id}")
    public ResponseEntity<UserAuthorityDTO> getUserAuthority(@PathVariable Long id) {
        log.debug("REST request to get UserAuthority : {}", id);
        Optional<UserAuthorityDTO> userAuthorityDTO = userAuthorityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAuthorityDTO);
    }

    /**
     * {@code DELETE  /user-authorities/:id} : delete the "id" userAuthority.
     *
     * @param id the id of the userAuthorityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-authorities/{id}")
    public ResponseEntity<Void> deleteUserAuthority(@PathVariable Long id) {
        log.debug("REST request to delete UserAuthority : {}", id);
        userAuthorityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/user-authorities/findByUserid")
    public ResponseEntity<UserAuthorityDTO> findByUserid(@RequestBody Long userid) throws URISyntaxException {
        log.debug("REST request to findByUserid UserAuthority : {}", userid);
        if (userid == null) {
            throw new BadRequestAlertException("A new userAuthority cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JHIUserAuthorityDTO jhiuserauthority = userAuthorityService.findByUserId(userid);
        UserDTO userDTO = userService.findOne(userid).get();

        UserAuthorityDTO uadto = new UserAuthorityDTO();
        uadto.setAuthority(jhiuserauthority.getAuthorityName());
        uadto.setActive(Boolean.TRUE);
        uadto.setUser(userDTO);

        uadto = userAuthorityService.save(uadto);

        return ResponseEntity
            .created(new URI("/api/user-authorities/findByUserid" + uadto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, uadto.getId().toString()))
            .body(uadto);
    }
}
