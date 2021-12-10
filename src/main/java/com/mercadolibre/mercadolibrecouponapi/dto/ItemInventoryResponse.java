package com.mercadolibre.mercadolibrecouponapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

/**
 * DTO for response from Mercado Libre API.
 * @author Carlos Parra
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemInventoryResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("price")
    private Float price;

    /**
     * Get identification of Item.
     * @return Identification
     */
    public String getId() {
        return id;
    }

    /**
     * Set identification of Item.
     * @param id Identification
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Get price of the item.
     * @return Price
     */
    public Float getPrice() {
        return price;
    }

    /**
     * Set price of the Item.
     * @param price Price of the Item
     */
    public void setPrice(final Float price) {
        this.price = price;
    }

    /**
     * Get representation String of Response of the API.
     * @return Representation String of Response
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
