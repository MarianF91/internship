package com.marianf91.market.pricecomparator.repository;

import com.marianf91.market.pricecomparator.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByNameIgnoreCase(String name);
}