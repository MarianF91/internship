package com.marianf91.market.pricecomparator.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record DiscountCreateDto(
        @NotBlank(message = "Store name is required") String storeName,
        @NotBlank(message = "Product ID is required") String productId,
        @NotNull(message = "fromDate is required") @PastOrPresent(message = "fromDate cannot be in the future") LocalDate fromDate,
        @NotNull(message = "toDate is required") @FutureOrPresent(message = "toDate cannot be in the past") LocalDate toDate,
        @NotNull(message = "percentage is required")
        @Min(value = 1, message = "percentage must be at least 1")
        @Max(value = 100, message = "percentage must be at most 100") Integer percentage
) {}