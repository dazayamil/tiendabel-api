package com.dazayamil.tiendabel.service;

import com.dazayamil.tiendabel.dto.request.SaleCreateRequestDTO;
import com.dazayamil.tiendabel.dto.request.SaleUpdateRequestDTO;
import com.dazayamil.tiendabel.dto.response.SaleResponseDTO;

import java.util.List;

public interface SaleService {
    SaleResponseDTO createSale(SaleCreateRequestDTO request);
    SaleResponseDTO getSaleById(Long id);
    List<SaleResponseDTO> getAllSales();
    SaleResponseDTO updateSaleById(Long id, SaleUpdateRequestDTO request);
}
