package com.dazayamil.tiendabel.dto.response;

import com.dazayamil.tiendabel.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SaleResponseDTO {
    private Long saleId;
    private LocalDateTime createdAt;
    private BigDecimal totalAmount;
    private Status status;
}
