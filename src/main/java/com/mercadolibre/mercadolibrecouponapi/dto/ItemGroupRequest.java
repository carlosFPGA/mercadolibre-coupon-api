package com.mercadolibre.mercadolibrecouponapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import java.util.List;

public class ItemGroupRequest {
    @JsonProperty("item_ids")
    private List<String> itemIdList;

    @JsonProperty("amount")
    private Float amount;

    public List<String> getItemIdList() {
        return itemIdList;
    }

    public void setItemIdList(final List<String> itemIdList) {
        this.itemIdList = itemIdList;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(final Float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
