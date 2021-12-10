package com.mercadolibre.mercadolibrecouponapi.model;

import com.google.gson.Gson;

import java.util.Objects;

public class Item {
    private String id;
    private Float price;

    /**
     * Constructor of the Item.
     * @param id Identification
     * @param price Price
     */
    public Item(final String id, final Float price) {
        super();
        this.id = id;
        this.price = price;
    }

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
     * Get price of the Item.
     * @return Price
     */
    public Float getPrice() {
        return price;
    }

    /**
     * Set price of the Item.
     * @param price Price
     */
    public void setPrice(final Float price) {
        this.price = price;
    }

    /**
     * Check If two Items are equals.
     * @param o Object
     * @return True when the two Items are equals, in other case False
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return Objects.equals(id, item.id) && Objects.equals(price, item.price);
    }

    /**
     * Get hashCode of the Item.
     * @return HashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, price);
    }

    /**
     * Representation String of the Item.
     * @return String
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
