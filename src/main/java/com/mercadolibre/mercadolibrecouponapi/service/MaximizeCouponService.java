package com.mercadolibre.mercadolibrecouponapi.service;

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
        List<String> selectedItems = new ArrayList<>();
        //validate items are not empty and amount is more than zero
        if (items != null && !items.isEmpty() && amount != null && amount > 0) {
            float currentMaximum = -0.01F;
            //Get the best possible group
            selectedItems = new ArrayList<>(evaluatePossibleItemGroup(new HashMap<>(), items, amount, currentMaximum).keySet());
        }
        return selectedItems;
    }

    /**
     * @param itemsInGroup
     * @param items
     * @param amount
     * @param currentMaximum
     * @return
     */
    private Map<String, Float> evaluatePossibleItemGroup(Map<String, Float> itemsInGroup, Map<String, Float> items,
                                                         Float amount, Float currentMaximum) {
        Float total = itemsInGroup.values().stream().reduce(0.00F, Float::sum);
        if (amount.equals(total)) {
            //Return directly the map when total of items in group is equal to the coupon
            return itemsInGroup;
        } else if (total < amount && total > currentMaximum) {
            //Use current group initially
            Map<String, Float> selectedItems = new HashMap<>(itemsInGroup);
            float selectedTotal = total;

            Set<Map.Entry<String, Float>> otherItems
                    = items.entrySet().stream().filter(item -> !itemsInGroup.containsKey(item.getKey())).collect(Collectors.toSet());
            for (Map.Entry<String, Float> item : otherItems) {
                //Create subgroup in branch using items in current group and one of other item
                Map<String, Float> itemsInSubGroup = new HashMap<>(itemsInGroup);
                itemsInSubGroup.put(item.getKey(), item.getValue());
                //Get the best possible in subgroup
                Map<String, Float> selectedItemsInSubGroup =
                        evaluatePossibleItemGroup(itemsInSubGroup,items, amount, total);
                if (!selectedItemsInSubGroup.isEmpty()) {
                    Float totalInSubGroup = selectedItemsInSubGroup.values().stream().reduce(0.00F, Float::sum);
                    if (totalInSubGroup > selectedTotal) {
                        //Subgroup is the biggest currently total in branch parent
                        selectedTotal = totalInSubGroup;
                        selectedItems = new HashMap<>(selectedItemsInSubGroup);
                    }
                }
            }
            return selectedItems;
        } else {
            return new HashMap<>();
        }
    }
}
