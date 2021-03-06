package com.mercadolibre.mercadolibrecouponapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Class for configuration of Rest Template.
 * @author Carlos Parra
 */
@Configuration
public class RestTemplateClientConfig {
    /**
     * Bean for RestTemplate.
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
