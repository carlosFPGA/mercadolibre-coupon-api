package com.mercadolibre.mercadolibrecouponapi.repository;

import com.mercadolibre.mercadolibrecouponapi.dto.ItemInventoryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class ItemInventoryRepository {
    private static final Logger logger = LoggerFactory.getLogger(ItemInventoryRepository.class);

    @Autowired
    RestTemplate restTemplate;

    @Value("${application.itemInventory.url:https://api.mercadolibre.com/items/{itemId}}")
    private String urlItemInventory;

    /**
     * Get price for item using Mercado Libre API, in case of error in communication
     * or structure of the response return null
     * @param itemId Identification of the item
     * @return Price of the item
     */
    public Float getPriceByItemId(String itemId) {
        Float price = null;
        try {
            ResponseEntity<ItemInventoryResponse> responseEntity =
                    restTemplate.getForEntity(urlItemInventory, ItemInventoryResponse.class, itemId);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                ItemInventoryResponse item = responseEntity.getBody();
                if (item != null && item.getPrice() != null) {
                    price = item.getPrice();
                    logger.info("Get price: {} for itemId: {}", price, itemId);
                }
            }
        } catch (Exception e) {
            logger.error("Error in getting price from Mercado Libre API for itemId: {}", itemId, e);
        }
        return price;
    }

    public void setUrlItemInventory(String urlItemInventory) {
        this.urlItemInventory = urlItemInventory;
    }
}
