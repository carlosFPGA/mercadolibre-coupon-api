package com.mercadolibre.mercadolibrecouponapi.service;

import com.mercadolibre.mercadolibrecouponapi.model.Item;
import com.mercadolibre.mercadolibrecouponapi.model.ItemGroup;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MaximizeCouponService {

    /**
     * Initial value of maximum total (Negative to considerate solution with zero and positive total sum)
     */
    public static final float INITIAL_MAXIMUM_TOTAL = -0.01F;

    /**
     * Get the list of items that maximize use of the coupon when exists solution, in other case, return an empty list.
     * @param items Map of id of possible items and their price
     * @param amount Total value of the coupon
     * @return List of id of items that maximize use of the coupon
     */
    public List<String> calculate(Map<String, Float> items, Float amount) {
        List<String> bestPossibleItemIdList = new ArrayList<>();
        //validate items are not empty and amount is not null
        if (items != null && !items.isEmpty() && amount != null) {
            //Get list of possible items that are not null
            List<Item> possibleItemList = items.entrySet().stream()
                    .filter(Objects::nonNull)
                    .map(item -> new Item(item.getKey(), item.getValue()))
                    .collect(Collectors.toList());
            //Get the best possible group
            bestPossibleItemIdList = getBestPossibleItemGroup(possibleItemList, amount).getItemIdList();
        }
        return bestPossibleItemIdList;
    }

    /**
     * Get the group of items that maximize use of the coupon when exists solution, in other case, return an empty.
     * @param itemList List of items
     * @param amount Total value of the coupon
     * @return Group of items that maximize use of the coupon
     */
    public ItemGroup getBestPossibleItemGroup(List<Item> itemList, Float amount) {
        ItemGroup bestPossibleItemGroup = new ItemGroup();
        //validate items are not empty and amount is not null
        if (itemList != null && !itemList.isEmpty() && amount != null) {
            //Get list of possible items that are not null nor greater than the coupon
            List<Item> possibleItemList = itemList.stream()
                    .filter(item -> item != null && item.getId() != null && item.getPrice() != null)
                    .filter(item -> item.getPrice() <= amount)
                    .collect(Collectors.toList());
            //Get the best possible group
            bestPossibleItemGroup =
                    evaluatePossibleItemGroup(new ItemGroup(), possibleItemList, amount, INITIAL_MAXIMUM_TOTAL);
        }
        return bestPossibleItemGroup;
    }

    /**
     * Evaluate the current group of items and sub-branches and return the best possible group of items in this
     * branch (the greatest total sum), or null when no group is lower or equals to the amount and greater than the
     * current maximum found.
     * @param itemGroup Current group of items to be evaluated
     * @param itemList List of possible items
     * @param amount Maximum total sum
     * @param currentMaximum Current Maximum total sum found
     * @return Group of items with the greatest total sum
     */
    private ItemGroup evaluatePossibleItemGroup(ItemGroup itemGroup, List<Item> itemList, Float amount, Float currentMaximum) {
        ItemGroup bestItemGroup = new ItemGroup();
        float totalGroup = itemGroup.getTotal();
        if (amount.equals(totalGroup)) {
            //Return directly the map when total of items in group is equal to the coupon
            bestItemGroup = itemGroup;
        } else if (totalGroup < amount && totalGroup > currentMaximum) {
            //Use current group initially
            bestItemGroup = new ItemGroup(itemGroup);
            float bestTotalGroup = totalGroup;
            for (Item otherItem : getItemOfOutsideGroupList(itemList, itemGroup)) {
                //Create child group in branch using items in current group and one of other items
                ItemGroup itemChildGroup = new ItemGroup(itemGroup);
                itemChildGroup.add(otherItem);
                //Get the best possible in child group
                ItemGroup bestChildGroup =
                        evaluatePossibleItemGroup(itemChildGroup, itemList, amount, totalGroup);
                if (!bestChildGroup.isEmpty()) {
                    float bestTotalChildGroup = bestChildGroup.getTotal();
                    if (bestTotalChildGroup > bestTotalGroup) {
                        //Subgroup is the biggest currently total in branch parent
                        bestTotalGroup = bestTotalChildGroup;
                        bestItemGroup = bestChildGroup;
                    }
                }
            }
        }
        return bestItemGroup;
    }

    /**
     * Get items that are not in group
     * @param itemGroup Group of items
     * @param itemList List of items
     * @return List of items that are not in group
     */
    private List<Item> getItemOfOutsideGroupList(List<Item> itemList, ItemGroup itemGroup) {
        return itemList.stream()
                .filter(i -> !itemGroup.containsItem(i))
                .collect(Collectors.toList());
    }
}
