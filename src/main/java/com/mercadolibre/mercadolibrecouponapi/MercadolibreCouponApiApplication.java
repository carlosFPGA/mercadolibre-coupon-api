package com.mercadolibre.mercadolibrecouponapi;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.servers.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
        servers = {
                @Server(url = "/", description = "Default Server URL"),
                @Server(url = "https://mercadolibre-coupon-api.uc.r.appspot.com/", description = "Google App Engine URL")
        }
)
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
