package com.mercadolibre.mercadolibrecouponapi.controller;

import com.mercadolibre.mercadolibrecouponapi.dto.ItemGroupRequest;
import com.mercadolibre.mercadolibrecouponapi.dto.ItemGroupResponse;
import com.mercadolibre.mercadolibrecouponapi.model.ItemGroup;
import com.mercadolibre.mercadolibrecouponapi.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

@RestController
class CouponController {
    private static final Logger logger = LoggerFactory.getLogger(CouponController.class);

    private static final Marker START_MARK = MarkerFactory.getMarker("START");
    private static final Marker FINISH_MARK = MarkerFactory.getMarker("FINISHED_PROCESS");

    @Autowired
    CouponService couponService;

    /**
     * Endpoint for get best group of items to maximize utilization of a coupon
     * @param request Request with list of id of items and amount
     * @return Http OK with list of id of items and total sum for the best utilization of the coupon
     */
    @PostMapping(value = "/coupon", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemGroupResponse> getMaximumUtilizationCoupon(@RequestBody ItemGroupRequest request) {
        logger.info(START_MARK, "STARTED getMaximumUtilizationCoupon");
        Instant start = Instant.now();
        //Validate that the parameters are valid
        if (request == null || request.getItemIdList() == null || request.getAmount() == null) {
            logger.info(FINISH_MARK, "FINISHED getMaximumUtilizationCoupon with Bad Request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ItemGroupResponse());
        }
        logger.info("Quantity of items: {} amount: {}", request.getItemIdList().size(), request.getAmount());
        logger.debug("Request: {}", request);

        ItemGroup bestItemGroup =
                couponService.getMaximumUtilizationCoupon(request.getItemIdList(), request.getAmount());

        //Validate that the best group exists
        if(bestItemGroup == null || bestItemGroup.isEmpty()){
            logger.info(FINISH_MARK, "FINISHED getMaximumUtilizationCoupon with Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ItemGroupResponse(new ArrayList<>(), 0.0F));
        }
        //Map ItemGroup to ItemsResponse DTO
        ItemGroupResponse response = new ItemGroupResponse(bestItemGroup.getItemIdList(), bestItemGroup.getTotal());

        long time = Duration.between(start, Instant.now()).toMillis();
        logger.info(FINISH_MARK, "FINISHED getMaximumUtilizationCoupon in ms : {}", time);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
