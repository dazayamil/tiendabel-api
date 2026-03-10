package com.dazayamil.tiendabel.model.entity;

import com.dazayamil.tiendabel.model.enums.Payment;
import com.dazayamil.tiendabel.model.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "sale")
@Data
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "create_at", nullable = false)
    private LocalDate createAt;

    @Column(name = "payment_method", nullable = false)
    private Payment paymentMethod;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private Status status;

    @OneToMany(mappedBy = "sale")
    private List<SaleItem> items;

}
