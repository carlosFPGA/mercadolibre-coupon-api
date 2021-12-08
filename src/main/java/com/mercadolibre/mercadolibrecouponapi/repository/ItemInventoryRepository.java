package com.mercadolibre.mercadolibrecouponapi.repository;

import com.mercadolibre.mercadolibrecouponapi.dto.ItemInventoryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class ItemInventoryRepository {
    private static final Logger logger = LoggerFactory.getLogger(ItemInventoryRepository.class);

    private final RestTemplate restTemplate;

    @Value("${application.itemInventory.url:https://api.mercadolibre.com/items/{itemId}}")
    private String urlItemInventory;

    public ItemInventoryRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Float getPriceByItemId(String itemId) {
        Float price = null;
        try {
            ResponseEntity<ItemInventoryResponse> responseEntity =
                    restTemplate.getForEntity(urlItemInventory, ItemInventoryResponse.class, itemId);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                ItemInventoryResponse body = responseEntity.getBody();
                if (body != null && body.getPrice() != null) {
                    price = body.getPrice();
                    logger.info("itemId: {} price: {}", itemId, price);
                }
            }
        } catch (Exception e) {
            logger.error("Error in get price for itemId: {}", itemId, e);
        }
        return price;
    }
}
