package com.mercadolibre.mercadolibrecouponapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MaximizeCouponServiceTest {
    @InjectMocks
    MaximizeCouponService maximizeCouponService;

    @Test
    void calculate_whenItemsIsNull_thenReturnEmpty() {
        List<String> result = maximizeCouponService.calculate(null, 1.0F);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void calculate_whenItemsIsEmpty_thenReturnEmpty() {
        List<String> result = maximizeCouponService.calculate(new HashMap<>(), 1.0F);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void calculate_whenAmountIsNull_thenReturnEmpty() {
        List<String> result = maximizeCouponService.calculate(Collections.singletonMap("MLA01", 1.0F), null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void calculate_whenAmountIsZero_thenReturnEmpty() {
        List<String> result = maximizeCouponService.calculate(Collections.singletonMap("MLA01", 1.0F), 0.0F);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void calculate_whenAmountIsLessThanZero_thenReturnEmpty() {
        List<String> result = maximizeCouponService.calculate(Collections.singletonMap("MLA01", 1.0F), -1.0F);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void calculate_whenOneItemAndItemIsEqualToAmount_thenReturnItem() {
        List<String> result = maximizeCouponService.calculate(Collections.singletonMap("MLA01", 2.0F), 2.00F);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains("MLA01"));
    }

    @Test
    void calculate_whenOneItemAndItemIsLowerThanAmount_thenReturnItem() {
        List<String> result = maximizeCouponService.calculate(Collections.singletonMap("MLA01", 1.0F), 2.00F);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains("MLA01"));
    }

    @Test
    void calculate_whenOneItemAndItemIsGreaterThanAmount_thenReturnEmpty() {
        List<String> result = maximizeCouponService.calculate(Collections.singletonMap("MLA01", 3.0F), 2.00F);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void calculate_whenThreeItemsAndSumIsBiggerThanAmountAndItemTwoIsTheBiggestValid_thenReturnItemTwo() {
        Map<String, Float> itemToPriceMap  = new HashMap<String, Float>() {{
            put("MLA01", 1.00F);
            put("MLA02", 1.02F);
            put("MLA03", 1.01F);
        }};
        List<String> result = maximizeCouponService.calculate(itemToPriceMap, 2.00F);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains("MLA02"));
    }

    @Test
    void calculate_whenThreeItemsAndSumOfTwoItemsIsTheBiggestValid_thenReturnTheTwoItems() {
        Map<String, Float> itemToPriceMap  = new HashMap<String, Float>() {{
            put("MLA01", 1.00F);
            put("MLA02", 1.10F);
            put("MLA03", 0.02F);
        }};
        List<String> result = maximizeCouponService.calculate(itemToPriceMap, 1.05F);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("MLA01"));
        assertTrue(result.contains("MLA03"));
    }

    @Test
    void calculate_whenThreeItemsAndAllItemsAreBiggerThanAmount_thenReturnEmpty() {
        Map<String, Float> itemToPriceMap  = new HashMap<String, Float>() {{
            put("MLA01", 2.00F);
            put("MLA02", 3.10F);
            put("MLA03", 4.02F);
        }};
        List<String> result = maximizeCouponService.calculate(itemToPriceMap, 1.05F);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void calculate_whenFourItemsAndSumOfTwoItemsIsTheBiggestValid_thenReturnTheTwoItems() {
        Map<String, Float> itemToPriceMap  = new HashMap<String, Float>() {{
            put("MLA01", 2.00F);
            put("MLA02", 1.10F);
            put("MLA03", 0.45F);
            put("MLA04", 0.75F);
        }};
        List<String> result = maximizeCouponService.calculate(itemToPriceMap, 1.50F);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("MLA03"));
        assertTrue(result.contains("MLA04"));
    }

    @Test
    void calculate_whenFiveItemsAndSumOfFourItemsIsTheBiggestValid_thenReturnTheFourItems() {
        Map<String, Float> itemToPriceMap  = new HashMap<String, Float>() {{
            put("MLA01", 100.00F);
            put("MLA02", 210.00F);
            put("MLA03", 260.00F);
            put("MLA04", 80.00F);
            put("MLA05", 90.00F);
        }};
        List<String> result = maximizeCouponService.calculate(itemToPriceMap, 500.00F);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.contains("MLA01"));
        assertTrue(result.contains("MLA02"));
        assertTrue(result.contains("MLA04"));
        assertTrue(result.contains("MLA05"));
    }
}
