package com.dazayamil.tiendabel.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleItemRequestDTO {
    private Long productoId;
    private int quantity;
    private String productSize;
    private BigDecimal priceAtMoment;
}
