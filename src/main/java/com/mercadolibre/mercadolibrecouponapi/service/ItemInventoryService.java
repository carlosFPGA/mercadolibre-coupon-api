package com.mercadolibre.mercadolibrecouponapi.service;

import com.mercadolibre.mercadolibrecouponapi.cache.ItemInventoryCache;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemInventoryService {
    final ItemInventoryCache itemInventoryCache;

    public ItemInventoryService(ItemInventoryCache itemInventoryCache) {
        this.itemInventoryCache = itemInventoryCache;
    }

    public Map<String, Float> getPriceOfItems(List<String> itemsId) {
        Map<String, Float> mapIdToPrice = new HashMap<>();
        for(String itemId : itemsId) {
            if (itemId != null && !itemId.isEmpty()) {
                Float price = itemInventoryCache.getPriceByItemId(itemId);
                if (price != null) {
                    mapIdToPrice.put(itemId, price);
                }
            }
        }
        return mapIdToPrice;
    }
}