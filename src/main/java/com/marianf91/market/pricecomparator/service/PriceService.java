package com.marianf91.market.pricecomparator.service;

import com.marianf91.market.pricecomparator.dto.PriceCreateDto;
import com.marianf91.market.pricecomparator.model.PriceSnapshot;
import com.marianf91.market.pricecomparator.repository.PriceSnapshotRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class PriceService {
    private final PriceSnapshotRepository repo;
    private final EntityLookupService lookup;

    public PriceService(PriceSnapshotRepository repo,
                        EntityLookupService lookup) {
        this.repo   = repo;
        this.lookup = lookup;
    }

    public List<PriceSnapshot> getAllPrices() {
        return repo.findAll();
    }

    public List<PriceSnapshot> getAllPrices(String storeName,
                                            String productId,
                                            LocalDate date) {
        return repo.findByFilters(storeName, productId, date);
    }

    public PriceSnapshot createPrice(PriceCreateDto dto) {
        var store   = lookup.resolveStore(dto.storeName());
        var product = lookup.resolveProduct(dto.productId());
        var ent     = PriceSnapshot.builder()
                .store(store)
                .product(product)
                .date(dto.date())
                .price(dto.price())
                .currency(dto.currency())
                .build();
        return repo.save(ent);
    }

    public List<PriceSnapshot> getPriceHistory(String productId,
                                               String storeName,
                                               LocalDate start,
                                               LocalDate end) {
        var product = lookup.resolveProduct(productId);
        List<PriceSnapshot> list =
                repo.findByProductAndDateBetween(product, start, end);
        if (storeName != null) {
            list = list.stream()
                    .filter(p -> p.getStore().getName()
                            .equalsIgnoreCase(storeName))
                    .toList();
        }
        return list;
    }
}