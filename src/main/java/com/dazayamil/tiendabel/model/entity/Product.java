package com.dazayamil.tiendabel.model.entity;

import com.dazayamil.tiendabel.model.enums.Category;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(name = "current_price", nullable = false)
    private BigDecimal currentPrice;
}
