package ec.loja.service.impl;

import ec.loja.domain.TariffVehicleType;
import ec.loja.repository.TariffVehicleTypeRepository;
import ec.loja.service.TariffVehicleTypeService;
import ec.loja.service.dto.TariffVehicleTypeDTO;
import ec.loja.service.mapper.TariffVehicleTypeMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TariffVehicleType}.
 */
@Service
@Transactional
public class TariffVehicleTypeServiceImpl implements TariffVehicleTypeService {

    private final Logger log = LoggerFactory.getLogger(TariffVehicleTypeServiceImpl.class);

    private final TariffVehicleTypeRepository tariffVehicleTypeRepository;

    private final TariffVehicleTypeMapper tariffVehicleTypeMapper;

    public TariffVehicleTypeServiceImpl(
        TariffVehicleTypeRepository tariffVehicleTypeRepository,
        TariffVehicleTypeMapper tariffVehicleTypeMapper
    ) {
        this.tariffVehicleTypeRepository = tariffVehicleTypeRepository;
        this.tariffVehicleTypeMapper = tariffVehicleTypeMapper;
    }

    @Override
    public TariffVehicleTypeDTO save(TariffVehicleTypeDTO tariffVehicleTypeDTO) {
        log.debug("Request to save TariffVehicleType : {}", tariffVehicleTypeDTO);
        TariffVehicleType tariffVehicleType = tariffVehicleTypeMapper.toEntity(tariffVehicleTypeDTO);
        tariffVehicleType = tariffVehicleTypeRepository.save(tariffVehicleType);
        return tariffVehicleTypeMapper.toDto(tariffVehicleType);
    }

    @Override
    public Optional<TariffVehicleTypeDTO> partialUpdate(TariffVehicleTypeDTO tariffVehicleTypeDTO) {
        log.debug("Request to partially update TariffVehicleType : {}", tariffVehicleTypeDTO);

        return tariffVehicleTypeRepository
            .findById(tariffVehicleTypeDTO.getId())
            .map(existingTariffVehicleType -> {
                tariffVehicleTypeMapper.partialUpdate(existingTariffVehicleType, tariffVehicleTypeDTO);

                return existingTariffVehicleType;
            })
            .map(tariffVehicleTypeRepository::save)
            .map(tariffVehicleTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TariffVehicleTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TariffVehicleTypes");
        return tariffVehicleTypeRepository.findAll(pageable).map(tariffVehicleTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TariffVehicleTypeDTO> findOne(Long id) {
        log.debug("Request to get TariffVehicleType : {}", id);
        return tariffVehicleTypeRepository.findById(id).map(tariffVehicleTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TariffVehicleType : {}", id);
        tariffVehicleTypeRepository.deleteById(id);
    }

    @Override
    public List<TariffVehicleType> getTarrifVehicleByInstitution(Long institutionid, LocalDate currentdate) {
        log.debug("Request to  getTarrifVehicleByInstitution : {} {}", institutionid, currentdate);
        List<TariffVehicleType> list = tariffVehicleTypeRepository.getByInstitution(institutionid, currentdate);
        return list;
    }
}
