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

    @Autowired
    ItemInventoryRepository inventoryRepository;

    /**
     *  Get price of the item
     * @param itemId Identification of the item
     * @return Price of the item
     */
    @Cacheable(value = "itemInventoryCache", key = "#itemId", unless = "#result == null")
    public Float getPriceByItemId(String itemId) {
        return inventoryRepository.getPriceByItemId(itemId);
    }

    /**
     * Clean cache for specific item
     * @param itemId Identification of the item
     */
    @CacheEvict(value = "itemInventoryCache", key = "#itemId")
    public void releasePriceByItemId(String itemId) {
        LOGGER.info("Clean cache price by itemId :{}", itemId);
    }

    /**
     * Clean cache for all items
     */
    @CacheEvict(cacheNames="itemInventoryCache", allEntries = true)
    public void releaseAllPrices() {
        LOGGER.info("Clean cache all items");
    }
}