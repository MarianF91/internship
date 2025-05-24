package com.marianf91.market.pricecomparator.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PriceSnapshotDto(
        @NotBlank String productId,
        @NotBlank String storeName,
        @NotNull  @PastOrPresent LocalDate date,
        @NotNull  @Positive BigDecimal price,
        @NotBlank String currency
) {}