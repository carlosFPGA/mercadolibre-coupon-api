package com.mercadolibre.mercadolibrecouponapi.cache;

import com.mercadolibre.mercadolibrecouponapi.repository.ItemInventoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class ItemInventoryCacheTest {

    public static final float TEST_PRICE_1 = 10.00F;
    public static final float TEST_PRICE_2 = 20.00F;
    public static final float TEST_PRICE_3 = 30.00F;
    public static final float TEST_PRICE_4 = 40.00F;
    public static final float TEST_PRICE_5 = 50.00F;
    public static final float TEST_PRICE_6 = 60.00F;
    public static final float TEST_PRICE_7 = 70.00F;
    public static final float TEST_PRICE_8 = 80.00F;

    @TestConfiguration
    @EnableCaching
    static class Config {
        @Bean
        CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("itemInventoryCache");
        }

        @Bean
        ItemInventoryCache itemInventoryCache() {
            return new ItemInventoryCache();
        }

        @Bean
        ItemInventoryRepository inventoryRepository() {
            return Mockito.mock(ItemInventoryRepository.class);
        }

        @Bean
        RestTemplate restTemplate() {
            return Mockito.mock(RestTemplate.class);
        }
    }

    @Autowired
    private ItemInventoryRepository inventoryRepository;

    @Autowired
    private CacheManager manager;

    @Autowired
    private ItemInventoryCache cache;

    @BeforeEach
    void prepareMock() {
        Objects.requireNonNull(manager.getCache("itemInventoryCache")).clear();
    }

    @AfterEach
    void clearMock() {
        Objects.requireNonNull(manager.getCache("itemInventoryCache")).clear();
    }

    @Test
    void getPriceByItemIdWhenPreviousValueThenReturnValueCached() {
        String itemId = "MLA123";
        //Mock two results
        float firstValue = TEST_PRICE_1;
        float secondValue = TEST_PRICE_2;
        when(inventoryRepository.getPriceByItemId(anyString())).thenReturn(firstValue, secondValue);

        //First invocation returns value returned by the method
        verifyValueReturned(itemId, firstValue);

        //Check value in cache
        verifyValueIsInCache(itemId, firstValue);

        //Second invocation should be return cached value
        verifyValueReturned(itemId, firstValue);

        //Verify inventoryRepository was invoked once
        verify(inventoryRepository, times(1)).getPriceByItemId(itemId);
        verifyNoMoreInteractions(inventoryRepository);
    }

    @Test
    void getPriceByItemIdWhenClearCacheCalledThenReturnValueNotCached() {
        String itemId = "MLA124";
        //Mock two results
        float firstValue = TEST_PRICE_3;
        float secondValue = TEST_PRICE_4;
        when(inventoryRepository.getPriceByItemId(anyString())).thenReturn(firstValue, secondValue);

        //First invocation returns value returned by the method
        verifyValueReturned(itemId, firstValue);

        //Clear cache
        cache.releaseAllPrices();

        //Check there are not values in cache
        verifyValueIsNotInCache(itemId);

        //Second invocation should be return value returned by the method
        verifyValueReturned(itemId, secondValue);

        //Verify inventoryRepository was invoked twice
        verify(inventoryRepository, times(2)).getPriceByItemId(itemId);
        verifyNoMoreInteractions(inventoryRepository);
    }

    @Test
    void getPriceByItemIdWhenClearSpecificCacheCalledThenReturnValueNotCached() {
        String itemId = "MLA125";
        //Mock two results
        float firstValue = TEST_PRICE_5;
        float secondValue = TEST_PRICE_6;
        when(inventoryRepository.getPriceByItemId(anyString())).thenReturn(firstValue, secondValue);

        //First invocation returns value returned by the method
        verifyValueReturned(itemId, firstValue);

        //Clear cache
        cache.releasePriceByItemId(itemId);

        //Check there are not values in cache
        verifyValueIsNotInCache(itemId);

        //Second invocation should be return value returned by the method
        verifyValueReturned(itemId, secondValue);

        //Verify inventoryRepository was invoked twice
        verify(inventoryRepository, times(2)).getPriceByItemId(itemId);
        verifyNoMoreInteractions(inventoryRepository);
    }

    @Test
    void getPriceByItemIdWhenClearOtherSpecificCacheCalledThenReturnValueCached() {
        String itemId = "MLA126";
        String otherItemId = "MLA345";
        //Mock two results
        float firstValue = TEST_PRICE_7;
        float secondValue = TEST_PRICE_8;
        when(inventoryRepository.getPriceByItemId(anyString())).thenReturn(firstValue, secondValue);

        //First invocation returns value returned by the method
        verifyValueReturned(itemId, firstValue);

        //Clear cache
        cache.releasePriceByItemId(otherItemId);

        //Check value is in cache
        verifyValueIsInCache(itemId, firstValue);
        //Check other value is not in cache
        verifyValueIsNotInCache(otherItemId);

        //Second invocation should be return value returned by cache
        verifyValueReturned(itemId, firstValue);

        //Verify inventoryRepository was invoked twice
        verify(inventoryRepository, times(1)).getPriceByItemId(itemId);
        verifyNoMoreInteractions(inventoryRepository);
    }

    private void verifyValueIsInCache(final String itemId, final Float expected) {
        Cache.ValueWrapper valueWrapper = Objects.requireNonNull(manager.getCache("itemInventoryCache"))
                .get(itemId);
        assertNotNull(valueWrapper);
        assertNotNull(valueWrapper.get());
        assertEquals(expected, (Float) valueWrapper.get());
    }

    private void verifyValueReturned(final String itemId, final float expected) {
        Float firstResult = cache.getPriceByItemId(itemId);
        assertNotNull(firstResult);
        assertEquals(expected, firstResult);
    }

    private void verifyValueIsNotInCache(final String itemId) {
        Cache.ValueWrapper valueWrapper = Objects.requireNonNull(manager.getCache("itemInventoryCache"))
                .get(itemId);
        assertNull(valueWrapper);
    }
}
