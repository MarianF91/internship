package com.marianf91.market.pricecomparator.service;

import com.marianf91.market.pricecomparator.dto.DiscountCreateDto;
import com.marianf91.market.pricecomparator.dto.DiscountResponseDto;
import com.marianf91.market.pricecomparator.model.Discount;
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
                                          LocalDate fromDate,
                                          LocalDate toDate) {
        if (storeName == null && productId == null && fromDate == null && toDate == null) {
            return getAllDiscounts();
        }
        return repo.findByFilters(storeName, productId, fromDate, toDate);
    }

    public Discount createDiscount(DiscountCreateDto dto) {
        var store   = lookup.resolveStore(dto.storeName());
        var product = lookup.resolveProduct(dto.productId());
        var ent     = Discount.builder()
                .store(store)
                .product(product)
                .fromDate(dto.fromDate())
                .toDate(dto.toDate())
                .percentage(dto.percentage())
                .build();
        return repo.save(ent);
    }

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

    public List<Discount> getNewDiscounts(LocalDate since) {
        return repo.findByFromDateAfter(since);
    }

    public List<DiscountResponseDto> getNewDiscountsLast24h() {
        LocalDate since = LocalDate.now().minusDays(1);
        var discounts = repo.findByFromDateAfter(since);
        return discounts.stream()
                .map(d -> new DiscountResponseDto(
                        d.getProduct().getId(),
                        d.getProduct().getName(),
                        d.getStore().getName(),
                        d.getFromDate(),
                        d.getToDate(),
                        d.getPercentage()
                ))
                .toList();
    }
}