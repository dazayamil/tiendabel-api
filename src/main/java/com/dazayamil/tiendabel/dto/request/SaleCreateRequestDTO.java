package com.dazayamil.tiendabel.dto.request;

import com.dazayamil.tiendabel.model.enums.Payment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SaleCreateRequestDTO {
    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "paymentMethod is required")
    private Payment paymentMethod;

    @NotEmpty(message = "items must contain at least one item")
    @Valid
    private List<SaleItemRequestDTO> items;
}
