package com.dazayamil.tiendabel.dto.request;

import com.dazayamil.tiendabel.model.enums.Payment;
import lombok.Data;

import java.util.List;

@Data
public class SaleCreateRequestDTO {
    private Long userId;
    private Payment paymentMethod;
    private List<SaleItemRequestDTO> items;
}
