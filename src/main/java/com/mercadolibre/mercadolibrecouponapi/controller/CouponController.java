package com.mercadolibre.mercadolibrecouponapi.controller;

import com.mercadolibre.mercadolibrecouponapi.dto.ItemListRequest;
import com.mercadolibre.mercadolibrecouponapi.dto.ItemListResponse;
import com.mercadolibre.mercadolibrecouponapi.service.ItemInventoryService;
import com.mercadolibre.mercadolibrecouponapi.service.MaximizeCouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
class CouponController {
    private static final Logger logger = LoggerFactory.getLogger(CouponController.class);

    private static final Marker START_MARK = MarkerFactory.getMarker("START");
    private static final Marker FINISH_MARK = MarkerFactory.getMarker("FINISHED_PROCESS");

    final ItemInventoryService itemInventoryService;

    final MaximizeCouponService maximizeCouponService;

    public CouponController(ItemInventoryService itemInventoryService, MaximizeCouponService maximizeCouponService) {
        this.itemInventoryService = itemInventoryService;
        this.maximizeCouponService = maximizeCouponService;
    }

    @PostMapping(value = "/coupon", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemListResponse> getMaximumUtilizationCoupon(@RequestBody ItemListRequest itemListRequest) {
        logger.info(START_MARK, "STARTED getMaximumUtilizationCoupon");
        if (itemListRequest == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        logger.info("Quantity of items: {} amount: {}", itemListRequest.getItemIdList().size(),
                itemListRequest.getAmount());
        logger.debug("Request: {}", itemListRequest);
        Map<String, Float> priceOfItems = itemInventoryService.getPriceOfItems(itemListRequest.getItemIdList());
        logger.info("priceOfItems: {}", priceOfItems);
        if (priceOfItems == null || priceOfItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<String> itemsId = maximizeCouponService.calculate(priceOfItems, itemListRequest.getAmount());
        logger.info("itemsId: {}", itemsId);
        if (itemsId == null || itemsId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Float total = priceOfItems.entrySet().stream().filter(e -> itemsId.contains(e.getKey()))
                .map(Map.Entry::getValue).reduce(0.0F, Float::sum);

        ItemListResponse itemListResponse = new ItemListResponse();
        itemListResponse.setItemIdList(itemsId);
        itemListResponse.setTotal(total);

        logger.info(FINISH_MARK, "FINISHED getMaximumUtilizationCoupon");
        return ResponseEntity.status(HttpStatus.OK).body(itemListResponse);

    }
}
