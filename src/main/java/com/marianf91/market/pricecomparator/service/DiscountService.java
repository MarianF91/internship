package com.marianf91.market.pricecomparator.service;

import com.marianf91.market.pricecomparator.model.Discount;
import com.marianf91.market.pricecomparator.repository.DiscountRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DiscountService {
    private final DiscountRepository repo;

    public DiscountService(DiscountRepository repo) {
        this.repo = repo;
    }

    public List<BestDiscountDTO> getBestDiscounts() {
        LocalDate today = LocalDate.now();
        List<Object[]> raw = repo.findBestCurrentDiscounts(today);

        return raw.stream()
                .map(arr -> {
                    var product = (com.marianf91.market.pricecomparator.model.Product) arr[0];
                    int pct = ((Number) arr[1]).intValue();
                    return new BestDiscountDTO(product.getId(), product.getName(), pct);
                })
                .toList();
    }

    public List<Discount> getAllDiscounts() {
        return repo.findAll();
    }
}