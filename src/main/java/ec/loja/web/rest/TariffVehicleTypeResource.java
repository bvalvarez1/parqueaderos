package ec.loja.web.rest;

import ec.loja.repository.TariffVehicleTypeRepository;
import ec.loja.service.TariffVehicleTypeService;
import ec.loja.service.dto.TariffVehicleTypeDTO;
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
 * REST controller for managing {@link ec.loja.domain.TariffVehicleType}.
 */
@RestController
@RequestMapping("/api")
public class TariffVehicleTypeResource {

    private final Logger log = LoggerFactory.getLogger(TariffVehicleTypeResource.class);

    private static final String ENTITY_NAME = "tariffVehicleType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TariffVehicleTypeService tariffVehicleTypeService;

    private final TariffVehicleTypeRepository tariffVehicleTypeRepository;

    public TariffVehicleTypeResource(
        TariffVehicleTypeService tariffVehicleTypeService,
        TariffVehicleTypeRepository tariffVehicleTypeRepository
    ) {
        this.tariffVehicleTypeService = tariffVehicleTypeService;
        this.tariffVehicleTypeRepository = tariffVehicleTypeRepository;
    }

    /**
     * {@code POST  /tariff-vehicle-types} : Create a new tariffVehicleType.
     *
     * @param tariffVehicleTypeDTO the tariffVehicleTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tariffVehicleTypeDTO, or with status {@code 400 (Bad Request)} if the tariffVehicleType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tariff-vehicle-types")
    public ResponseEntity<TariffVehicleTypeDTO> createTariffVehicleType(@Valid @RequestBody TariffVehicleTypeDTO tariffVehicleTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save TariffVehicleType : {}", tariffVehicleTypeDTO);
        if (tariffVehicleTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new tariffVehicleType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TariffVehicleTypeDTO result = tariffVehicleTypeService.save(tariffVehicleTypeDTO);
        return ResponseEntity
            .created(new URI("/api/tariff-vehicle-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tariff-vehicle-types/:id} : Updates an existing tariffVehicleType.
     *
     * @param id the id of the tariffVehicleTypeDTO to save.
     * @param tariffVehicleTypeDTO the tariffVehicleTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tariffVehicleTypeDTO,
     * or with status {@code 400 (Bad Request)} if the tariffVehicleTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tariffVehicleTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tariff-vehicle-types/{id}")
    public ResponseEntity<TariffVehicleTypeDTO> updateTariffVehicleType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TariffVehicleTypeDTO tariffVehicleTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TariffVehicleType : {}, {}", id, tariffVehicleTypeDTO);
        if (tariffVehicleTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tariffVehicleTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tariffVehicleTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TariffVehicleTypeDTO result = tariffVehicleTypeService.save(tariffVehicleTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tariffVehicleTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tariff-vehicle-types/:id} : Partial updates given fields of an existing tariffVehicleType, field will ignore if it is null
     *
     * @param id the id of the tariffVehicleTypeDTO to save.
     * @param tariffVehicleTypeDTO the tariffVehicleTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tariffVehicleTypeDTO,
     * or with status {@code 400 (Bad Request)} if the tariffVehicleTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tariffVehicleTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tariffVehicleTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tariff-vehicle-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TariffVehicleTypeDTO> partialUpdateTariffVehicleType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TariffVehicleTypeDTO tariffVehicleTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TariffVehicleType partially : {}, {}", id, tariffVehicleTypeDTO);
        if (tariffVehicleTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tariffVehicleTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tariffVehicleTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TariffVehicleTypeDTO> result = tariffVehicleTypeService.partialUpdate(tariffVehicleTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tariffVehicleTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tariff-vehicle-types} : get all the tariffVehicleTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tariffVehicleTypes in body.
     */
    @GetMapping("/tariff-vehicle-types")
    public ResponseEntity<List<TariffVehicleTypeDTO>> getAllTariffVehicleTypes(Pageable pageable) {
        log.debug("REST request to get a page of TariffVehicleTypes");
        Page<TariffVehicleTypeDTO> page = tariffVehicleTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tariff-vehicle-types/:id} : get the "id" tariffVehicleType.
     *
     * @param id the id of the tariffVehicleTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tariffVehicleTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tariff-vehicle-types/{id}")
    public ResponseEntity<TariffVehicleTypeDTO> getTariffVehicleType(@PathVariable Long id) {
        log.debug("REST request to get TariffVehicleType : {}", id);
        Optional<TariffVehicleTypeDTO> tariffVehicleTypeDTO = tariffVehicleTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tariffVehicleTypeDTO);
    }

    /**
     * {@code DELETE  /tariff-vehicle-types/:id} : delete the "id" tariffVehicleType.
     *
     * @param id the id of the tariffVehicleTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tariff-vehicle-types/{id}")
    public ResponseEntity<Void> deleteTariffVehicleType(@PathVariable Long id) {
        log.debug("REST request to delete TariffVehicleType : {}", id);
        tariffVehicleTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
