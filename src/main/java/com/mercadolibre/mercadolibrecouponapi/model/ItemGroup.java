package com.mercadolibre.mercadolibrecouponapi.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemGroup {
    private Set<Item> itemSet;

    public ItemGroup() {
        super();
        this.itemSet = new HashSet<>();
    }

    public ItemGroup(ItemGroup itemGroup) {
        super();
        this.itemSet = new HashSet<>(itemGroup.getItemSet());
    }

    public void add(Item item) {
        if(itemSet == null) {
            itemSet = new HashSet<>();
        }
        if(item != null && item.getPrice() != null) {
            itemSet.add(item);
        }
    }

    public Set<Item> getItemSet() {
        return itemSet;
    }

    public void setItemSet(Set<Item> itemSet) {
        this.itemSet = itemSet;
    }

    public float getTotal(){
        if (itemSet == null) {
            return 0;
        }
        return itemSet.stream()
                .map(Item::getPrice)
                .reduce(0.0F, Float::sum);
    }

    public List<String> getItemIdList() {
        if (itemSet == null) {
            return new ArrayList<>();
        }
        return itemSet.stream()
                .map(Item::getId)
                .collect(Collectors.toList());
    }

    public boolean containsItem(Item item) {
        if (itemSet == null) {
            return false;
        }
        return itemSet.stream()
                .map(Item::getId)
                .collect(Collectors.toList())
                .contains(item.getId());
    }

    public boolean isEmpty() {
        return itemSet.isEmpty();
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
