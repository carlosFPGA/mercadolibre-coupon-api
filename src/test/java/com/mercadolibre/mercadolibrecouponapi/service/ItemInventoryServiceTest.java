package com.mercadolibre.mercadolibrecouponapi.service;

import com.mercadolibre.mercadolibrecouponapi.cache.ItemInventoryCache;
import com.mercadolibre.mercadolibrecouponapi.model.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemInventoryServiceTest {

    @InjectMocks
    ItemInventoryService itemInventoryService;

    @Mock
    ItemInventoryCache itemInventoryCache;

    @Captor
    private ArgumentCaptor<String> itemId;

    @Test
    void getItemWithPriceList_whenItemsIsNull_thenReturnEmptyMap() {
        List<Item> itemList = itemInventoryService.getItemWithPriceList(null);
        assertNotNull(itemList);
        assertTrue(itemList.isEmpty());

        verifyNoInteractions(itemInventoryCache);
    }

    @Test
    void getItemWithPriceList_whenItemsIsEmpty_thenReturnEmptyMap() {
        List<Item> itemList = itemInventoryService.getItemWithPriceList(new ArrayList<>());
        assertNotNull(itemList);
        assertTrue(itemList.isEmpty());

        verifyNoInteractions(itemInventoryCache);
    }

    @Test
    void getItemWithPriceList_whenItemsIsValid_thenReturnPriceInMap() {
        when(itemInventoryCache.getPriceByItemId("MLA123")).thenReturn(10.00F);

        List<Item> itemList = itemInventoryService.getItemWithPriceList(Collections.singletonList("MLA123"));
        assertNotNull(itemList);
        assertEquals(1, itemList.size());
        assertEquals("MLA123", itemList.get(0).getId());
        assertEquals(10.00F,  itemList.get(0).getPrice());

        verifyUseItemInventoryService(Collections.singletonList("MLA123"));
    }

    @Test
    void getItemWithPriceList_whenItemsIsValidButPriceIsNull_thenReturnEmptyMap() {
        when(itemInventoryCache.getPriceByItemId("MLA123")).thenReturn(null);

        List<Item> itemList = itemInventoryService.getItemWithPriceList(Collections.singletonList("MLA123"));
        assertNotNull(itemList);
        assertTrue(itemList.isEmpty());

        verifyUseItemInventoryService(Collections.singletonList("MLA123"));
    }

    @Test
    void getItemWithPriceList_whenItemInListIsNull_thenReturnOtherPrices() {
        when(itemInventoryCache.getPriceByItemId(anyString())).thenReturn(10.00F);

        List<Item> itemList = itemInventoryService.getItemWithPriceList(Arrays.asList("MLA123", null));
        assertNotNull(itemList);
        assertEquals(1, itemList.size());
        assertEquals("MLA123", itemList.get(0).getId());
        assertEquals(10.00F,  itemList.get(0).getPrice());

        verifyUseItemInventoryService(Collections.singletonList("MLA123"));
    }

    @Test
    void getItemWithPriceList_whenItemInListIsEmpty_thenReturnOtherPrices() {
        when(itemInventoryCache.getPriceByItemId(anyString())).thenReturn(10.00F);

        List<Item> itemList = itemInventoryService.getItemWithPriceList(Arrays.asList("MLA123", ""));
        assertNotNull(itemList);
        assertEquals(1, itemList.size());
        assertEquals("MLA123", itemList.get(0).getId());
        assertEquals(10.00F,  itemList.get(0).getPrice());

        verifyUseItemInventoryService(Collections.singletonList("MLA123"));
    }

    @Test
    void getItemWithPriceList_whenTwoItems_thenReturnTwoItems() {
        when(itemInventoryCache.getPriceByItemId("MLA123")).thenReturn(10.00F);
        when(itemInventoryCache.getPriceByItemId("MLA456")).thenReturn(20.00F);

        List<Item> itemList = itemInventoryService.getItemWithPriceList(Arrays.asList("MLA123", "MLA456"));
        assertNotNull(itemList);
        assertEquals(2, itemList.size());
        assertTrue(itemList.contains(new Item("MLA123", 10.00F)));
        assertTrue(itemList.contains(new Item("MLA456", 20.00F)));

        verifyUseItemInventoryService(Arrays.asList("MLA123", "MLA456"));
    }


    private void verifyUseItemInventoryService(List<String> itemIdListExpected) {
        verify(itemInventoryCache, times(itemIdListExpected.size())).getPriceByItemId(itemId.capture());
        List<String> itemIdListActual = itemId.getAllValues();
        assertNotNull(itemIdListActual);
        assertTrue(itemIdListExpected.containsAll(itemIdListActual));
        assertTrue(itemIdListActual.containsAll(itemIdListExpected));
        verifyNoMoreInteractions(itemInventoryCache);
    }
}
