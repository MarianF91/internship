package com.marianf91.market.pricecomparator.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "price_snapshot",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_price_snapshot",
                columnNames = {"product_id", "store_id", "date"}
        )
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Store store;

    private LocalDate date;
    private BigDecimal price;
    private String currency;
}