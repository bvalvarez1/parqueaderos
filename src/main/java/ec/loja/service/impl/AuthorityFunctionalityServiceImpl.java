package ec.loja.service.impl;

import ec.loja.domain.AuthorityFunctionality;
import ec.loja.repository.AuthorityFunctionalityRepository;
import ec.loja.service.AuthorityFunctionalityService;
import ec.loja.service.dto.AuthorityFunctionalityDTO;
import ec.loja.service.mapper.AuthorityFunctionalityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AuthorityFunctionality}.
 */
@Service
@Transactional
public class AuthorityFunctionalityServiceImpl implements AuthorityFunctionalityService {

    private final Logger log = LoggerFactory.getLogger(AuthorityFunctionalityServiceImpl.class);

    private final AuthorityFunctionalityRepository authorityFunctionalityRepository;

    private final AuthorityFunctionalityMapper authorityFunctionalityMapper;

    public AuthorityFunctionalityServiceImpl(
        AuthorityFunctionalityRepository authorityFunctionalityRepository,
        AuthorityFunctionalityMapper authorityFunctionalityMapper
    ) {
        this.authorityFunctionalityRepository = authorityFunctionalityRepository;
        this.authorityFunctionalityMapper = authorityFunctionalityMapper;
    }

    @Override
    public AuthorityFunctionalityDTO save(AuthorityFunctionalityDTO authorityFunctionalityDTO) {
        log.debug("Request to save AuthorityFunctionality : {}", authorityFunctionalityDTO);
        AuthorityFunctionality authorityFunctionality = authorityFunctionalityMapper.toEntity(authorityFunctionalityDTO);
        authorityFunctionality = authorityFunctionalityRepository.save(authorityFunctionality);
        return authorityFunctionalityMapper.toDto(authorityFunctionality);
    }

    @Override
    public Optional<AuthorityFunctionalityDTO> partialUpdate(AuthorityFunctionalityDTO authorityFunctionalityDTO) {
        log.debug("Request to partially update AuthorityFunctionality : {}", authorityFunctionalityDTO);

        return authorityFunctionalityRepository
            .findById(authorityFunctionalityDTO.getId())
            .map(existingAuthorityFunctionality -> {
                authorityFunctionalityMapper.partialUpdate(existingAuthorityFunctionality, authorityFunctionalityDTO);

                return existingAuthorityFunctionality;
            })
            .map(authorityFunctionalityRepository::save)
            .map(authorityFunctionalityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuthorityFunctionalityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AuthorityFunctionalities");
        return authorityFunctionalityRepository.findAll(pageable).map(authorityFunctionalityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthorityFunctionalityDTO> findOne(Long id) {
        log.debug("Request to get AuthorityFunctionality : {}", id);
        return authorityFunctionalityRepository.findById(id).map(authorityFunctionalityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AuthorityFunctionality : {}", id);
        authorityFunctionalityRepository.deleteById(id);
    }
}
