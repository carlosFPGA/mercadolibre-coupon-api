package com.mercadolibre.mercadolibrecouponapi.cache;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CleanCacheTaskTest {
    @InjectMocks
    private CleanCacheTask cleanCacheTask;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @Test
    void cleanAllCacheWhenThereAreNotCacheThenDoNothing() {
        when(cacheManager.getCacheNames()).thenReturn(new ArrayList<>());

        cleanCacheTask.cleanAllCache();

        verify(cacheManager, times(1)).getCacheNames();
        verify(cacheManager, never()).getCache(anyString());
        verifyNoMoreInteractions(cacheManager);
    }

    @Test
    void cleanAllCacheWhenThereIsOneCacheThenCleanCache() {
        when(cacheManager.getCacheNames()).thenReturn(Collections.singletonList("itemInventoryCache"));
        when(cacheManager.getCache("itemInventoryCache")).thenReturn(cache);

        cleanCacheTask.cleanAllCache();

        verify(cacheManager, times(1)).getCacheNames();
        verify(cacheManager, times(1)).getCache("itemInventoryCache");
        verifyNoMoreInteractions(cacheManager);

        verify(cache, times(1)).clear();
        verifyNoMoreInteractions(cache);
    }
}
