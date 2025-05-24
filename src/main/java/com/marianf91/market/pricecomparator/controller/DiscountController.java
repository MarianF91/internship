package com.marianf91.market.pricecomparator.controller;

import com.marianf91.market.pricecomparator.model.Discount;
import com.marianf91.market.pricecomparator.service.BestDiscountDTO;
import com.marianf91.market.pricecomparator.service.DiscountService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    public List<Discount> getAll(
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false) String productId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        // if *all* filters are null, delegate to the no-arg service method
        if (storeName == null && productId == null && from == null && to == null) {
            return service.getAllDiscounts();
        }
        // otherwise use the filtering overload
        return service.getAllDiscounts(storeName, productId, from, to);
    }

    @GetMapping(value = "/best", produces = APPLICATION_JSON_VALUE)
    public List<BestDiscountDTO> best() {
        return service.getBestDiscounts();
    }
}