package com.dazayamil.tiendabel.service;

import com.dazayamil.tiendabel.dto.request.SaleCreateRequestDTO;
import com.dazayamil.tiendabel.dto.response.SaleResponseDTO;

public interface SaleService {
    SaleResponseDTO createSale(SaleCreateRequestDTO request);
}
