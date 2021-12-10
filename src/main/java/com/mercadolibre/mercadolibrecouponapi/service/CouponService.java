package com.mercadolibre.mercadolibrecouponapi.service;

import com.mercadolibre.mercadolibrecouponapi.model.Item;
import com.mercadolibre.mercadolibrecouponapi.model.ItemGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for calculations for a coupon.
 * @author Carlos Parra
 */
@Service
public class CouponService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CouponService.class);

    @Autowired
    private ItemInventoryService itemInventoryService;

    @Autowired
    private MaximizeCouponService maximizeCouponService;

    /**
     *  Get the group of items that maximize use of the coupon when exists solution, in other case, return an empty
     *  when there is no solution.
     * @param itemIdList List of identification of items
     * @param amount Total value of the coupon
     * @return Group of items that maximize use of the coupon
     */
    public ItemGroup getMaximumUtilizationCoupon(final List<String> itemIdList, final Float amount) {
        ItemGroup bestItemGroup = new ItemGroup();
        try {
            List<Item> itemList = itemInventoryService.getItemWithPriceList(itemIdList);
            LOGGER.debug("itemList: {}", itemList);
            bestItemGroup = maximizeCouponService.getBestPossibleItemGroup(itemList, amount);
            LOGGER.info("bestPossibleItemGroup: {}", bestItemGroup);
        } catch (Exception e) {
            LOGGER.error("Error in getMaximumUtilizationCoupon", e);
        }
        return bestItemGroup;
    }
}
