package com.marianf91.market.pricecomparator.service;

import com.marianf91.market.pricecomparator.dto.PriceSnapshotDto;
import com.marianf91.market.pricecomparator.model.PriceSnapshot;
import com.marianf91.market.pricecomparator.model.Store;
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
        this.repo = repo;
        this.lookup = lookup;
    }

    public List<PriceSnapshot> getAllPrices() {
        return repo.findAll();
    }

    public List<PriceSnapshot> getAllPrices(String storeName,
                                            String productId,
                                            LocalDate date) {
        if (storeName != null) {
            var store = lookup.resolveStore(storeName);
            return repo.findByStore(store);
        }
        if (productId != null) {
            var product = lookup.resolveProduct(productId);
            return repo.findByProduct(product);
        }
        if (date != null) {
            return repo.findByDate(date);
        }
        return getAllPrices();
    }

    public PriceSnapshot createPrice(PriceSnapshotDto dto) {
        var store   = lookup.resolveStore(dto.storeName());
        var product = lookup.resolveProduct(dto.productId());

        var snapshot = PriceSnapshot.builder()
                .store(store)
                .product(product)
                .date(dto.date())
                .price(dto.price())
                .currency(dto.currency())
                .build();

        return repo.save(snapshot);
    }
}