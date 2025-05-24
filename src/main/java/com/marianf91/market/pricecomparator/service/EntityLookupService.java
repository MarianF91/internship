package com.marianf91.market.pricecomparator.service;

import com.marianf91.market.pricecomparator.model.Product;
import com.marianf91.market.pricecomparator.model.Store;
import com.marianf91.market.pricecomparator.repository.ProductRepository;
import com.marianf91.market.pricecomparator.repository.StoreRepository;
import org.springframework.stereotype.Component;

@Component
public class EntityLookupService {
    private final StoreRepository storeRepo;
    private final ProductRepository productRepo;

    public EntityLookupService(StoreRepository storeRepo,
                               ProductRepository productRepo) {
        this.storeRepo   = storeRepo;
        this.productRepo = productRepo;
    }

    public Store resolveStore(String storeName) {
        return storeRepo.findByNameIgnoreCase(storeName)
                .orElseThrow(() -> new IllegalArgumentException("Store not found: " + storeName));
    }

    public Product resolveProduct(String productId) {
        return productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
    }
}