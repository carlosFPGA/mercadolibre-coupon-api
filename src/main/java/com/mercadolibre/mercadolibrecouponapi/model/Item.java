package com.mercadolibre.mercadolibrecouponapi.model;

import com.google.gson.Gson;

public class Item {
    private String id;
    private Float price;

    public Item(String id, Float price) {
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
