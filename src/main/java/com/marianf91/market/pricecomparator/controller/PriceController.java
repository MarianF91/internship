package com.marianf91.market.pricecomparator.controller;

import com.marianf91.market.pricecomparator.model.PriceSnapshot;
import com.marianf91.market.pricecomparator.service.PriceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prices")
public class PriceController {
    private final PriceService service;

    public PriceController(PriceService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PriceSnapshot> getAll(
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false) String productId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        if (storeName == null && productId == null && date == null) {
            return service.getAllPrices();
        }
        return service.getAllPrices(storeName, productId, date);
    }
}
