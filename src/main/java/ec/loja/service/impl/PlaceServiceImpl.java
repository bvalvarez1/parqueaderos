package ec.loja.service.impl;

import ec.loja.domain.Place;
import ec.loja.repository.PlaceRepository;
import ec.loja.service.PlaceService;
import ec.loja.service.dto.PlaceDTO;
import ec.loja.service.dto.PlaceStatusDTO;
import ec.loja.service.dto.PlaceTicketsDTO;
import ec.loja.service.mapper.PlaceMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Place}.
 */
@Service
@Transactional
public class PlaceServiceImpl implements PlaceService {

    private final Logger log = LoggerFactory.getLogger(PlaceServiceImpl.class);

    private final PlaceRepository placeRepository;

    private final PlaceMapper placeMapper;

    public PlaceServiceImpl(PlaceRepository placeRepository, PlaceMapper placeMapper) {
        this.placeRepository = placeRepository;
        this.placeMapper = placeMapper;
    }

    @Override
    public PlaceDTO save(PlaceDTO placeDTO) {
        log.debug("Request to save Place : {}", placeDTO);
        Place place = placeMapper.toEntity(placeDTO);
        place = placeRepository.save(place);
        return placeMapper.toDto(place);
    }

    @Override
    public Optional<PlaceDTO> partialUpdate(PlaceDTO placeDTO) {
        log.debug("Request to partially update Place : {}", placeDTO);

        return placeRepository
            .findById(placeDTO.getId())
            .map(existingPlace -> {
                placeMapper.partialUpdate(existingPlace, placeDTO);

                return existingPlace;
            })
            .map(placeRepository::save)
            .map(placeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlaceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Places");
        return placeRepository.findAll(pageable).map(placeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlaceDTO> findOne(Long id) {
        log.debug("Request to get Place : {}", id);
        return placeRepository.findById(id).map(placeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Place : {}", id);
        placeRepository.deleteById(id);
    }

    @Override
    public List<PlaceTicketsDTO> getPlaces(Long institutionid) {
        log.debug("Request to getPlaces: {}", institutionid);
        return placeRepository.getPlaces(institutionid);
    }

    @Override
    public List<PlaceStatusDTO> getPlacesByStatus(String catalogPlace, Long userid) {
        log.debug("Request to getPlaces: {} {}", catalogPlace, userid);
        return placeRepository.getStatusPlace(catalogPlace, userid);
    }

    @Override
    public Optional<PlaceDTO> getAvailablePlace(Long institutionid, Long statusid) {
        log.debug("Request to getAvailablePlace: {} {}", institutionid, statusid);
        List<Place> places = placeRepository.getPlace(institutionid, statusid);
        for (Place place : places) {
            System.out.println("--->" + place.getNumber());
        }
        return (!places.isEmpty()) ? Optional.of(places.get(0)).map(placeMapper::toDto) : Optional.ofNullable(null);
    }
}
