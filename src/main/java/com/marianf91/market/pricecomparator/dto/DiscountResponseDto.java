package com.marianf91.market.pricecomparator.dto;

import java.time.LocalDate;

public record DiscountResponseDto(
        String productId,
        String productName,
        String storeName,
        LocalDate fromDate,
        LocalDate toDate,
        Integer percentage
) {}