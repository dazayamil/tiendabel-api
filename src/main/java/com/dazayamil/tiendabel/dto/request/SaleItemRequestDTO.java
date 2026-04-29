package com.dazayamil.tiendabel.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleItemRequestDTO {

    @NotNull(message = "productId is required")
    private Long productoId;

    @Min(value = 1, message = "quantity must be at least 1")
    private int quantity;

    @NotBlank(message = "productSize is required")
    private String productSize;

    @NotNull(message = "priceAtMoment is required")
    @DecimalMin(value = "0.01", message = "priceAtMoment must be greater than 0")
    private BigDecimal priceAtMoment;
}
