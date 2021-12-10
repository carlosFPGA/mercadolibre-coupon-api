package com.mercadolibre.mercadolibrecouponapi.service;

import com.mercadolibre.mercadolibrecouponapi.cache.ItemInventoryCache;
import com.mercadolibre.mercadolibrecouponapi.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for get price of Items using Mercado Libre API.
 * @author Carlos Parra
 */
@Service
public class ItemInventoryService {
    @Autowired
    private ItemInventoryCache itemInventoryCache;

    /**
     * Get prices of the items.
     * @param itemIdList List of id of items
     * @return List of item with price each one
     */
    public List<Item> getItemWithPriceList(final List<String> itemIdList) {
        List<Item> itemList = new ArrayList<>();
        if (itemIdList != null) {
            itemList = itemIdList.parallelStream()
                    .filter(itemId -> itemId != null && !itemId.isEmpty())
                    .map(itemId -> new Item(itemId, itemInventoryCache.getPriceByItemId(itemId)))
                    .filter(item -> item.getPrice() != null)
                    .collect(Collectors.toList());
        }
        return itemList;
    }
}
