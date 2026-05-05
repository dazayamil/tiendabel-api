package com.dazayamil.tiendabel.dto.response;

import com.dazayamil.tiendabel.model.enums.Payment;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentBreakdownDTO {
    private Payment paymentMethod;
    private int count;
    private BigDecimal total;
}
