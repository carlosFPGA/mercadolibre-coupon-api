package com.mercadolibre.mercadolibrecouponapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import java.util.List;

public class ItemGroupResponse {
    @JsonProperty("item_ids")
    private List<String> itemIdList;

    @JsonProperty("total")
    private Float total;

    public ItemGroupResponse() {
        super();
    }

    public ItemGroupResponse(final List<String> itemIdList, final Float total) {
        super();
        this.itemIdList = itemIdList;
        this.total = total;
    }

    public List<String> getItemIdList() {
        return itemIdList;
    }

    public void setItemIdList(final List<String> itemIdList) {
        this.itemIdList = itemIdList;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(final Float total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
