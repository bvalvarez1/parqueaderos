package ec.loja.service.impl;

import ec.loja.domain.ItemCatalogue;
import ec.loja.repository.ItemCatalogueRepository;
import ec.loja.service.ItemCatalogueService;
import ec.loja.service.dto.ItemCatalogueDTO;
import ec.loja.service.mapper.ItemCatalogueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ItemCatalogue}.
 */
@Service
@Transactional
public class ItemCatalogueServiceImpl implements ItemCatalogueService {

    private final Logger log = LoggerFactory.getLogger(ItemCatalogueServiceImpl.class);

    private final ItemCatalogueRepository itemCatalogueRepository;

    private final ItemCatalogueMapper itemCatalogueMapper;

    public ItemCatalogueServiceImpl(ItemCatalogueRepository itemCatalogueRepository, ItemCatalogueMapper itemCatalogueMapper) {
        this.itemCatalogueRepository = itemCatalogueRepository;
        this.itemCatalogueMapper = itemCatalogueMapper;
    }

    @Override
    public ItemCatalogueDTO save(ItemCatalogueDTO itemCatalogueDTO) {
        log.debug("Request to save ItemCatalogue : {}", itemCatalogueDTO);
        ItemCatalogue itemCatalogue = itemCatalogueMapper.toEntity(itemCatalogueDTO);
        itemCatalogue = itemCatalogueRepository.save(itemCatalogue);
        return itemCatalogueMapper.toDto(itemCatalogue);
    }

    @Override
    public Optional<ItemCatalogueDTO> partialUpdate(ItemCatalogueDTO itemCatalogueDTO) {
        log.debug("Request to partially update ItemCatalogue : {}", itemCatalogueDTO);

        return itemCatalogueRepository
            .findById(itemCatalogueDTO.getId())
            .map(existingItemCatalogue -> {
                itemCatalogueMapper.partialUpdate(existingItemCatalogue, itemCatalogueDTO);

                return existingItemCatalogue;
            })
            .map(itemCatalogueRepository::save)
            .map(itemCatalogueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemCatalogueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ItemCatalogues");
        return itemCatalogueRepository.findAll(pageable).map(itemCatalogueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemCatalogueDTO> findOne(Long id) {
        log.debug("Request to get ItemCatalogue : {}", id);
        return itemCatalogueRepository.findById(id).map(itemCatalogueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ItemCatalogue : {}", id);
        itemCatalogueRepository.deleteById(id);
    }

    @Override
    public Optional<ItemCatalogueDTO> findByCodeAndCatalog(String code, String catalogCode) {
        log.debug("Request to findByCodeAndCatalog ItemCatalogue : {} {}", code, catalogCode);
        return itemCatalogueRepository.findByCodeAndCatalogCode(code, catalogCode).map(itemCatalogueMapper::toDto);
    }
}
