package com.mercadolibre.mercadolibrecouponapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class MercadolibreCouponApiApplication {

    /**
     * Main method.
     * @param args Arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(MercadolibreCouponApiApplication.class, args);
    }

}
