package ec.loja.service.impl;

import ec.loja.domain.Tariff;
import ec.loja.repository.TariffRepository;
import ec.loja.service.TariffService;
import ec.loja.service.dto.TariffDTO;
import ec.loja.service.mapper.TariffMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tariff}.
 */
@Service
@Transactional
public class TariffServiceImpl implements TariffService {

    private final Logger log = LoggerFactory.getLogger(TariffServiceImpl.class);

    private final TariffRepository tariffRepository;

    private final TariffMapper tariffMapper;

    public TariffServiceImpl(TariffRepository tariffRepository, TariffMapper tariffMapper) {
        this.tariffRepository = tariffRepository;
        this.tariffMapper = tariffMapper;
    }

    @Override
    public TariffDTO save(TariffDTO tariffDTO) {
        log.debug("Request to save Tariff : {}", tariffDTO);
        Tariff tariff = tariffMapper.toEntity(tariffDTO);
        tariff = tariffRepository.save(tariff);
        return tariffMapper.toDto(tariff);
    }

    @Override
    public Optional<TariffDTO> partialUpdate(TariffDTO tariffDTO) {
        log.debug("Request to partially update Tariff : {}", tariffDTO);

        return tariffRepository
            .findById(tariffDTO.getId())
            .map(existingTariff -> {
                tariffMapper.partialUpdate(existingTariff, tariffDTO);

                return existingTariff;
            })
            .map(tariffRepository::save)
            .map(tariffMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TariffDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tariffs");
        return tariffRepository.findAll(pageable).map(tariffMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TariffDTO> findOne(Long id) {
        log.debug("Request to get Tariff : {}", id);
        return tariffRepository.findById(id).map(tariffMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tariff : {}", id);
        tariffRepository.deleteById(id);
    }
}
