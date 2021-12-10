package com.mercadolibre.mercadolibrecouponapi.controller;

import com.mercadolibre.mercadolibrecouponapi.cache.ItemInventoryCache;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for clean cache.
 * @author Carlos Parra
 */
@Hidden
@RestController
public class CleanCacheController {
    @Autowired
    private ItemInventoryCache itemInventoryCache;

    /**
     * Endpoint for clean cache of price of specific item.
     * @param itemId Identification of item
     * @return Response with confirmation
     */
    @GetMapping(value = "/cleanCache/price/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> releasePriceByItemId(
            @PathVariable(value = "itemId") final String itemId) {
        itemInventoryCache.releasePriceByItemId(itemId);
        return ResponseEntity.status(HttpStatus.OK)
                .body("{\"status\": \"true\",\"reason\": \"Cleared price cache by itemId\"}");
    }

    /**
     * Endpoint for clean cache of price of all items.
     * @return Response with confirmation
     */
    @GetMapping(value = "/cleanCache/allPrices", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> releaseAllPrices() {
        itemInventoryCache.releaseAllPrices();
        return ResponseEntity.status(HttpStatus.OK)
                .body("{\"status\": \"true\",\"reason\": \"Cleared All prices cache\"}");
    }
}
