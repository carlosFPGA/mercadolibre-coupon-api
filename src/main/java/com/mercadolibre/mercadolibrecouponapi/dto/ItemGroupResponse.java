package com.mercadolibre.mercadolibrecouponapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import java.util.List;

/**
 * DTO for response in Coupon Controller.
 * @author Carlos Parra
 */
public class ItemGroupResponse {
    @JsonProperty("item_ids")
    private List<String> itemIdList;

    @JsonProperty("total")
    private Float total;

    /**
     * Constructor for Response.
     */
    public ItemGroupResponse() {
        super();
    }

    /**
     * Constructor for Response.
     * @param itemIdList List of identification of Items
     * @param total Total sum of prices
     */
    public ItemGroupResponse(final List<String> itemIdList, final Float total) {
        super();
        this.itemIdList = itemIdList;
        this.total = total;
    }

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
     * Get Total sum of prices.
     * @return Total sum of prices
     */
    public Float getTotal() {
        return total;
    }

    /**
     * Set Total sum of prices.
     * @param total Total sum of prices
     */
    public void setTotal(final Float total) {
        this.total = total;
    }

    /**
     * Get representation String of Response.
     * @return Representation String of Response
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
