package com.marianf91.market.pricecomparator.service;

public record BestDiscountDTO(
        String productId, String productName, int percentage
) {
}