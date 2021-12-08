package ec.loja.service;

import ec.loja.service.dto.ReceiptDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ec.loja.domain.Receipt}.
 */
public interface ReceiptService {
    /**
     * Save a receipt.
     *
     * @param receiptDTO the entity to save.
     * @return the persisted entity.
     */
    ReceiptDTO save(ReceiptDTO receiptDTO);

    /**
     * Partially updates a receipt.
     *
     * @param receiptDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReceiptDTO> partialUpdate(ReceiptDTO receiptDTO);

    /**
     * Get all the receipts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReceiptDTO> findAll(Pageable pageable);

    /**
     * Get the "id" receipt.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReceiptDTO> findOne(Long id);

    /**
     * Delete the "id" receipt.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
