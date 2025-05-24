package com.marianf91.market.pricecomparator.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record DiscountDto(
        @NotBlank String productId,
        @NotBlank String storeName,
        @NotNull  @FutureOrPresent LocalDate fromDate,
        @NotNull  @FutureOrPresent LocalDate toDate,
        @Min(1)   @Max(100)       int percentage
) {}