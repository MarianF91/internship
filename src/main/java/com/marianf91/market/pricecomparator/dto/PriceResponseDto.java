package com.marianf91.market.pricecomparator.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PriceResponseDto(
        String productId,
        String productName,
        String storeName,
        LocalDate date,
        BigDecimal price,
        String currency
) {}