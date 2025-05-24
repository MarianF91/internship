package com.marianf91.market.pricecomparator.controller;

import com.marianf91.market.pricecomparator.model.Discount;
import com.marianf91.market.pricecomparator.service.BestDiscountDTO;
import com.marianf91.market.pricecomparator.service.DiscountService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

}