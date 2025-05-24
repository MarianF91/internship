package com.marianf91.market.pricecomparator.controller;

import com.marianf91.market.pricecomparator.dto.DiscountDto;
import com.marianf91.market.pricecomparator.model.Discount;
import com.marianf91.market.pricecomparator.service.BestDiscountDTO;
import com.marianf91.market.pricecomparator.service.DiscountService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {
    private final DiscountService service;

    public DiscountController(DiscountService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Discount> getAll() {
        return service.getAllDiscounts();
    }

    @GetMapping(value = "/best", produces = APPLICATION_JSON_VALUE)
    public List<BestDiscountDTO> best() {
        return service.getBestDiscounts();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Discount> createDiscount(
            @Valid @RequestBody DiscountDto dto
    ) {
        Discount saved = service.createDiscount(dto);
        URI location = URI.create("/api/discounts/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping
    public List<Discount> getAll(
            @RequestParam(required = false) String store,
            @RequestParam(required = false) String product,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        return service.getAllDiscounts(store, product, fromDate, toDate);
    }
}