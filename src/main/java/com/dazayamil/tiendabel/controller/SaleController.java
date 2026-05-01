package com.dazayamil.tiendabel.controller;

import com.dazayamil.tiendabel.dto.request.SaleCreateRequestDTO;
import com.dazayamil.tiendabel.dto.response.SaleResponseDTO;
import com.dazayamil.tiendabel.service.SaleService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/sales")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService){
        this.saleService = saleService;
    }

    @GetMapping()
    public ResponseEntity<List<SaleResponseDTO>> getAllSales(){
        return ResponseEntity.ok(this.saleService.getAllSales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> getSaleById(@PathVariable Long id){
        return ResponseEntity.ok(this.saleService.getSaleById(id));
    }

    // POST /v1/sales -> 201 Create con Location header
    @PostMapping
    public ResponseEntity<SaleResponseDTO> createSale(@Valid @RequestBody SaleCreateRequestDTO request){
        SaleResponseDTO created = this.saleService.createSale(request);
        URI location = URI.create("/v1/sales/" + created.getSaleId());

        return ResponseEntity.created(location).body(created);
    }
}
