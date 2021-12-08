package ec.loja.service.impl;

import ec.loja.domain.UserAuthorityInstitution;
import ec.loja.repository.UserAuthorityInstitutionRepository;
import ec.loja.service.UserAuthorityInstitutionService;
import ec.loja.service.dto.UserAuthorityInstitutionDTO;
import ec.loja.service.mapper.UserAuthorityInstitutionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserAuthorityInstitution}.
 */
@Service
@Transactional
public class UserAuthorityInstitutionServiceImpl implements UserAuthorityInstitutionService {

    private final Logger log = LoggerFactory.getLogger(UserAuthorityInstitutionServiceImpl.class);

    private final UserAuthorityInstitutionRepository userAuthorityInstitutionRepository;

    private final UserAuthorityInstitutionMapper userAuthorityInstitutionMapper;

    public UserAuthorityInstitutionServiceImpl(
        UserAuthorityInstitutionRepository userAuthorityInstitutionRepository,
        UserAuthorityInstitutionMapper userAuthorityInstitutionMapper
    ) {
        this.userAuthorityInstitutionRepository = userAuthorityInstitutionRepository;
        this.userAuthorityInstitutionMapper = userAuthorityInstitutionMapper;
    }

    @Override
    public UserAuthorityInstitutionDTO save(UserAuthorityInstitutionDTO userAuthorityInstitutionDTO) {
        log.debug("Request to save UserAuthorityInstitution : {}", userAuthorityInstitutionDTO);
        UserAuthorityInstitution userAuthorityInstitution = userAuthorityInstitutionMapper.toEntity(userAuthorityInstitutionDTO);
        userAuthorityInstitution = userAuthorityInstitutionRepository.save(userAuthorityInstitution);
        return userAuthorityInstitutionMapper.toDto(userAuthorityInstitution);
    }

    @Override
    public Optional<UserAuthorityInstitutionDTO> partialUpdate(UserAuthorityInstitutionDTO userAuthorityInstitutionDTO) {
        log.debug("Request to partially update UserAuthorityInstitution : {}", userAuthorityInstitutionDTO);

        return userAuthorityInstitutionRepository
            .findById(userAuthorityInstitutionDTO.getId())
            .map(existingUserAuthorityInstitution -> {
                userAuthorityInstitutionMapper.partialUpdate(existingUserAuthorityInstitution, userAuthorityInstitutionDTO);

                return existingUserAuthorityInstitution;
            })
            .map(userAuthorityInstitutionRepository::save)
            .map(userAuthorityInstitutionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAuthorityInstitutionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserAuthorityInstitutions");
        return userAuthorityInstitutionRepository.findAll(pageable).map(userAuthorityInstitutionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAuthorityInstitutionDTO> findOne(Long id) {
        log.debug("Request to get UserAuthorityInstitution : {}", id);
        return userAuthorityInstitutionRepository.findById(id).map(userAuthorityInstitutionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserAuthorityInstitution : {}", id);
        userAuthorityInstitutionRepository.deleteById(id);
    }
}
