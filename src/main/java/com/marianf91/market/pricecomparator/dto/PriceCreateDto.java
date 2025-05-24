package com.marianf91.market.pricecomparator.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PriceCreateDto(
        @NotBlank(message = "Store name is required") String storeName,
        @NotBlank(message = "Product ID is required") String productId,
        @NotNull(message = "date is required")
        @PastOrPresent(message = "date cannot be in the future") LocalDate date,
        @NotNull(message = "price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "price must be positive") BigDecimal price,
        @NotBlank(message = "currency is required") String currency
) {}