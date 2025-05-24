package com.marianf91.market.pricecomparator.service;

import com.marianf91.market.pricecomparator.dto.DiscountDto;
import com.marianf91.market.pricecomparator.model.Discount;
import com.marianf91.market.pricecomparator.service.BestDiscountDTO;
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

    /** All discounts */
    public List<Discount> getAllDiscounts() {
        return repo.findAll();
    }

    /**
     * Filters discounts using optional parameters.
     * If all parameters are null, returns repo.findAll().
     */
    public List<Discount> getAllDiscounts(
            String storeName,
            String productId,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        if (storeName == null && productId == null && fromDate == null && toDate == null) {
            return getAllDiscounts();
        }
        return repo.findByFilters(
                storeName,
                productId,
                fromDate,
                toDate
        );
    }

    /** Creates a new discount from DTO */
    public Discount createDiscount(DiscountDto dto) {
        var store    = lookup.resolveStore(dto.storeName());
        var product  = lookup.resolveProduct(dto.productId());

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
     * Returns the best discount today, per product.
     * Returns a DTO with the ID and max percentage.
     */
    public List<BestDiscountDTO> getBestDiscounts() {
        LocalDate today = LocalDate.now();
        return repo.findBestCurrentDiscounts(today).stream()
                .map(arr -> {
                    var product = (com.marianf91.market.pricecomparator.model.Product) arr[0];
                    int pct     = ((Number) arr[1]).intValue();
                    return new BestDiscountDTO(
                            product.getId(),
                            product.getName(),
                            pct
                    );
                })
                .toList();
    }
}
