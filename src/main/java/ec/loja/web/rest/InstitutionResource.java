package ec.loja.web.rest;

import ec.loja.domain.TariffVehicleType;
import ec.loja.domain.User;
import ec.loja.repository.InstitutionRepository;
import ec.loja.service.InstitutionService;
import ec.loja.service.ItemCatalogueService;
import ec.loja.service.TariffVehicleTypeService;
import ec.loja.service.UserService;
import ec.loja.service.dto.InstitutionDTO;
import ec.loja.service.dto.InstitutionParkingDTO;
import ec.loja.service.dto.InstitutionTarriffDTO;
import ec.loja.service.dto.ItemCatalogueDTO;
import ec.loja.service.dto.PlaceDTO;
import ec.loja.service.dto.PlacesFreeDTO;
import ec.loja.service.dto.PositionDTO;
import ec.loja.service.dto.TariffVehicleTypeDTO;
import ec.loja.web.rest.errors.BadRequestAlertException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.Normalizer;
import java.time.LocalDate;
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
 * REST controller for managing {@link ec.loja.domain.Institution}.
 */
@RestController
@RequestMapping("/api")
public class InstitutionResource {

    private final Logger log = LoggerFactory.getLogger(InstitutionResource.class);

    private static final String ENTITY_NAME = "institution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstitutionService institutionService;
    private final UserService userService;
    private final TariffVehicleTypeService tariffVehicleTypeService;
    private final ItemCatalogueService itemCatalogueService;

    private final InstitutionRepository institutionRepository;
    private String STATUS_PLACE = "STATUS_PLACE";
    private String FREE_PLACE = "FREE_PLACE";
    private String BUSY_PLACE = "BUSY_PLACE";

    public InstitutionResource(
        InstitutionService institutionService,
        InstitutionRepository institutionRepository,
        UserService userService,
        TariffVehicleTypeService tariffVehicleTypeService,
        ItemCatalogueService itemCatalogueService
    ) {
        this.institutionService = institutionService;
        this.institutionRepository = institutionRepository;
        this.userService = userService;
        this.tariffVehicleTypeService = tariffVehicleTypeService;
        this.itemCatalogueService = itemCatalogueService;
    }

    /**
     * {@code POST  /institutions} : Create a new institution.
     *
     * @param institutionDTO the institutionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new institutionDTO, or with status {@code 400 (Bad Request)} if the institution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/institutions")
    public ResponseEntity<InstitutionDTO> createInstitution(@Valid @RequestBody InstitutionDTO institutionDTO) throws URISyntaxException {
        log.debug("REST request to save Institution : {}", institutionDTO);
        if (institutionDTO.getId() != null) {
            throw new BadRequestAlertException("A new institution cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String formatted = limpiarAcentos(institutionDTO.getName());
        institutionDTO.setSequencename("seq_" + formatted);

        System.out.println("=====*** " + institutionDTO.getSequencename());
        System.out.println("=====*** " + institutionDTO.toString());

        InstitutionDTO result = institutionService.save(institutionDTO);
        //crear los lugares
        institutionService.createPlaces(result.getPlacesNumber(), result.getId());

        //crear la secuencia
        institutionService.createSequence(result.getSequencename());

        return ResponseEntity
            .created(new URI("/api/institutions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    public static String limpiarAcentos(String cadena) {
        String limpio = null;
        cadena = cadena.toLowerCase().replace(" ", "");
        if (cadena != null) {
            String valor = cadena;
            // Normalizar texto para eliminar acentos, dieresis, cedillas y tildes
            limpio = Normalizer.normalize(valor, Normalizer.Form.NFD);
            // Quitar caracteres no ASCII excepto la enie, interrogacion que abre, exclamacion que abre, grados, U con dieresis.
            limpio = limpio.replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "");
            // Regresar a la forma compuesta, para poder comparar la enie con la tabla de valores
            limpio = Normalizer.normalize(limpio, Normalizer.Form.NFC);
        }

        System.out.println("PUTA CADENA=========" + limpio);
        return limpio;
    }

    /**
     * {@code PUT  /institutions/:id} : Updates an existing institution.
     *
     * @param id the id of the institutionDTO to save.
     * @param institutionDTO the institutionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated institutionDTO,
     * or with status {@code 400 (Bad Request)} if the institutionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the institutionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/institutions/{id}")
    public ResponseEntity<InstitutionDTO> updateInstitution(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InstitutionDTO institutionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Institution : {}, {}", id, institutionDTO);
        if (institutionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, institutionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!institutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InstitutionDTO result = institutionService.save(institutionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, institutionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /institutions/:id} : Partial updates given fields of an existing institution, field will ignore if it is null
     *
     * @param id the id of the institutionDTO to save.
     * @param institutionDTO the institutionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated institutionDTO,
     * or with status {@code 400 (Bad Request)} if the institutionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the institutionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the institutionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/institutions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InstitutionDTO> partialUpdateInstitution(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InstitutionDTO institutionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Institution partially : {}, {}", id, institutionDTO);
        if (institutionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, institutionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!institutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InstitutionDTO> result = institutionService.partialUpdate(institutionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, institutionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /institutions} : get all the institutions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of institutions in body.
     */
    @GetMapping("/institutions")
    public ResponseEntity<List<InstitutionDTO>> getAllInstitutions(Pageable pageable) {
        log.debug("REST request to get a page of Institutions");
        Page<InstitutionDTO> page = institutionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /institutions/:id} : get the "id" institution.
     *
     * @param id the id of the institutionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the institutionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/institutions/{id}")
    public ResponseEntity<InstitutionDTO> getInstitution(@PathVariable Long id) {
        log.debug("REST request to get Institution : {}", id);
        Optional<InstitutionDTO> institutionDTO = institutionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(institutionDTO);
    }

    /**
     * {@code DELETE  /institutions/:id} : delete the "id" institution.
     *
     * @param id the id of the institutionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/institutions/{id}")
    public ResponseEntity<Void> deleteInstitution(@PathVariable Long id) {
        log.debug("REST request to delete Institution : {}", id);
        institutionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping(value = "/institutions/byUser")
    public ResponseEntity<InstitutionTarriffDTO> getByLoggedUser() throws URISyntaxException {
        log.debug("REST request togetByLoggedUser");
        final Optional<User> isUser = userService.getUserWithAuthorities();
        if (!isUser.isPresent()) {
            log.error("User is not logged in");
        }

        final User user = isUser.get();

        Optional<InstitutionParkingDTO> result = institutionService.getByUser(user.getId());
        //una vez obtenia la institution obtener las tarifas de acuedo al tipo
        List<TariffVehicleType> list = tariffVehicleTypeService.getTarrifVehicleByInstitution(result.get().getId(), LocalDate.now());

        InstitutionTarriffDTO data = new InstitutionTarriffDTO();
        data.setInstitution(result.get());
        data.setTarrifs(list);

        return ResponseUtil.wrapOrNotFound(
            Optional.ofNullable(data),
            HeaderUtil.createAlert(applicationName, ENTITY_NAME, result.get().getId().toString())
        );
    }

    /**
     *
     * @param latitude
     * @param longitude
     * @return
     * @throws URISyntaxException
     */
    @PostMapping(value = "/institutions/nearPosition")
    public ResponseEntity<List<PlacesFreeDTO>> getFreePlacesByUserPosition(@RequestBody PositionDTO position) throws URISyntaxException {
        log.debug("REST request getFreePlacesByUserPosition: {} {} ", position.getLatitude(), position.getLongitude());

        BigDecimal radius = new BigDecimal("0.01");
        BigDecimal latitudeStart = BigDecimal.ZERO;
        BigDecimal latitudeEnd = BigDecimal.ZERO;
        BigDecimal longitudeStart = BigDecimal.ZERO;
        BigDecimal longitudeEnd = BigDecimal.ZERO;

        latitudeStart = position.getLatitude().subtract(radius);
        latitudeEnd = position.getLatitude().add(radius);

        longitudeStart = position.getLongitude().subtract(radius);
        longitudeEnd = position.getLongitude().add(radius);

        Optional<ItemCatalogueDTO> freePlace = itemCatalogueService.findByCodeAndCatalog(FREE_PLACE, STATUS_PLACE);

        List<PlacesFreeDTO> places = institutionRepository.getFreePlaces(
            latitudeStart,
            latitudeEnd,
            longitudeStart,
            longitudeEnd,
            freePlace.get().getCode()
        );
        return ResponseEntity.ok().body(places);
    }
}
