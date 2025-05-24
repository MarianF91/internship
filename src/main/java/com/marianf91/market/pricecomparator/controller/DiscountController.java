package com.marianf91.market.pricecomparator.controller;

import com.marianf91.market.pricecomparator.dto.DiscountCreateDto;
import com.marianf91.market.pricecomparator.dto.DiscountResponseDto;
import com.marianf91.market.pricecomparator.service.BestDiscountDTO;
import com.marianf91.market.pricecomparator.service.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/api/discounts", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Discounts", description = "Endpoints for managing product discounts")
public class DiscountController {

    private final DiscountService service;

    public DiscountController(DiscountService service) {
        this.service = service;
    }
    @Operation(
            summary = "Get all discounts, with optional filters",
            description = "Retrieve all discounts. You can filter by store, product, from/to date."
    )
    @GetMapping
    public List<DiscountResponseDto> getAll(
            @RequestParam(name = "store",    required = false) String storeName,
            @RequestParam(name = "product",  required = false) String productId,
            @RequestParam(name = "fromDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(name = "toDate",   required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        var discounts = (storeName == null && productId == null && fromDate == null && toDate == null)
                ? service.getAllDiscounts()
                : service.getAllDiscounts(storeName, productId, fromDate, toDate);

        return discounts.stream()
                .map(d -> new DiscountResponseDto(
                        d.getProduct().getId(),
                        d.getProduct().getName(),
                        d.getStore().getName(),
                        d.getFromDate(),
                        d.getToDate(),
                        d.getPercentage()
                ))
                .toList();
    }
    @Operation(
            summary = "Get the best current discounts",
            description = "Returns top products with the highest current discounts across all stores."
    )
    @GetMapping(path = "/best")
    public List<BestDiscountDTO> best() {
        return service.getBestDiscounts();
    }
    @Operation(
            summary = "Create a new discount",
            description = "Add a new discount for a product and store."
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public DiscountResponseDto create(@Valid @RequestBody DiscountCreateDto dto) {
        var created = service.createDiscount(dto);
        return new DiscountResponseDto(
                created.getProduct().getId(),
                created.getProduct().getName(),
                created.getStore().getName(),
                created.getFromDate(),
                created.getToDate(),
                created.getPercentage()
        );
    }

    /**
     * Returns all discounts created since the given date (inclusive),
     * defaulting to the last 24h if no 'since' parameter is provided.
     * Examples:
     *  - GET /api/discounts/new
     *  - GET /api/discounts/new?since=2025-05-23
     */
    @Operation(
            summary = "Get discounts created since a specific date (new discounts)",
            description = "Returns all discounts created since the provided date. If not specified, defaults to the last 24 hours."
    )
    @GetMapping(path = "/new")
    public List<DiscountResponseDto> getNew(
            @RequestParam(name = "since", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since
    ) {
        LocalDate threshold = (since != null)
                ? since
                : LocalDate.now().minusDays(1);

        return service.getNewDiscounts(threshold).stream()
                .map(d -> new DiscountResponseDto(
                        d.getProduct().getId(),
                        d.getProduct().getName(),
                        d.getStore().getName(),
                        d.getFromDate(),
                        d.getToDate(),
                        d.getPercentage()
                ))
                .toList();
    }
}