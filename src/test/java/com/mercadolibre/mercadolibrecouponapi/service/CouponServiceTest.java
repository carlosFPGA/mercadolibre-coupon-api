package com.mercadolibre.mercadolibrecouponapi.service;

import com.mercadolibre.mercadolibrecouponapi.model.Item;
import com.mercadolibre.mercadolibrecouponapi.model.ItemGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.anyFloat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {
    public static final float TEST_AMOUNT = 30.00F;
    public static final float TEST_PRICE_1 = 10.0F;
    public static final float TEST_PRICE_2 = 20.0F;
    @InjectMocks
    private CouponService couponService;

    @Mock
    private ItemInventoryService itemInventoryService;

    @Mock
    private MaximizeCouponService maximizeCouponService;

    @Test
    void getMaximumUtilizationCouponWhenErrorInItemInventoryServiceThenReturnEmptyGroup() {
        when(itemInventoryService.getItemWithPriceList(anyList())).thenThrow(RuntimeException.class);

        ItemGroup result = couponService.getMaximumUtilizationCoupon(Arrays.asList("MLA123", "ML456"), TEST_AMOUNT);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(itemInventoryService, times(1))
                .getItemWithPriceList(Arrays.asList("MLA123", "ML456"));
        verifyNoMoreInteractions(itemInventoryService);
        verifyNoInteractions(maximizeCouponService);
    }

    @Test
    void getMaximumUtilizationCouponWhenErrorInMaximizeCouponServiceThenReturnEmptyGroup() {
        List<Item> itemList = Arrays.asList(new Item("MLA123", TEST_PRICE_1), new Item("MLA456", TEST_PRICE_2));
        when(itemInventoryService.getItemWithPriceList(anyList())).thenReturn(itemList);
        when(maximizeCouponService.getBestPossibleItemGroup(anyList(), anyFloat())).thenThrow(RuntimeException.class);

        ItemGroup result = couponService.getMaximumUtilizationCoupon(Arrays.asList("MLA123", "ML456"), TEST_AMOUNT);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(itemInventoryService, times(1))
                .getItemWithPriceList(Arrays.asList("MLA123", "ML456"));
        verifyNoMoreInteractions(itemInventoryService);
        verify(maximizeCouponService, times(1)).getBestPossibleItemGroup(itemList, TEST_AMOUNT);
        verifyNoMoreInteractions(maximizeCouponService);
    }

    @Test
    void getMaximumUtilizationCouponWhenSuccessThenReturnSolutionGroup() {
        Item mla123 = new Item("MLA123", TEST_PRICE_1);
        Item mla456 = new Item("MLA456", TEST_PRICE_2);
        List<Item> itemList = Arrays.asList(mla123, mla456);
        when(itemInventoryService.getItemWithPriceList(anyList())).thenReturn(itemList);
        ItemGroup itemGroup = new ItemGroup(new HashSet<>(Arrays.asList(mla123, mla456)));
        when(maximizeCouponService.getBestPossibleItemGroup(anyList(), anyFloat())).thenReturn(itemGroup);

        ItemGroup result = couponService.getMaximumUtilizationCoupon(Arrays.asList("MLA123", "ML456"), TEST_AMOUNT);

        assertNotNull(result);
        assertEquals(itemGroup, result);

        verify(itemInventoryService, times(1))
                .getItemWithPriceList(Arrays.asList("MLA123", "ML456"));
        verifyNoMoreInteractions(itemInventoryService);
        verify(maximizeCouponService, times(1)).getBestPossibleItemGroup(itemList, TEST_AMOUNT);
        verifyNoMoreInteractions(maximizeCouponService);
    }
}
