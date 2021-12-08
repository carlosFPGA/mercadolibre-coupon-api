package com.mercadolibre.mercadolibrecouponapi.model;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemGroup {
    private Set<Item> items;

    public ItemGroup() {
        this.items = new HashSet<>();
    }

    public ItemGroup(Set<Item> items) {
        this.items = items;
    }

    public ItemGroup(ItemGroup itemGroup) {
        this.items = new HashSet<>(itemGroup.getItems());
    }

    public void add(Item item) {
        if(items == null) {
            items = new HashSet<>();
        }
        if(item != null && item.getPrice() != null) {
            items.add(item);
        }
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public float getTotal(){
        if (items == null) {
            return 0;
        }
        return items.stream()
                .map(Item::getPrice)
                .reduce(0.0F, Float::sum);
    }

    public List<String> getItemsId() {
        return items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());
    }

    public boolean containsItem(Item item) {
        return items.stream()
                .map(Item::getId)
                .collect(Collectors.toList())
                .contains(item.getId());
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
