package com.mercadolibre.mercadolibrecouponapi.service;

import com.mercadolibre.mercadolibrecouponapi.model.Item;
import com.mercadolibre.mercadolibrecouponapi.model.ItemGroup;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MaximizeCouponService {

    /**
     * Get the list of items that maximize use of the coupon when exists solution, in other case, return an empty list.
     * @param items Map of id of possible items and their price
     * @param amount Total value of the coupon
     * @return List of id of items that maximize use of the coupon
     */
    public List<String> calculate(Map<String, Float> items, Float amount) {
        List<String> bestPossibleItemIdGroup = new ArrayList<>();
        //validate items are not empty and amount is more than zero
        if (items != null && !items.isEmpty() && amount != null && amount > 0) {
            List<Item> possibleItems = items.entrySet().stream()
                    .map(i -> new Item(i.getKey(), i.getValue()))
                    .collect(Collectors.toList());
            float currentMaximum = -0.01F;
            //Get the best possible group
            bestPossibleItemIdGroup =
                    evaluatePossibleItemGroup(new ItemGroup(), possibleItems,
                            amount, currentMaximum)
                            .getItemsId();
        }
        return bestPossibleItemIdGroup;
    }


    private ItemGroup evaluatePossibleItemGroup(ItemGroup itemGroup, List<Item> items,
                                                Float amount, Float currentMaximum) {
        ItemGroup bestPossibleItemGroup = new ItemGroup();
        float totalGroup = itemGroup.getTotal();
        if (amount.equals(totalGroup)) {
            //Return directly the map when total of items in group is equal to the coupon
            bestPossibleItemGroup = itemGroup;
        } else if (totalGroup < amount && totalGroup > currentMaximum) {
            //Use current group initially
            bestPossibleItemGroup = new ItemGroup(itemGroup);
            float bestPossibleTotalGroup = totalGroup;
            List<Item> otherItems = items.stream()
                    .filter(i -> !itemGroup.containsItem(i))
                    .collect(Collectors.toList());
            for (Item otherItem : otherItems) {
                //Create subgroup in branch using items in current group and one of other item
                ItemGroup itemsSubGroup = new ItemGroup(itemGroup);
                itemsSubGroup.add(otherItem);
                //Get the best possible in subgroup
                ItemGroup bestPossibleItemsSubGroup =
                        evaluatePossibleItemGroup(itemsSubGroup, items, amount, totalGroup);
                if (!bestPossibleItemsSubGroup.isEmpty()) {
                    float bestPossibleTotalSubGroup = bestPossibleItemsSubGroup.getTotal();
                    if (bestPossibleTotalSubGroup > bestPossibleTotalGroup) {
                        //Subgroup is the biggest currently total in branch parent
                        bestPossibleTotalGroup = bestPossibleTotalSubGroup;
                        bestPossibleItemGroup = bestPossibleItemsSubGroup;
                    }
                }
            }
        }
        return bestPossibleItemGroup;
    }
}
