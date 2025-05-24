package com.marianf91.market.pricecomparator.controller;

import com.marianf91.market.pricecomparator.dto.PriceSnapshotDto;
import com.marianf91.market.pricecomparator.model.PriceSnapshot;
import com.marianf91.market.pricecomparator.service.PriceService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public List<PriceSnapshot> getAll() {
        return service.getAllPrices();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PriceSnapshot> createPrice(
            @Valid @RequestBody PriceSnapshotDto dto
    ) {
        PriceSnapshot saved = service.createPrice(dto);
        URI location = URI.create("/api/prices/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping
    public List<PriceSnapshot> getAll(
            @RequestParam(required = false) String store,
            @RequestParam(required = false) String product,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return service.getAllPrices(store, product, date);
    }

}