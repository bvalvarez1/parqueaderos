package ec.loja.service.impl;

import ec.loja.domain.RecordTicket;
import ec.loja.repository.RecordTicketRepository;
import ec.loja.service.RecordTicketService;
import ec.loja.service.dto.RecordTicketDTO;
import ec.loja.service.mapper.RecordTicketMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RecordTicket}.
 */
@Service
@Transactional
public class RecordTicketServiceImpl implements RecordTicketService {

    private final Logger log = LoggerFactory.getLogger(RecordTicketServiceImpl.class);

    private final RecordTicketRepository recordTicketRepository;

    private final RecordTicketMapper recordTicketMapper;

    public RecordTicketServiceImpl(RecordTicketRepository recordTicketRepository, RecordTicketMapper recordTicketMapper) {
        this.recordTicketRepository = recordTicketRepository;
        this.recordTicketMapper = recordTicketMapper;
    }

    @Override
    public RecordTicketDTO save(RecordTicketDTO recordTicketDTO) {
        log.debug("Request to save RecordTicket : {}", recordTicketDTO);
        RecordTicket recordTicket = recordTicketMapper.toEntity(recordTicketDTO);
        recordTicket = recordTicketRepository.save(recordTicket);
        return recordTicketMapper.toDto(recordTicket);
    }

    @Override
    public Optional<RecordTicketDTO> partialUpdate(RecordTicketDTO recordTicketDTO) {
        log.debug("Request to partially update RecordTicket : {}", recordTicketDTO);

        return recordTicketRepository
            .findById(recordTicketDTO.getId())
            .map(existingRecordTicket -> {
                recordTicketMapper.partialUpdate(existingRecordTicket, recordTicketDTO);

                return existingRecordTicket;
            })
            .map(recordTicketRepository::save)
            .map(recordTicketMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecordTicketDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RecordTickets");
        return recordTicketRepository.findAll(pageable).map(recordTicketMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecordTicketDTO> findOne(Long id) {
        log.debug("Request to get RecordTicket : {}", id);
        return recordTicketRepository.findById(id).map(recordTicketMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RecordTicket : {}", id);
        recordTicketRepository.deleteById(id);
    }

    @Override
    public Optional<RecordTicketDTO> findBySerialAndInstitution(String sequential, Long institutionid) {
        log.debug("Request to findBySerialAndInstitution RecordTicket : {} {}", sequential, institutionid);
        RecordTicket record = recordTicketRepository.findBySerianAndInstitution(sequential, institutionid);
        return Optional.ofNullable(record).map(recordTicketMapper::toDto);
    }
}
