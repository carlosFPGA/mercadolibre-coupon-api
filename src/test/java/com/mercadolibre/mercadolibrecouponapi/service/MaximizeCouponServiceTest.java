package com.mercadolibre.mercadolibrecouponapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class MaximizeCouponServiceTest {
    public static final float TEST_PRICE_1 = 3.0F;
    public static final float TEST_PRICE_2 = 1.00F;
    public static final float TEST_PRICE_3 = 1.02F;
    public static final float TEST_PRICE_4 = 1.01F;
    public static final float TEST_PRICE_5 = 1.10F;
    public static final float TEST_PRICE_6 = 0.02F;
    public static final float TEST_PRICE_7 = 2.00F;
    public static final float TEST_PRICE_8 = 3.10F;
    public static final float TEST_PRICE_9 = 4.02F;
    public static final float TEST_PRICE_10 = 0.45F;
    public static final float TEST_PRICE_11 = 0.75F;
    public static final float TEST_PRICE_12 = 100.00F;
    public static final float TEST_PRICE_13 = 210.00F;
    public static final float TEST_PRICE_14 = 260.00F;
    public static final float TEST_PRICE_15 = 80.00F;
    public static final float TEST_PRICE_16 = 90.00F;

    public static final float TEST_AMOUNT_1 = 1.0F;
    public static final float TEST_AMOUNT_2 = -1.0F;
    public static final float TEST_AMOUNT_3 = 2.00F;
    public static final float TEST_AMOUNT_4 = 1.05F;
    public static final float TEST_AMOUNT_5 = 1.50F;
    public static final float TEST_AMOUNT_6 = 500.00F;

    @InjectMocks
    private MaximizeCouponService maximizeCouponService;

    @Test
    void calculateWhenItemsIsNullThenReturnEmpty() {
        List<String> result = maximizeCouponService.calculate(null, TEST_AMOUNT_1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void calculateWhenItemsIsEmptyThenReturnEmpty() {
        List<String> result = maximizeCouponService.calculate(new HashMap<>(), TEST_AMOUNT_1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void calculateWhenAmountIsNullThenReturnEmpty() {
        List<String> result = maximizeCouponService.calculate(Collections.singletonMap("MLA01", TEST_AMOUNT_1),
                null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void calculateWhenAmountIsZeroThenReturnEmpty() {
        List<String> result = maximizeCouponService.calculate(Collections.singletonMap("MLA01", TEST_AMOUNT_1),
                0.0F);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void calculateWhenAmountIsLessThanZeroThenReturnEmpty() {
        List<String> result = maximizeCouponService.calculate(Collections.singletonMap("MLA01", 1.0F), TEST_AMOUNT_2);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void calculateWhenOneItemAndItemIsEqualToAmountThenReturnItem() {
        List<String> result = maximizeCouponService.calculate(Collections.singletonMap("MLA01", 2.0F), TEST_AMOUNT_3);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains("MLA01"));
    }

    @Test
    void calculateWhenOneItemAndItemIsLowerThanAmountThenReturnItem() {
        List<String> result = maximizeCouponService.calculate(Collections.singletonMap("MLA01", TEST_PRICE_2),
                2.00F);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains("MLA01"));
    }

    @Test
    void calculateWhenOneItemAndItemIsGreaterThanAmountThenReturnEmpty() {
        List<String> result = maximizeCouponService.calculate(Collections.singletonMap("MLA01", TEST_PRICE_1),
                2.00F);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void calculateWhenThreeItemsAndSumIsBiggerThanAmountAndItemTwoIsTheBiggestValidThenReturnItemTwo() {
        Map<String, Float> itemToPriceMap  = new HashMap<String, Float>() {{
            put("MLA01", TEST_PRICE_2);
            put("MLA02", TEST_PRICE_3);
            put("MLA03", TEST_PRICE_4);
        }};
        List<String> result = maximizeCouponService.calculate(itemToPriceMap, TEST_AMOUNT_3);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains("MLA02"));
    }

    @Test
    void calculateWhenThreeItemsAndSumOfTwoItemsIsTheBiggestValidThenReturnTheTwoItems() {
        Map<String, Float> itemToPriceMap  = new HashMap<String, Float>() {{
            put("MLA01", TEST_PRICE_2);
            put("MLA02", TEST_PRICE_5);
            put("MLA03", TEST_PRICE_6);
        }};
        List<String> result = maximizeCouponService.calculate(itemToPriceMap, TEST_AMOUNT_4);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("MLA01"));
        assertTrue(result.contains("MLA03"));
    }

    @Test
    void calculateWhenThreeItemsAndAllItemsAreBiggerThanAmountThenReturnEmpty() {
        Map<String, Float> itemToPriceMap  = new HashMap<String, Float>() {{
            put("MLA01", TEST_PRICE_7);
            put("MLA02", TEST_PRICE_8);
            put("MLA03", TEST_PRICE_9);
        }};
        List<String> result = maximizeCouponService.calculate(itemToPriceMap, TEST_AMOUNT_4);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void calculateWhenFourItemsAndSumOfTwoItemsIsTheBiggestValidThenReturnTheTwoItems() {
        Map<String, Float> itemToPriceMap  = new HashMap<String, Float>() {{
            put("MLA01", TEST_PRICE_7);
            put("MLA02", TEST_PRICE_5);
            put("MLA03", TEST_PRICE_10);
            put("MLA04", TEST_PRICE_11);
        }};
        List<String> result = maximizeCouponService.calculate(itemToPriceMap, TEST_AMOUNT_5);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("MLA03"));
        assertTrue(result.contains("MLA04"));
    }

    @Test
    void calculateWhenFiveItemsAndSumOfFourItemsIsTheBiggestValidThenReturnTheFourItems() {
        Map<String, Float> itemToPriceMap  = new HashMap<String, Float>() {{
            put("MLA01", TEST_PRICE_12);
            put("MLA02", TEST_PRICE_13);
            put("MLA03", TEST_PRICE_14);
            put("MLA04", TEST_PRICE_15);
            put("MLA05", TEST_PRICE_16);
        }};
        List<String> result = maximizeCouponService.calculate(itemToPriceMap, TEST_AMOUNT_6);
        assertNotNull(result);
        List<String> itemIdListExpected = Arrays.asList("MLA01", "MLA02", "MLA04", "MLA05");
        assertEquals(itemIdListExpected.size(), result.size());
        assertEquals(itemIdListExpected, result);
    }
}
