package com.mercadolibre.mercadolibrecouponapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.*;
import org.springframework.scheduling.annotation.*;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class MercadolibreCouponApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MercadolibreCouponApiApplication.class, args);
    }

}
