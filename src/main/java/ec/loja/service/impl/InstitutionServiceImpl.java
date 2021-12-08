package ec.loja.service.impl;

import ec.loja.domain.Institution;
import ec.loja.repository.InstitutionRepository;
import ec.loja.service.InstitutionService;
import ec.loja.service.dto.InstitutionDTO;
import ec.loja.service.dto.InstitutionParkingDTO;
import ec.loja.service.dto.PlacesFreeDTO;
import ec.loja.service.mapper.InstitutionMapper;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Institution}.
 */
@Service
@Transactional
public class InstitutionServiceImpl implements InstitutionService {

    private final Logger log = LoggerFactory.getLogger(InstitutionServiceImpl.class);

    private final InstitutionRepository institutionRepository;

    private final InstitutionMapper institutionMapper;

    public InstitutionServiceImpl(InstitutionRepository institutionRepository, InstitutionMapper institutionMapper) {
        this.institutionRepository = institutionRepository;
        this.institutionMapper = institutionMapper;
    }

    @Override
    public InstitutionDTO save(InstitutionDTO institutionDTO) {
        log.debug("Request to save Institution : {}", institutionDTO);
        Institution institution = institutionMapper.toEntity(institutionDTO);
        institution = institutionRepository.save(institution);
        return institutionMapper.toDto(institution);
    }

    @Override
    public Optional<InstitutionDTO> partialUpdate(InstitutionDTO institutionDTO) {
        log.debug("Request to partially update Institution : {}", institutionDTO);

        return institutionRepository
            .findById(institutionDTO.getId())
            .map(existingInstitution -> {
                institutionMapper.partialUpdate(existingInstitution, institutionDTO);

                return existingInstitution;
            })
            .map(institutionRepository::save)
            .map(institutionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstitutionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Institutions");
        return institutionRepository.findAll(pageable).map(institutionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InstitutionDTO> findOne(Long id) {
        log.debug("Request to get Institution : {}", id);
        return institutionRepository.findById(id).map(institutionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Institution : {}", id);
        institutionRepository.deleteById(id);
    }

    @Override
    public Optional<InstitutionParkingDTO> getByUser(Long userid) {
        log.debug("Request to getByUser : {}", userid);
        return institutionRepository.getNameByCashier(userid);
    }

    @Override
    public String serialTicket(String sequenceName) {
        log.debug("Request to serialTicket : {}", sequenceName);
        Long value = institutionRepository.getSerialInstitution(sequenceName);
        return String.format("%05d", value);
    }

    @Override
    public List<PlacesFreeDTO> getFreeLocations(
        BigDecimal latitudeStart,
        BigDecimal latitudeEnd,
        BigDecimal longitudeStart,
        BigDecimal longitudeEnd,
        String statusPlace
    ) {
        log.debug("Request to getFreeLocations : {} {} {} {}", latitudeStart, latitudeEnd, longitudeStart, longitudeEnd);
        List<PlacesFreeDTO> placesfree = institutionRepository.getFreePlaces(
            latitudeStart,
            latitudeEnd,
            longitudeStart,
            longitudeEnd,
            statusPlace
        );
        return placesfree;
    }

    @Override
    public void createPlaces(int total, Long institutionid) {
        institutionRepository.createPlacesForInstitution(total, institutionid);
    }

    @Override
    public void createSequence(String sequencename) {
        institutionRepository.createSequence(sequencename);
    }
}
