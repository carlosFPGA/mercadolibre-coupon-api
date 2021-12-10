package com.mercadolibre.mercadolibrecouponapi.controller;

import com.mercadolibre.mercadolibrecouponapi.cache.ItemInventoryCache;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CleanCacheControllerTest {
    private final String APPLICATION_TYPE = "application/json";

    private MockMvc mvc;

    @InjectMocks
    CleanCacheController cleanCacheController;

    @Mock
    ItemInventoryCache itemInventoryCache;

    @BeforeEach
    void prepareMock() {
        mvc = MockMvcBuilders.standaloneSetup(cleanCacheController).build();
    }

    @Test
    void releasePriceByItemId_whenRequestMethodPost_ReturnMethodNotAllowed() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/cleanCache/price/MLA123"))
                .andExpect(status().isMethodNotAllowed());

        verifyNoInteractions(itemInventoryCache);
    }

    @Test
    void releasePriceByItemId_whenItemIdIsValid_ReturnOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/cleanCache/price/MLA123"))
                .andExpect(status().isOk());

        verify(itemInventoryCache, times(1)).releasePriceByItemId("MLA123");
        verifyNoMoreInteractions(itemInventoryCache);
    }

    @Test
    void releaseAllPrices_whenRequestMethodPost_ReturnMethodNotAllowed() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/cleanCache/allPrices"))
                .andExpect(status().isMethodNotAllowed());

        verifyNoInteractions(itemInventoryCache);
    }

    @Test
    void releaseAllPrices_whenRequestIsValid_ReturnOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/cleanCache/allPrices"))
                .andExpect(status().isOk());

        verify(itemInventoryCache, times(1)).releaseAllPrices();
        verifyNoMoreInteractions(itemInventoryCache);
    }
}
