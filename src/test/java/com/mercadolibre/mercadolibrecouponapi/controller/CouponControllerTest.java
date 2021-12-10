package com.mercadolibre.mercadolibrecouponapi.controller;

import com.mercadolibre.mercadolibrecouponapi.model.Item;
import com.mercadolibre.mercadolibrecouponapi.model.ItemGroup;
import com.mercadolibre.mercadolibrecouponapi.service.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CouponControllerTest {
    private static final String APPLICATION_TYPE = "application/json";
    public static final float TEST_PRICE = 5000.00F;
    public static final float TEST_AMOUNT_1 = 1000.00F;
    public static final float TEST_AMOUNT_2 = 10000.00F;

    private MockMvc mvc;

    @InjectMocks
    private CouponController couponController;

    @Mock
    private CouponService couponService;

    @Captor
    private ArgumentCaptor<List<String>> itemIdListCaptor;

    @BeforeEach
    void prepareMock() {
        mvc = MockMvcBuilders.standaloneSetup(couponController).build();
    }

    @Test
    void getMaximumUtilizationCouponWhenRequestMethodGetThenReturnMethodNotAllowed() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/coupon"))
                .andExpect(status().isMethodNotAllowed());

        verifyNoInteractions(couponService);
    }

    @ParameterizedTest
    @ValueSource(strings = {"{}", "{\"items_id\":[\"MCA123\"]}", "{\"amount\":1000.00}"})
    void getMaximumUtilizationCouponWhenLostSomeParameterInRequestThenReturnBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/coupon")
                        .contentType(APPLICATION_TYPE)
                        .content(""))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(couponService);
    }

    @Test
    void getMaximumUtilizationCouponWhenAmountIsInsufficientThenReturnNotFound() throws Exception {
        when(couponService.getMaximumUtilizationCoupon(anyList(), anyFloat())).thenReturn(new ItemGroup());

        mvc.perform(MockMvcRequestBuilders.post("/coupon")
                        .contentType(APPLICATION_TYPE)
                        .content("{\"item_ids\":[\"MCA123\"],\"amount\":1000.00}"))
                .andExpect(status().isNotFound());

        verify(couponService, times(1))
                .getMaximumUtilizationCoupon(itemIdListCaptor.capture(), eq(TEST_AMOUNT_1));
        List<String> itemIdList = itemIdListCaptor.getValue();
        assertNotNull(itemIdList);
        assertEquals(1, itemIdList.size());
        assertEquals("MCA123", itemIdList.get(0));

        verifyNoMoreInteractions(couponService);
    }

    @Test
    void getMaximumUtilizationCouponWhenSuccessThenReturnOk() throws Exception {
        ItemGroup itemGroup = new ItemGroup();
        itemGroup.add(new Item("MCA123", TEST_PRICE));
        when(couponService.getMaximumUtilizationCoupon(anyList(), anyFloat()))
                .thenReturn(itemGroup);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/coupon")
                        .contentType(APPLICATION_TYPE)
                        .content("{\"item_ids\":[\"MCA123\"],\"amount\":10000.00}"))
                .andExpect(status().isOk()).andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"item_ids\":[\"MCA123\"],\"total\":5000}",
                actualResponse, JSONCompareMode.LENIENT);

        verify(couponService, times(1))
                .getMaximumUtilizationCoupon(itemIdListCaptor.capture(), eq(TEST_AMOUNT_2));
        List<String> itemIdList = itemIdListCaptor.getValue();
        assertNotNull(itemIdList);
        assertEquals(1, itemIdList.size());
        assertEquals("MCA123", itemIdList.get(0));

        verifyNoMoreInteractions(couponService);
    }
}
