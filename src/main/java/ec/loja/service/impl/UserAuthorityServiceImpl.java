package ec.loja.service.impl;

import ec.loja.domain.User;
import ec.loja.domain.UserAuthority;
import ec.loja.repository.UserAuthorityRepository;
import ec.loja.service.UserAuthorityService;
import ec.loja.service.dto.JHIUserAuthorityDTO;
import ec.loja.service.dto.UserAuthorityDTO;
import ec.loja.service.mapper.UserAuthorityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserAuthority}.
 */
@Service
@Transactional
public class UserAuthorityServiceImpl implements UserAuthorityService {

    private final Logger log = LoggerFactory.getLogger(UserAuthorityServiceImpl.class);

    private final UserAuthorityRepository userAuthorityRepository;

    private final UserAuthorityMapper userAuthorityMapper;

    public UserAuthorityServiceImpl(UserAuthorityRepository userAuthorityRepository, UserAuthorityMapper userAuthorityMapper) {
        this.userAuthorityRepository = userAuthorityRepository;
        this.userAuthorityMapper = userAuthorityMapper;
    }

    @Override
    public UserAuthorityDTO save(UserAuthorityDTO userAuthorityDTO) {
        log.debug("Request to save UserAuthority : {}", userAuthorityDTO);
        UserAuthority userAuthority = userAuthorityMapper.toEntity(userAuthorityDTO);
        userAuthority = userAuthorityRepository.save(userAuthority);
        return userAuthorityMapper.toDto(userAuthority);
    }

    @Override
    public Optional<UserAuthorityDTO> partialUpdate(UserAuthorityDTO userAuthorityDTO) {
        log.debug("Request to partially update UserAuthority : {}", userAuthorityDTO);

        return userAuthorityRepository
            .findById(userAuthorityDTO.getId())
            .map(existingUserAuthority -> {
                userAuthorityMapper.partialUpdate(existingUserAuthority, userAuthorityDTO);

                return existingUserAuthority;
            })
            .map(userAuthorityRepository::save)
            .map(userAuthorityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAuthorityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserAuthorities");
        return userAuthorityRepository.findAll(pageable).map(userAuthorityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAuthorityDTO> findOne(Long id) {
        log.debug("Request to get UserAuthority : {}", id);
        return userAuthorityRepository.findById(id).map(userAuthorityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserAuthority : {}", id);
        userAuthorityRepository.deleteById(id);
    }

    @Override
    public JHIUserAuthorityDTO findByUserId(Long userid) {
        return userAuthorityRepository.findUserAuthority(userid);
    }
}
