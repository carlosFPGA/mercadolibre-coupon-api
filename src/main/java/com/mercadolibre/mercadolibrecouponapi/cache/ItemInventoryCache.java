package com.mercadolibre.mercadolibrecouponapi.cache;

import com.mercadolibre.mercadolibrecouponapi.repository.ItemInventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class ItemInventoryCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemInventoryCache.class);

    final ItemInventoryRepository inventoryRepository;

    public ItemInventoryCache(ItemInventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Cacheable(value = "itemInventoryCache", key = "#itemId", unless = "#result == null")
    public Float getPriceByItemId(String itemId) {
        return inventoryRepository.getPriceByItemId(itemId);
    }

    @CacheEvict(value = "itemInventoryCache", key = "#itemId")
    public void releaseByItemId(String itemId) {
        LOGGER.info("Clean cache price by itemId");
    }

    @CacheEvict(cacheNames="itemInventoryCache", allEntries = true)
    public void releaseAll() {
        LOGGER.info("Clean cache all items");
    }

}