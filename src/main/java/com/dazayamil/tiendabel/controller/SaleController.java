package com.dazayamil.tiendabel.controller;

import com.dazayamil.tiendabel.dto.request.SaleCreateRequestDTO;
import com.dazayamil.tiendabel.dto.response.SaleResponseDTO;
import com.dazayamil.tiendabel.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/v1/sales")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService){
        this.saleService = saleService;
    }

    // POST /v1/sales -> 201 Create con Location header
    @PostMapping
    public ResponseEntity<SaleResponseDTO> createSale(@Valid @RequestBody SaleCreateRequestDTO request){
        SaleResponseDTO created = saleService.createSale(request);
        URI location = URI.create("/v1/sales/" + created.getSaleId());

        return ResponseEntity.created(location).body(created);
    }
}
