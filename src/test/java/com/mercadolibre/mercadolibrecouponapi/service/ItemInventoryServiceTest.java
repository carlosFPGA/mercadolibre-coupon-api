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

    public static final float TEST_PRICE_1 = 10.00F;
    public static final float TEST_PRICE_2 = 20.00F;
    @InjectMocks
    private ItemInventoryService itemInventoryService;

    @Mock
    private ItemInventoryCache itemInventoryCache;

    @Captor
    private ArgumentCaptor<String> itemId;

    @Test
    void getItemWithPriceListWhenItemsIsNullThenReturnEmptyMap() {
        List<Item> itemList = itemInventoryService.getItemWithPriceList(null);
        assertNotNull(itemList);
        assertTrue(itemList.isEmpty());

        verifyNoInteractions(itemInventoryCache);
    }

    @Test
    void getItemWithPriceListWhenItemsIsEmptyThenReturnEmptyMap() {
        List<Item> itemList = itemInventoryService.getItemWithPriceList(new ArrayList<>());
        assertNotNull(itemList);
        assertTrue(itemList.isEmpty());

        verifyNoInteractions(itemInventoryCache);
    }

    @Test
    void getItemWithPriceListWhenItemsIsValidThenReturnPriceInMap() {
        when(itemInventoryCache.getPriceByItemId("MLA123")).thenReturn(TEST_PRICE_1);

        List<Item> itemList = itemInventoryService.getItemWithPriceList(Collections.singletonList("MLA123"));
        assertNotNull(itemList);
        assertEquals(1, itemList.size());
        assertEquals("MLA123", itemList.get(0).getId());
        assertEquals(TEST_PRICE_1,  itemList.get(0).getPrice());

        verifyUseItemInventoryService(Collections.singletonList("MLA123"));
    }

    @Test
    void getItemWithPriceListWhenItemsIsValidButPriceIsNullThenReturnEmptyMap() {
        when(itemInventoryCache.getPriceByItemId("MLA123")).thenReturn(null);

        List<Item> itemList = itemInventoryService.getItemWithPriceList(Collections.singletonList("MLA123"));
        assertNotNull(itemList);
        assertTrue(itemList.isEmpty());

        verifyUseItemInventoryService(Collections.singletonList("MLA123"));
    }

    @Test
    void getItemWithPriceListWhenItemInListIsNullThenReturnOtherPrices() {
        when(itemInventoryCache.getPriceByItemId(anyString())).thenReturn(TEST_PRICE_1);

        List<Item> itemList = itemInventoryService.getItemWithPriceList(Arrays.asList("MLA123", null));
        assertNotNull(itemList);
        assertEquals(1, itemList.size());
        assertEquals("MLA123", itemList.get(0).getId());
        assertEquals(TEST_PRICE_1,  itemList.get(0).getPrice());

        verifyUseItemInventoryService(Collections.singletonList("MLA123"));
    }

    @Test
    void getItemWithPriceListWhenItemInListIsEmptyThenReturnOtherPrices() {
        when(itemInventoryCache.getPriceByItemId(anyString())).thenReturn(TEST_PRICE_1);

        List<Item> itemList = itemInventoryService.getItemWithPriceList(Arrays.asList("MLA123", ""));
        assertNotNull(itemList);
        assertEquals(1, itemList.size());
        assertEquals("MLA123", itemList.get(0).getId());
        assertEquals(TEST_PRICE_1,  itemList.get(0).getPrice());

        verifyUseItemInventoryService(Collections.singletonList("MLA123"));
    }

    @Test
    void getItemWithPriceListWhenTwoItemsThenReturnTwoItems() {
        when(itemInventoryCache.getPriceByItemId("MLA123")).thenReturn(TEST_PRICE_1);
        when(itemInventoryCache.getPriceByItemId("MLA456")).thenReturn(TEST_PRICE_2);

        List<Item> itemList = itemInventoryService.getItemWithPriceList(Arrays.asList("MLA123", "MLA456"));
        assertNotNull(itemList);
        assertEquals(2, itemList.size());
        assertTrue(itemList.contains(new Item("MLA123", TEST_PRICE_1)));
        assertTrue(itemList.contains(new Item("MLA456", TEST_PRICE_2)));

        verifyUseItemInventoryService(Arrays.asList("MLA123", "MLA456"));
    }


    private void verifyUseItemInventoryService(final List<String> itemIdListExpected) {
        verify(itemInventoryCache, times(itemIdListExpected.size())).getPriceByItemId(itemId.capture());
        List<String> itemIdListActual = itemId.getAllValues();
        assertNotNull(itemIdListActual);
        assertTrue(itemIdListExpected.containsAll(itemIdListActual));
        assertTrue(itemIdListActual.containsAll(itemIdListExpected));
        verifyNoMoreInteractions(itemInventoryCache);
    }
}
