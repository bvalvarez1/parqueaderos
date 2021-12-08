package ec.loja.web.rest;

import ec.loja.domain.ItemCatalogue;
import ec.loja.domain.RecordTicket;
import ec.loja.domain.User;
import ec.loja.repository.RecordTicketRepository;
import ec.loja.service.InstitutionService;
import ec.loja.service.ItemCatalogueService;
import ec.loja.service.PlaceService;
import ec.loja.service.RecordTicketService;
import ec.loja.service.TariffService;
import ec.loja.service.TariffVehicleTypeService;
import ec.loja.service.UserService;
import ec.loja.service.dto.InstitutionDTO;
import ec.loja.service.dto.ItemCatalogueDTO;
import ec.loja.service.dto.PlaceDTO;
import ec.loja.service.dto.RecordTicketDTO;
import ec.loja.service.dto.RecordTicketRequest;
import ec.loja.service.dto.TariffVehicleTypeDTO;
import ec.loja.service.dto.UserDTO;
import ec.loja.service.mapper.UserMapper;
import ec.loja.web.rest.errors.BadRequestAlertException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
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
 * REST controller for managing {@link ec.loja.domain.RecordTicket}.
 */
@RestController
@RequestMapping("/api")
public class RecordTicketResource {

    private final Logger log = LoggerFactory.getLogger(RecordTicketResource.class);

    private static final String ENTITY_NAME = "recordTicket";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private String EMITTED_STATUS = "EMITTED_TICKET";
    private String PAYED_STATUS = "PAYED_TICKET";
    private String VOID_STATUS = "VOID_TICKET";
    private String TICKET_STATUS = "TICKET_STATUS";
    private String STATUS_PLACE = "STATUS_PLACE";
    private String FREE_PLACE = "FREE_PLACE";
    private String BUSY_PLACE = "BUSY_PLACE";

    private final RecordTicketService recordTicketService;
    private final RecordTicketRepository recordTicketRepository;
    private final ItemCatalogueService itemCatalogService;
    private final InstitutionService institutionService;
    private final TariffVehicleTypeService tariffVehicleTypeService;
    private final PlaceService placeService;
    private final UserService userService;
    private final UserMapper userMapper;

    public RecordTicketResource(
        RecordTicketService recordTicketService,
        RecordTicketRepository recordTicketRepository,
        ItemCatalogueService itemCatalogService,
        TariffVehicleTypeService tariffVehicleTypeService,
        UserService userService,
        InstitutionService institutionService,
        PlaceService placeService,
        UserMapper userMapper
    ) {
        this.recordTicketService = recordTicketService;
        this.recordTicketRepository = recordTicketRepository;
        this.userService = userService;
        this.userMapper = userMapper;
        this.itemCatalogService = itemCatalogService;
        this.placeService = placeService;
        this.institutionService = institutionService;
        this.tariffVehicleTypeService = tariffVehicleTypeService;
    }

    /**
     * {@code POST  /record-tickets} : Create a new recordTicket.
     *
     * @param recordTicketDTO the recordTicketDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recordTicketDTO, or with status {@code 400 (Bad Request)} if the recordTicket has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/record-tickets")
    public ResponseEntity<RecordTicketDTO> createRecordTicket(@Valid @RequestBody RecordTicketDTO recordTicketDTO)
        throws URISyntaxException {
        log.debug("REST request to save RecordTicket : {}", recordTicketDTO);
        if (recordTicketDTO.getId() != null) {
            throw new BadRequestAlertException("A new recordTicket cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecordTicketDTO result = recordTicketService.save(recordTicketDTO);
        return ResponseEntity
            .created(new URI("/api/record-tickets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /record-tickets} : Create a new recordTicket.
     *
     * @param recordTicketDTO the recordTicketDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recordTicketDTO, or with status {@code 400 (Bad Request)} if the recordTicket has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/record-tickets/record-tickets-parking")
    public ResponseEntity<RecordTicketDTO> createRecordTicketParking(@Valid @RequestBody RecordTicketDTO recordTicketDTO)
        throws URISyntaxException {
        log.debug("REST request to save RecordTicket : {}", recordTicketDTO);

        if (recordTicketDTO.getId() != null) {
            throw new BadRequestAlertException("A new recordTicket cannot already have an ID", ENTITY_NAME, "idexists");
        }

        //fijar datos por defecto
        final Optional<User> isUser = userService.getUserWithAuthorities();
        if (!isUser.isPresent()) {
            log.error("User is not logged in");
        }

        final User user = isUser.get();

        Optional<User> opt = Optional.ofNullable(user);
        Optional<UserDTO> userdto = opt.map(userMapper::toDtoId);
        ItemCatalogueDTO emittedStatus = itemCatalogService.findByCodeAndCatalog(EMITTED_STATUS, TICKET_STATUS).get();
        ItemCatalogueDTO availablePlace = itemCatalogService.findByCodeAndCatalog(FREE_PLACE, STATUS_PLACE).get();
        ItemCatalogueDTO busyPlace = itemCatalogService.findByCodeAndCatalog(BUSY_PLACE, STATUS_PLACE).get();

        InstitutionDTO institution = institutionService.findOne(recordTicketDTO.getInstitution().getId()).get();
        PlaceDTO placedto = placeService.getAvailablePlace(institution.getId(), availablePlace.getId()).get();

        System.out.println("=====......."+institution);
        String ticketSerial = institutionService.serialTicket(institution.getSequencename() );

        ticketSerial = institution.getAcronym() + ticketSerial;

        recordTicketDTO.setInitDate(Instant.now());
        recordTicketDTO.setEmitter(userdto.get());
        recordTicketDTO.setStatus(emittedStatus);
        recordTicketDTO.setPlaceid(placedto);
        recordTicketDTO.setSequential(ticketSerial);

        RecordTicketDTO result = recordTicketService.save(recordTicketDTO);

        //cambiar el estado al lugar
        placedto.setStatus(busyPlace);
        placeService.save(placedto);

        return ResponseEntity
            .created(new URI("/api/record-tickets-parking/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/record-tickets/findByserialAndInstitution")
    public ResponseEntity<RecordTicketDTO> findBySerialAndInstitution(@Valid @RequestBody RecordTicketRequest request) {
        log.debug("REST request to save RecordTicket : {}", request);

        if (request.getSequential() == null) {
            throw new BadRequestAlertException("Falta serial para busqueda", ENTITY_NAME, "sequential");
        }

        Optional<RecordTicketDTO> recordTicketDTO_DB = recordTicketService.findBySerialAndInstitution(
            request.getSequential(),
            request.getInstitutionid()
        );

        //con el objeto retornado trabajar el tiempo de parqueo
        RecordTicketDTO ticket = recordTicketDTO_DB.get();
        ticket.setEndDate(Instant.now());

        Duration duration = Duration.between(ticket.getInitDate(), ticket.getEndDate());
        Instant parkingTime = Instant.ofEpochMilli(duration.toMillis());
        ticket.setParkingTime(parkingTime);
        System.out.println("DIAS " + duration.toDaysPart());
        System.out.println("Horas " + duration.toHoursPart());
        System.out.println("Minutos " + duration.toMinutesPart());
        System.out.println("Segundos " + duration.toSecondsPart());

        ticket.setDays(duration.toDaysPart());
        ticket.setHours(duration.toHoursPart());
        ticket.setMinutes(duration.toMinutesPart());
        ticket.setSeconds(duration.toSecondsPart());

        Optional<TariffVehicleTypeDTO> tarrifVehicledto = this.tariffVehicleTypeService.findOne(ticket.getTariffVehicle().getId());
        ticket.setTariffVehicle(tarrifVehicledto.get());
        System.out.println("====" + ticket.getTariffVehicle().getId());
        //calcular el valor por horas
        BigDecimal valuePerhour = tarrifVehicledto.get().getValue();
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalDay = BigDecimal.ZERO;
        BigDecimal iva = new BigDecimal(12); //iva poner como catalogo el actual
        BigDecimal taxes = BigDecimal.ZERO;

        System.out.println("Valor por hora" + valuePerhour);
        if (ticket.getDays() > 0) {
            BigDecimal hoursPerDay = new BigDecimal(24);
            BigDecimal days = new BigDecimal(ticket.getDays());
            totalDay = valuePerhour.multiply(hoursPerDay).multiply(days);
        }

        BigDecimal totalHour = BigDecimal.ZERO;
        if (ticket.getHours() > 0) {
            BigDecimal hours = new BigDecimal(ticket.getHours());
            totalHour = valuePerhour.multiply(hours);
        }

        // TODO aplicar el tiempo minimo de cobro
        BigDecimal totalMinutes = BigDecimal.ZERO;
        if (ticket.getMinutes() > 0) {
            totalMinutes = valuePerhour; // o se divide ...revisar configuracion
        }

        subtotal = subtotal.add(totalDay).add(totalHour).add(totalMinutes);
        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
        taxes = subtotal.divide(iva,2, RoundingMode.HALF_UP);
        taxes = taxes.setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = subtotal.add(taxes);

        ticket.setTaxableTotal(subtotal);
        ticket.setTaxes(taxes);
        ticket.setTotal(total);

        Optional<PlaceDTO> placedto = placeService.findOne(ticket.getPlaceid().getId());
        ticket.setPlaceid(placedto.get());

        return ResponseUtil.wrapOrNotFound(Optional.of(ticket));
    }

    @PostMapping("/record-tickets/payTicket")
    public ResponseEntity<RecordTicketDTO> payTicket(@Valid @RequestBody  RecordTicketDTO request) {
        log.debug("REST request to payTicket RecordTicket : {}", request);

        if (request.getId() == null) {
            throw new BadRequestAlertException("Falta id para actualizacion", ENTITY_NAME, "ID");
        }
        final Optional<User> isUser = userService.getUserWithAuthorities();
        if (!isUser.isPresent()) {
            log.error("User is not logged in");
        }

        final User user = isUser.get();
        Optional<ItemCatalogueDTO> payedStatus = itemCatalogService.findByCodeAndCatalog(PAYED_STATUS, TICKET_STATUS);
        
        //al ticket fijarle los valores de cobrador, fecha de cobro 
        request.setCollector(userService.converttoDTO(user).get()); 
        request.setEndDate(Instant.now());
        request.setStatus(payedStatus.get());
        
        RecordTicketDTO result = recordTicketService.save(request);

        //liberar el lugar o place ocupado
        Optional<PlaceDTO> placedto  = placeService.findOne(request.getPlaceid().getId());
        PlaceDTO placeDTO = placedto.get();
        Optional<ItemCatalogueDTO> freeStatus = itemCatalogService.findByCodeAndCatalog(FREE_PLACE, STATUS_PLACE);
        placeDTO.setStatus(freeStatus.get());
        placeService.save(placeDTO);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /record-tickets/:id} : Updates an existing recordTicket.
     *
     * @param id the id of the recordTicketDTO to save.
     * @param recordTicketDTO the recordTicketDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recordTicketDTO,
     * or with status {@code 400 (Bad Request)} if the recordTicketDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recordTicketDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/record-tickets/{id}")
    public ResponseEntity<RecordTicketDTO> updateRecordTicket(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RecordTicketDTO recordTicketDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RecordTicket : {}, {}", id, recordTicketDTO);
        if (recordTicketDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recordTicketDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recordTicketRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RecordTicketDTO result = recordTicketService.save(recordTicketDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recordTicketDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /record-tickets/:id} : Partial updates given fields of an existing recordTicket, field will ignore if it is null
     *
     * @param id the id of the recordTicketDTO to save.
     * @param recordTicketDTO the recordTicketDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recordTicketDTO,
     * or with status {@code 400 (Bad Request)} if the recordTicketDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recordTicketDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recordTicketDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/record-tickets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecordTicketDTO> partialUpdateRecordTicket(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RecordTicketDTO recordTicketDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RecordTicket partially : {}, {}", id, recordTicketDTO);
        if (recordTicketDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recordTicketDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recordTicketRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecordTicketDTO> result = recordTicketService.partialUpdate(recordTicketDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recordTicketDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /record-tickets} : get all the recordTickets.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recordTickets in body.
     */
    @GetMapping("/record-tickets")
    public ResponseEntity<List<RecordTicketDTO>> getAllRecordTickets(Pageable pageable) {
        log.debug("REST request to get a page of RecordTickets");
        Page<RecordTicketDTO> page = recordTicketService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /record-tickets/:id} : get the "id" recordTicket.
     *
     * @param id the id of the recordTicketDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recordTicketDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/record-tickets/{id}")
    public ResponseEntity<RecordTicketDTO> getRecordTicket(@PathVariable Long id) {
        log.debug("REST request to get RecordTicket : {}", id);
        Optional<RecordTicketDTO> recordTicketDTO = recordTicketService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recordTicketDTO);
    }

    /**
     * {@code DELETE  /record-tickets/:id} : delete the "id" recordTicket.
     *
     * @param id the id of the recordTicketDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/record-tickets/{id}")
    public ResponseEntity<Void> deleteRecordTicket(@PathVariable Long id) {
        log.debug("REST request to delete RecordTicket : {}", id);
        recordTicketService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }


    
    @PatchMapping(value = "/record-tickets/cancel/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecordTicketDTO> cancelRecordTicket(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RecordTicketDTO recordTicketDTO
    ) throws URISyntaxException {
        log.debug("REST request to  cancelRecordTicket: {}", id);
        if (id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
         
        if (!recordTicketRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        //cambiar el estado a anulado y liberar el puesto
        RecordTicketDTO recordDB = recordTicketService.findOne(id).get();
        ItemCatalogueDTO voidStatus = itemCatalogService.findByCodeAndCatalog(VOID_STATUS, TICKET_STATUS).get();
        recordDB.setStatus(voidStatus);
        Optional<RecordTicketDTO> result = recordTicketService.partialUpdate(recordDB);

        //desocupar el espacio de parqueamiento
        PlaceDTO placedto  = placeService.findOne(recordDB.getPlaceid().getId()).get();
        Optional<ItemCatalogueDTO> freeStatus = itemCatalogService.findByCodeAndCatalog(FREE_PLACE, STATUS_PLACE);
        placedto.setStatus(freeStatus.get());
        placeService.save(placedto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recordDB.getId().toString())
        );
    }
}
