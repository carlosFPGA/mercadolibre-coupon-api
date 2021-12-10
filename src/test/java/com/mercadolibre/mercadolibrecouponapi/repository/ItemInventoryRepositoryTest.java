package com.mercadolibre.mercadolibrecouponapi.repository;

import com.mercadolibre.mercadolibrecouponapi.dto.ItemInventoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemInventoryRepositoryTest {
    @InjectMocks
    ItemInventoryRepository itemInventoryRepository;

    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    void prepareMock() {
        itemInventoryRepository.setUrlItemInventory("https://localtest/items/{itemId}");
    }

    @Test
    void getPriceByItemId_whenErrorInCommunication_thenReturnNull() {
        String itemId = "MLA123";
        when(restTemplate.getForEntity(eq("https://localtest/items/{itemId}"), any(), eq(itemId)))
                .thenThrow(RestClientException.class);

        Float priceResult = itemInventoryRepository.getPriceByItemId(itemId);
        assertNull(priceResult);
    }

    @Test
    void getPriceByItemId_whenItemDoesNotExists_thenReturnNull() {
        String itemId = "MLA123";
        when(restTemplate.getForEntity(eq("https://localtest/items/{itemId}"), any(), eq(itemId)))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

        Float priceResult = itemInventoryRepository.getPriceByItemId(itemId);
        assertNull(priceResult);
    }

    @Test
    void getPriceByItemId_whenResponseOkButBodyIsNull_thenReturnNull() {
        String itemId = "MLA123";
        when(restTemplate.getForEntity(eq("https://localtest/items/{itemId}"), any(), eq(itemId)))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(null));

        Float priceResult = itemInventoryRepository.getPriceByItemId(itemId);
        assertNull(priceResult);
    }

    @Test
    void getPriceByItemId_whenResponseOkButPriceIsNull_thenReturnNull() {
        String itemId = "MLA123";
        ItemInventoryResponse itemResponse = new ItemInventoryResponse();
        itemResponse.setId("MLA123");
        itemResponse.setPrice(null);
        when(restTemplate.getForEntity(eq("https://localtest/items/{itemId}"), any(), eq(itemId)))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(itemResponse));

        Float priceResult = itemInventoryRepository.getPriceByItemId(itemId);
        assertNull(priceResult);
    }

    @Test
    void getPriceByItemId_whenResponseIsValid_thenReturnPrice() {
        String itemId = "MLA123";
        Float price = 10.00F;
        ItemInventoryResponse itemResponse = new ItemInventoryResponse();
        itemResponse.setId("MLA123");
        itemResponse.setPrice(price);
        when(restTemplate.getForEntity(eq("https://localtest/items/{itemId}"), any(), eq(itemId)))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(itemResponse));

        Float priceResult = itemInventoryRepository.getPriceByItemId(itemId);
        assertNotNull(priceResult);
        assertEquals(price, priceResult);
    }
}
