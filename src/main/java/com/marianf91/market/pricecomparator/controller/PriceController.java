package com.marianf91.market.pricecomparator.controller;

import com.marianf91.market.pricecomparator.dto.PriceCreateDto;
import com.marianf91.market.pricecomparator.dto.PriceResponseDto;
import com.marianf91.market.pricecomparator.service.PriceService;
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
@RequestMapping(path = "/api/prices", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Prices", description = "Endpoints for managing product prices")
public class PriceController {

    private final PriceService service;

    public PriceController(PriceService service) {
        this.service = service;
    }
    @Operation(
            summary = "Get all prices, with optional filters",
            description = "Retrieve all prices. You can filter by store, product, date."
    )
    @GetMapping
    public List<PriceResponseDto> getAll(
            @RequestParam(name = "store",   required = false) String storeName,
            @RequestParam(name = "product", required = false) String productId,
            @RequestParam(name = "date",    required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        var prices = (storeName == null && productId == null && date == null)
                ? service.getAllPrices()
                : service.getAllPrices(storeName, productId, date);

        return prices.stream()
                .map(p -> new PriceResponseDto(
                        p.getProduct().getId(),
                        p.getProduct().getName(),
                        p.getStore().getName(),
                        p.getDate(),
                        p.getPrice(),
                        p.getCurrency()
                ))
                .toList();
    }
    @Operation(
            summary = "Create a new price entry",
            description = "Add a new price snapshot for a product and store."
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PriceResponseDto create(@Valid @RequestBody PriceCreateDto dto) {
        var created = service.createPrice(dto);
        return new PriceResponseDto(
                created.getProduct().getId(),
                created.getProduct().getName(),
                created.getStore().getName(),
                created.getDate(),
                created.getPrice(),
                created.getCurrency()
        );
    }
    @Operation(
            summary = "Get price history for a product",
            description = "Returns the price history for a product between two dates, optionally filtered by store."
    )
    @GetMapping("/history/{productId}")
    public List<PriceResponseDto> history(
            @PathVariable String productId,
            @RequestParam(name = "store", required = false) String storeName,
            @RequestParam(name = "start")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(name = "end")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        var snapshots = service.getPriceHistory(productId, storeName, start, end);
        return snapshots.stream()
                .map(p -> new PriceResponseDto(
                        p.getProduct().getId(),
                        p.getProduct().getName(),
                        p.getStore().getName(),
                        p.getDate(),
                        p.getPrice(),
                        p.getCurrency()
                ))
                .toList();
    }
}