package com.marianf91.market.pricecomparator.service;

import com.marianf91.market.pricecomparator.dto.DiscountDto;
import com.marianf91.market.pricecomparator.model.Discount;
import com.marianf91.market.pricecomparator.repository.DiscountRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DiscountService {
    private final DiscountRepository repo;
    private final EntityLookupService lookup;  // used for create()

    public DiscountService(DiscountRepository repo,
                           EntityLookupService lookup) {
        this.repo = repo;
        this.lookup = lookup;
    }

    /**
     * No filters ⇒ return everything
     */
    public List<Discount> getAllDiscounts() {
        return repo.findAll();
    }

    /**
     * Optional filters ⇒ delegate to our single JPQL method
     */
    public List<Discount> getAllDiscounts(
            String storeName,
            String productId,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        return repo.findByFilters(storeName, productId, fromDate, toDate);
    }

    /**
     * Create new discount record
     */
    public Discount createDiscount(DiscountDto dto) {
        var store = lookup.resolveStore(dto.storeName());
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

    /**
     * Best current discounts
     */
    public List<BestDiscountDTO> getBestDiscounts() {
        LocalDate today = LocalDate.now();
        return repo.findBestCurrentDiscounts(today).stream()
                .map(arr -> {
                    var product = (com.marianf91.market.pricecomparator.model.Product) arr[0];
                    int pct = ((Number) arr[1]).intValue();
                    return new BestDiscountDTO(
                            product.getId(), product.getName(), pct
                    );
                })
                .toList();
    }
}