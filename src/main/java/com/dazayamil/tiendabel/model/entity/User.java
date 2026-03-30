package com.dazayamil.tiendabel.model.entity;

import com.dazayamil.tiendabel.model.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(targetEntity = Sale.class, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Sale> sales;
}
