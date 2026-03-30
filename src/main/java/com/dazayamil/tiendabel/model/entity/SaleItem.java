package com.dazayamil.tiendabel.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "sale_item")
@Data
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String productSize;

    @Column(name = "priceAtMoment", nullable = false)
    private BigDecimal priceAtMoment;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(targetEntity = Sale.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

}
