package ec.loja.service.impl;

import ec.loja.domain.SystemParameters;
import ec.loja.repository.SystemParametersRepository;
import ec.loja.service.SystemParametersService;
import ec.loja.service.dto.SystemParametersDTO;
import ec.loja.service.mapper.SystemParametersMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SystemParameters}.
 */
@Service
@Transactional
public class SystemParametersServiceImpl implements SystemParametersService {

    private final Logger log = LoggerFactory.getLogger(SystemParametersServiceImpl.class);

    private final SystemParametersRepository systemParametersRepository;

    private final SystemParametersMapper systemParametersMapper;

    public SystemParametersServiceImpl(
        SystemParametersRepository systemParametersRepository,
        SystemParametersMapper systemParametersMapper
    ) {
        this.systemParametersRepository = systemParametersRepository;
        this.systemParametersMapper = systemParametersMapper;
    }

    @Override
    public SystemParametersDTO save(SystemParametersDTO systemParametersDTO) {
        log.debug("Request to save SystemParameters : {}", systemParametersDTO);
        SystemParameters systemParameters = systemParametersMapper.toEntity(systemParametersDTO);
        systemParameters = systemParametersRepository.save(systemParameters);
        return systemParametersMapper.toDto(systemParameters);
    }

    @Override
    public Optional<SystemParametersDTO> partialUpdate(SystemParametersDTO systemParametersDTO) {
        log.debug("Request to partially update SystemParameters : {}", systemParametersDTO);

        return systemParametersRepository
            .findById(systemParametersDTO.getId())
            .map(existingSystemParameters -> {
                systemParametersMapper.partialUpdate(existingSystemParameters, systemParametersDTO);

                return existingSystemParameters;
            })
            .map(systemParametersRepository::save)
            .map(systemParametersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemParametersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SystemParameters");
        return systemParametersRepository.findAll(pageable).map(systemParametersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemParametersDTO> findOne(Long id) {
        log.debug("Request to get SystemParameters : {}", id);
        return systemParametersRepository.findById(id).map(systemParametersMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SystemParameters : {}", id);
        systemParametersRepository.deleteById(id);
    }
}
