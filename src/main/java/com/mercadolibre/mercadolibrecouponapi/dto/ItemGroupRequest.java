package com.mercadolibre.mercadolibrecouponapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import java.util.List;

/**
 * DTO for request in Coupon Controller.
 * @author Carlos Parra
 */
public class ItemGroupRequest {
    @JsonProperty("item_ids")
    private List<String> itemIdList;

    @JsonProperty("amount")
    private Float amount;

    /**
     * Get list of identification of Items.
     * @return List of identification of Items
     */
    public List<String> getItemIdList() {
        return itemIdList;
    }

    /**
     * Set list of identification of Items.
     * @param itemIdList List of identification of Items
     */
    public void setItemIdList(final List<String> itemIdList) {
        this.itemIdList = itemIdList;
    }

    /**
     * Get amount total of the coupon.
     * @return Amount total of the coupon
     */
    public Float getAmount() {
        return amount;
    }

    /**
     * Set amount total of the coupon.
     * @param amount Amount total of the coupon
     */
    public void setAmount(final Float amount) {
        this.amount = amount;
    }

    /**
     * Get representation String of request.
     * @return Representation String of request
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
