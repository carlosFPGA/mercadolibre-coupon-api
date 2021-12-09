package com.mercadolibre.mercadolibrecouponapi.service;

import com.mercadolibre.mercadolibrecouponapi.cache.ItemInventoryCache;
import com.mercadolibre.mercadolibrecouponapi.model.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemInventoryServiceTest {

    @InjectMocks
    ItemInventoryService itemInventoryService;

    @Mock
    ItemInventoryCache itemInventoryCache;

    @Test
    void getPriceOfItems_whenItemsIsNull_thenReturnEmptyMap() {
        List<Item> itemsWithPrice = itemInventoryService.getItemsWithPrice(null);
        assertNotNull(itemsWithPrice);
        assertTrue(itemsWithPrice.isEmpty());
    }

    @Test
    void getPriceOfItems_whenItemsIsEmpty_thenReturnEmptyMap() {
        List<Item> itemsWithPrice = itemInventoryService.getItemsWithPrice(new ArrayList<>());
        assertNotNull(itemsWithPrice);
        assertTrue(itemsWithPrice.isEmpty());
    }

    @Test
    void getPriceOfItems_whenItemsIsValid_thenReturnPriceInMap() {
        when(itemInventoryCache.getPriceByItemId("MLA123")).thenReturn(10.00F);

        List<Item> itemsWithPrice = itemInventoryService.getItemsWithPrice(Collections.singletonList("MLA123"));
        assertNotNull(itemsWithPrice);
        assertEquals(1, itemsWithPrice.size());
        assertEquals("MLA123", itemsWithPrice.get(0).getId());
        assertEquals(10.00F,  itemsWithPrice.get(0).getPrice());
    }

    @Test
    void getPriceOfItems_whenItemsIsValidButPriceIsNull_thenReturnEmptyMap() {
        when(itemInventoryCache.getPriceByItemId("MLA123")).thenReturn(null);

        List<Item> itemsWithPrice = itemInventoryService.getItemsWithPrice(Collections.singletonList("MLA123"));
        assertNotNull(itemsWithPrice);
        assertTrue(itemsWithPrice.isEmpty());
    }

    @Test
    void getPriceOfItems_whenItemInListIsNull_thenReturnOtherPrices() {
        when(itemInventoryCache.getPriceByItemId(anyString())).thenReturn(10.00F);

        List<Item> itemsWithPrice = itemInventoryService.getItemsWithPrice(Arrays.asList("MLA123", null));
        assertNotNull(itemsWithPrice);
        assertEquals(1, itemsWithPrice.size());
        assertEquals("MLA123", itemsWithPrice.get(0).getId());
        assertEquals(10.00F,  itemsWithPrice.get(0).getPrice());
    }

    @Test
    void getPriceOfItems_whenItemInListIsEmpty_thenReturnOtherPrices() {
        when(itemInventoryCache.getPriceByItemId(anyString())).thenReturn(10.00F);

        List<Item> itemsWithPrice = itemInventoryService.getItemsWithPrice(Arrays.asList("MLA123", ""));
        assertNotNull(itemsWithPrice);
        assertEquals(1, itemsWithPrice.size());
        assertEquals("MLA123", itemsWithPrice.get(0).getId());
        assertEquals(10.00F,  itemsWithPrice.get(0).getPrice());
    }
}
