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

/**
 * Repository for get price of Items using Mercado Libre API.
 * @author Carlos Parra
 */
@Repository
public class ItemInventoryRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemInventoryRepository.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${application.itemInventory.url:https://api.mercadolibre.com/items/{itemId}}")
    private String urlItemInventory;

    /**
     * Get price for item using Mercado Libre API, in case of error in communication
     * or structure of the response return null.
     * @param itemId Identification of the item
     * @return Price of the item
     */
    public Float getPriceByItemId(final String itemId) {
        Float price = null;
        try {
            ResponseEntity<ItemInventoryResponse> responseEntity =
                    restTemplate.getForEntity(urlItemInventory, ItemInventoryResponse.class, itemId);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                ItemInventoryResponse item = responseEntity.getBody();
                if (item != null && item.getPrice() != null) {
                    price = item.getPrice();
                    LOGGER.info("Get price: {} for itemId: {}", price, itemId);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error in getting price from Mercado Libre API for itemId: {}", itemId, e);
        }
        return price;
    }

    /**
     * Set Url of Mercado Libre API for getting price for an item.
     * @param urlItemInventory Url of Mercado Libre API
     */
    public void setUrlItemInventory(final String urlItemInventory) {
        this.urlItemInventory = urlItemInventory;
    }
}
