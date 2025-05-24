package com.marianf91.market.pricecomparator.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "product",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_product_identity",
                columnNames = {"name", "brand", "quantity", "unit"}
        )
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    private String category;
    private Double quantity;
    private String unit;
}