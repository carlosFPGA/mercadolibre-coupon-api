package com.mercadolibre.mercadolibrecouponapi.service;

import com.mercadolibre.mercadolibrecouponapi.model.Item;
import com.mercadolibre.mercadolibrecouponapi.model.ItemGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {
    private static final Logger logger = LoggerFactory.getLogger(CouponService.class);

    @Autowired
    ItemInventoryService itemInventoryService;

    @Autowired
    MaximizeCouponService maximizeCouponService;

    /**
     *  Get the group of items that maximize use of the coupon when exists solution, in other case, return an empty
     *  when there is no solution.
     * @param itemIdList List of identification of items
     * @param amount Total value of the coupon
     * @return Group of items that maximize use of the coupon
     */
    public ItemGroup getMaximumUtilizationCoupon(List<String> itemIdList, Float amount) {
        ItemGroup bestItemGroup = new ItemGroup();
        try {
            List<Item> itemList = itemInventoryService.getItemWithPriceList(itemIdList);
            logger.debug("itemList: {}", itemList);
            bestItemGroup = maximizeCouponService.getBestPossibleItemGroup(itemList, amount);
            logger.info("bestPossibleItemGroup: {}", bestItemGroup);
        } catch (Exception e) {
            logger.error("Error in getMaximumUtilizationCoupon", e);
        }
        return bestItemGroup;
    }
}
