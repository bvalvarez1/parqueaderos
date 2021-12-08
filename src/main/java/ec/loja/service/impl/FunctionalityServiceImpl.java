package ec.loja.service.impl;

import ec.loja.domain.Functionality;
import ec.loja.repository.FunctionalityRepository;
import ec.loja.service.FunctionalityService;
import ec.loja.service.dto.FunctionalityDTO;
import ec.loja.service.mapper.FunctionalityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Functionality}.
 */
@Service
@Transactional
public class FunctionalityServiceImpl implements FunctionalityService {

    private final Logger log = LoggerFactory.getLogger(FunctionalityServiceImpl.class);

    private final FunctionalityRepository functionalityRepository;

    private final FunctionalityMapper functionalityMapper;

    public FunctionalityServiceImpl(FunctionalityRepository functionalityRepository, FunctionalityMapper functionalityMapper) {
        this.functionalityRepository = functionalityRepository;
        this.functionalityMapper = functionalityMapper;
    }

    @Override
    public FunctionalityDTO save(FunctionalityDTO functionalityDTO) {
        log.debug("Request to save Functionality : {}", functionalityDTO);
        Functionality functionality = functionalityMapper.toEntity(functionalityDTO);
        functionality = functionalityRepository.save(functionality);
        return functionalityMapper.toDto(functionality);
    }

    @Override
    public Optional<FunctionalityDTO> partialUpdate(FunctionalityDTO functionalityDTO) {
        log.debug("Request to partially update Functionality : {}", functionalityDTO);

        return functionalityRepository
            .findById(functionalityDTO.getId())
            .map(existingFunctionality -> {
                functionalityMapper.partialUpdate(existingFunctionality, functionalityDTO);

                return existingFunctionality;
            })
            .map(functionalityRepository::save)
            .map(functionalityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FunctionalityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Functionalities");
        return functionalityRepository.findAll(pageable).map(functionalityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FunctionalityDTO> findOne(Long id) {
        log.debug("Request to get Functionality : {}", id);
        return functionalityRepository.findById(id).map(functionalityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Functionality : {}", id);
        functionalityRepository.deleteById(id);
    }
}
