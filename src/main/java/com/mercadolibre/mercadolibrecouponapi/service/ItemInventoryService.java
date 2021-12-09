package com.mercadolibre.mercadolibrecouponapi.service;

import com.mercadolibre.mercadolibrecouponapi.cache.ItemInventoryCache;
import com.mercadolibre.mercadolibrecouponapi.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemInventoryService {
    @Autowired
    ItemInventoryCache itemInventoryCache;

    /**
     * Get prices of the items
     * @param itemsId List of id of items
     * @return List of item with price each one
     */
    public List<Item> getItemsWithPrice(List<String> itemsId) {
        List<Item> items = new ArrayList<>();
        if(itemsId != null) {
            items = itemsId.parallelStream()
                    .filter(itemId -> itemId != null && !itemId.isEmpty())
                    .map(itemId -> new Item(itemId, itemInventoryCache.getPriceByItemId(itemId)))
                    .filter(item -> item.getPrice() != null)
                    .collect(Collectors.toList());
        }
        return items;
    }
}