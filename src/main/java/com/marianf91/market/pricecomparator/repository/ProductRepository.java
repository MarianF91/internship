package com.marianf91.market.pricecomparator.repository;

import com.marianf91.market.pricecomparator.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByNameAndBrandAndQuantityAndUnit(
            String name, String brand, Double quantity, String unit
    );

}