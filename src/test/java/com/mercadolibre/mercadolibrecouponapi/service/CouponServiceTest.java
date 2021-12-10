package com.mercadolibre.mercadolibrecouponapi.service;

import com.mercadolibre.mercadolibrecouponapi.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {
    @InjectMocks
    CouponService couponService;

    @Mock
    ItemInventoryService itemInventoryService;

    @Mock
    MaximizeCouponService maximizeCouponService;

    @Test
    void getMaximumUtilizationCoupon_whenErrorInItemInventoryService_thenReturnEmptyGroup(){
        when(itemInventoryService.getItemWithPriceList(anyList())).thenThrow(RuntimeException.class);

        ItemGroup result = couponService.getMaximumUtilizationCoupon(Arrays.asList("MLA123", "ML456"), 30.00F);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(itemInventoryService, times(1))
                .getItemWithPriceList(Arrays.asList("MLA123", "ML456"));
        verifyNoMoreInteractions(itemInventoryService);
        verifyNoInteractions(maximizeCouponService);
    }

    @Test
    void getMaximumUtilizationCoupon_whenErrorInMaximizeCouponService_thenReturnEmptyGroup(){
        List<Item> itemList = Arrays.asList(new Item("MLA123", 10.0F), new Item("MLA456", 20.0F));
        when(itemInventoryService.getItemWithPriceList(anyList())).thenReturn(itemList);
        when(maximizeCouponService.getBestPossibleItemGroup(anyList(), anyFloat())).thenThrow(RuntimeException.class);

        ItemGroup result = couponService.getMaximumUtilizationCoupon(Arrays.asList("MLA123", "ML456"), 30.00F);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(itemInventoryService, times(1))
                .getItemWithPriceList(Arrays.asList("MLA123", "ML456"));
        verifyNoMoreInteractions(itemInventoryService);
        verify(maximizeCouponService, times(1)).getBestPossibleItemGroup(itemList, 30.00F);
        verifyNoMoreInteractions(maximizeCouponService);
    }

    @Test
    void getMaximumUtilizationCoupon_whenSuccess_thenReturnSolutionGroup(){
        Item mla123 = new Item("MLA123", 10.0F);
        Item mla456 = new Item("MLA456", 20.0F);
        List<Item> itemList = Arrays.asList(mla123, mla456);
        when(itemInventoryService.getItemWithPriceList(anyList())).thenReturn(itemList);
        ItemGroup itemGroup = new ItemGroup(new HashSet<>(Arrays.asList(mla123, mla456)));
        when(maximizeCouponService.getBestPossibleItemGroup(anyList(), anyFloat())).thenReturn(itemGroup);

        ItemGroup result = couponService.getMaximumUtilizationCoupon(Arrays.asList("MLA123", "ML456"), 30.00F);

        assertNotNull(result);
        assertEquals(itemGroup, result);

        verify(itemInventoryService, times(1))
                .getItemWithPriceList(Arrays.asList("MLA123", "ML456"));
        verifyNoMoreInteractions(itemInventoryService);
        verify(maximizeCouponService, times(1)).getBestPossibleItemGroup(itemList, 30.00F);
        verifyNoMoreInteractions(maximizeCouponService);
    }
}
