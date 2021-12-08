package ec.loja.service.impl;

import ec.loja.domain.Horary;
import ec.loja.repository.HoraryRepository;
import ec.loja.service.HoraryService;
import ec.loja.service.dto.HoraryDTO;
import ec.loja.service.mapper.HoraryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Horary}.
 */
@Service
@Transactional
public class HoraryServiceImpl implements HoraryService {

    private final Logger log = LoggerFactory.getLogger(HoraryServiceImpl.class);

    private final HoraryRepository horaryRepository;

    private final HoraryMapper horaryMapper;

    public HoraryServiceImpl(HoraryRepository horaryRepository, HoraryMapper horaryMapper) {
        this.horaryRepository = horaryRepository;
        this.horaryMapper = horaryMapper;
    }

    @Override
    public HoraryDTO save(HoraryDTO horaryDTO) {
        log.debug("Request to save Horary : {}", horaryDTO);
        Horary horary = horaryMapper.toEntity(horaryDTO);
        horary = horaryRepository.save(horary);
        return horaryMapper.toDto(horary);
    }

    @Override
    public Optional<HoraryDTO> partialUpdate(HoraryDTO horaryDTO) {
        log.debug("Request to partially update Horary : {}", horaryDTO);

        return horaryRepository
            .findById(horaryDTO.getId())
            .map(existingHorary -> {
                horaryMapper.partialUpdate(existingHorary, horaryDTO);

                return existingHorary;
            })
            .map(horaryRepository::save)
            .map(horaryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HoraryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Horaries");
        return horaryRepository.findAll(pageable).map(horaryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HoraryDTO> findOne(Long id) {
        log.debug("Request to get Horary : {}", id);
        return horaryRepository.findById(id).map(horaryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Horary : {}", id);
        horaryRepository.deleteById(id);
    }
}
