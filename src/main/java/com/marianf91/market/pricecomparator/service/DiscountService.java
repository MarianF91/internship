package com.marianf91.market.pricecomparator.service;

import com.marianf91.market.pricecomparator.dto.DiscountDto;
import com.marianf91.market.pricecomparator.model.Discount;
import com.marianf91.market.pricecomparator.model.Product;
import com.marianf91.market.pricecomparator.model.Store;
import com.marianf91.market.pricecomparator.repository.DiscountRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DiscountService {
    private final DiscountRepository repo;
    private final EntityLookupService lookup;

    public DiscountService(DiscountRepository repo,
                           EntityLookupService lookup) {
        this.repo   = repo;
        this.lookup = lookup;
    }

    public List<Discount> getAllDiscounts() {
        return repo.findAll();
    }

    public List<Discount> getAllDiscounts(String storeName,
                                          String productId,
                                          LocalDate from,
                                          LocalDate to) {
        if (storeName != null) {
            var store = lookup.resolveStore(storeName);
            return repo.findByStore(store);
        }
        if (productId != null) {
            var product = lookup.resolveProduct(productId);
            return repo.findByProduct(product);
        }
        if (from != null && to != null) {
            return repo.findByFromDateBetween(from, to);
        }
        return getAllDiscounts();
    }

    public Discount createDiscount(DiscountDto dto) {
        var store   = lookup.resolveStore(dto.storeName());
        var product = lookup.resolveProduct(dto.productId());

        var discount = Discount.builder()
                .store(store)
                .product(product)
                .fromDate(dto.fromDate())
                .toDate(dto.toDate())
                .percentage(dto.percentage())
                .build();

        return repo.save(discount);
    }

    public List<BestDiscountDTO> getBestDiscounts() {
        LocalDate today = LocalDate.now();
        return repo.findBestCurrentDiscounts(today).stream()
                .map(arr -> {
                    var product = (Product) arr[0];
                    int pct     = ((Number) arr[1]).intValue();
                    return new BestDiscountDTO(
                            product.getId(), product.getName(), pct
                    );
                })
                .toList();
    }
}