package ec.loja.service.impl;

import ec.loja.domain.SystemParameterInstitution;
import ec.loja.repository.SystemParameterInstitutionRepository;
import ec.loja.service.SystemParameterInstitutionService;
import ec.loja.service.dto.SystemParameterInstitutionDTO;
import ec.loja.service.mapper.SystemParameterInstitutionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SystemParameterInstitution}.
 */
@Service
@Transactional
public class SystemParameterInstitutionServiceImpl implements SystemParameterInstitutionService {

    private final Logger log = LoggerFactory.getLogger(SystemParameterInstitutionServiceImpl.class);

    private final SystemParameterInstitutionRepository systemParameterInstitutionRepository;

    private final SystemParameterInstitutionMapper systemParameterInstitutionMapper;

    public SystemParameterInstitutionServiceImpl(
        SystemParameterInstitutionRepository systemParameterInstitutionRepository,
        SystemParameterInstitutionMapper systemParameterInstitutionMapper
    ) {
        this.systemParameterInstitutionRepository = systemParameterInstitutionRepository;
        this.systemParameterInstitutionMapper = systemParameterInstitutionMapper;
    }

    @Override
    public SystemParameterInstitutionDTO save(SystemParameterInstitutionDTO systemParameterInstitutionDTO) {
        log.debug("Request to save SystemParameterInstitution : {}", systemParameterInstitutionDTO);
        SystemParameterInstitution systemParameterInstitution = systemParameterInstitutionMapper.toEntity(systemParameterInstitutionDTO);
        systemParameterInstitution = systemParameterInstitutionRepository.save(systemParameterInstitution);
        return systemParameterInstitutionMapper.toDto(systemParameterInstitution);
    }

    @Override
    public Optional<SystemParameterInstitutionDTO> partialUpdate(SystemParameterInstitutionDTO systemParameterInstitutionDTO) {
        log.debug("Request to partially update SystemParameterInstitution : {}", systemParameterInstitutionDTO);

        return systemParameterInstitutionRepository
            .findById(systemParameterInstitutionDTO.getId())
            .map(existingSystemParameterInstitution -> {
                systemParameterInstitutionMapper.partialUpdate(existingSystemParameterInstitution, systemParameterInstitutionDTO);

                return existingSystemParameterInstitution;
            })
            .map(systemParameterInstitutionRepository::save)
            .map(systemParameterInstitutionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemParameterInstitutionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SystemParameterInstitutions");
        return systemParameterInstitutionRepository.findAll(pageable).map(systemParameterInstitutionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemParameterInstitutionDTO> findOne(Long id) {
        log.debug("Request to get SystemParameterInstitution : {}", id);
        return systemParameterInstitutionRepository.findById(id).map(systemParameterInstitutionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SystemParameterInstitution : {}", id);
        systemParameterInstitutionRepository.deleteById(id);
    }
}
