package ec.loja.service.impl;

import ec.loja.domain.Receipt;
import ec.loja.repository.ReceiptRepository;
import ec.loja.service.ReceiptService;
import ec.loja.service.dto.ReceiptDTO;
import ec.loja.service.mapper.ReceiptMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Receipt}.
 */
@Service
@Transactional
public class ReceiptServiceImpl implements ReceiptService {

    private final Logger log = LoggerFactory.getLogger(ReceiptServiceImpl.class);

    private final ReceiptRepository receiptRepository;

    private final ReceiptMapper receiptMapper;

    public ReceiptServiceImpl(ReceiptRepository receiptRepository, ReceiptMapper receiptMapper) {
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
    }

    @Override
    public ReceiptDTO save(ReceiptDTO receiptDTO) {
        log.debug("Request to save Receipt : {}", receiptDTO);
        Receipt receipt = receiptMapper.toEntity(receiptDTO);
        receipt = receiptRepository.save(receipt);
        return receiptMapper.toDto(receipt);
    }

    @Override
    public Optional<ReceiptDTO> partialUpdate(ReceiptDTO receiptDTO) {
        log.debug("Request to partially update Receipt : {}", receiptDTO);

        return receiptRepository
            .findById(receiptDTO.getId())
            .map(existingReceipt -> {
                receiptMapper.partialUpdate(existingReceipt, receiptDTO);

                return existingReceipt;
            })
            .map(receiptRepository::save)
            .map(receiptMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReceiptDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Receipts");
        return receiptRepository.findAll(pageable).map(receiptMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReceiptDTO> findOne(Long id) {
        log.debug("Request to get Receipt : {}", id);
        return receiptRepository.findById(id).map(receiptMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Receipt : {}", id);
        receiptRepository.deleteById(id);
    }
}
