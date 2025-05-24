package com.marianf91.market.pricecomparator.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "store",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_store_name",
                columnNames = "name"
        )
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}