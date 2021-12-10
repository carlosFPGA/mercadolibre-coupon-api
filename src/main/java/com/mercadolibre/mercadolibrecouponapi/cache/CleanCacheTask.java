package com.mercadolibre.mercadolibrecouponapi.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CleanCacheTask {
    private static final Logger logger = LoggerFactory.getLogger(CleanCacheTask.class);
    @Autowired
    private CacheManager cacheManager;

    @Scheduled(fixedRateString = "${clear.cache.fixed.rate:3600000}", initialDelayString = "${clear.cache.init.delay:3600000}")
    public void cleanAllCache() {
        cacheManager.getCacheNames()
                .parallelStream()
                .forEach(name -> {
                    logger.info("Scheduled Clean Cache : {}", name);
                    Objects.requireNonNull(cacheManager.getCache(name)).clear();
                });
    }
}
