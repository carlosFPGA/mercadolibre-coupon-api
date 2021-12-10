package com.mercadolibre.mercadolibrecouponapi.controller;

import com.mercadolibre.mercadolibrecouponapi.cache.ItemInventoryCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CleanCacheController {
    private static final Logger logger = LoggerFactory.getLogger(CleanCacheController.class);

    @Autowired
    ItemInventoryCache itemInventoryCache;

    @GetMapping(value = "/cleanCache/price/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String releasePriceByItemId(@PathVariable(value = "itemId") String itemId) {
        if (itemId == null || itemId.trim().isEmpty()) {
            logger.error("Result Clean Cache: itemId null or empty" );
            return "{\"status\": \"false\", \"reason\": \"Invalid itemId\"}";
        }
        itemInventoryCache.releasePriceByItemId(itemId);
        return "{\"status\": \"true\", \"reason\": \"Cleared price cache by itemId\"}";
    }

    @GetMapping(value = "/cleanCache/allPrices", produces = MediaType.APPLICATION_JSON_VALUE)
    public String releaseAllPrices() {
        itemInventoryCache.releaseAllPrices();
        return "{\"status\": \"true\", \"reason\": \"Cleared All prices cache\"}";
    }
}
