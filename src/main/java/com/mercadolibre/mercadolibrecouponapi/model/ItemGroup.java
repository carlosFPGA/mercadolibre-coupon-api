package com.mercadolibre.mercadolibrecouponapi.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Models for a group of Items.
 * @author Carlos Parra
 */
public class ItemGroup {
    private Set<Item> itemSet;

    /**
     * Constructor for group of Items.
     */
    public ItemGroup() {
        super();
        this.itemSet = new HashSet<>();
    }

    /**
     * Constructor for group of Items.
     * @param itemSet Set of items
     */
    public ItemGroup(final Set<Item> itemSet) {
        this.itemSet = itemSet;
    }

    /**
     * Constructor for group of Items.
     * @param itemGroup Group of Items.
     */
    public ItemGroup(final ItemGroup itemGroup) {
        super();
        this.itemSet = new HashSet<>(itemGroup.getItemSet());
    }

    /**
     * Add Item to group of Items.
     * @param item Item
     */
    public void add(final Item item) {
        if (itemSet == null) {
            itemSet = new HashSet<>();
        }
        if (item != null && item.getPrice() != null) {
            itemSet.add(item);
        }
    }

    /**
     * Get set of Items.
     * @return Set of Items
     */
    public Set<Item> getItemSet() {
        return itemSet;
    }

    /**
     * Define Set of Items.
     * @param itemSet Set of Items
     */
    public void setItemSet(final Set<Item> itemSet) {
        this.itemSet = itemSet;
    }

    /**
     * Get total sum of the price of Items.
     * @return Total sum.
     */
    public float getTotal() {
        if (itemSet == null) {
            return 0;
        }
        return itemSet.stream()
                .map(Item::getPrice)
                .reduce(0.0F, Float::sum);
    }

    /**
     * Get list of Identification of Items.
     * @return List of Identification of Items
     */
    public List<String> getItemIdList() {
        if (itemSet == null) {
            return new ArrayList<>();
        }
        return itemSet.stream()
                .map(Item::getId)
                .collect(Collectors.toList());
    }

    /**
     * Check if Item is in group of Items.
     * @param item Item
     * @return True when Item is in group, in other case False
     */
    public boolean containsItem(final Item item) {
        if (itemSet == null) {
            return false;
        }
        return itemSet.stream()
                .map(Item::getId)
                .collect(Collectors.toList())
                .contains(item.getId());
    }

    /**
     * Check if group of Items is Empty.
     * @return True with group of Items is empty, in other case False.
     */
    public boolean isEmpty() {
        return itemSet.isEmpty();
    }

    /**
     * Get representation String of the group of Items.
     * @return Representation String of the group of Items
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
