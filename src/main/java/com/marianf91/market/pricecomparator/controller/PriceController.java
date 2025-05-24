package com.marianf91.market.pricecomparator.controller;

import com.marianf91.market.pricecomparator.model.PriceSnapshot;
import com.marianf91.market.pricecomparator.service.PriceService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
public class PriceController {
    private final PriceService service;

    public PriceController(PriceService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PriceSnapshot> getAll() {
        return service.getAllPrices();
    }
}