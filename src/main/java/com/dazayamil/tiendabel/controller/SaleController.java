package com.dazayamil.tiendabel.controller;

import com.dazayamil.tiendabel.dto.request.SaleCreateRequestDTO;
import com.dazayamil.tiendabel.dto.request.SaleUpdateRequestDTO;
import com.dazayamil.tiendabel.dto.response.DailyReportResponseDTO;
import com.dazayamil.tiendabel.dto.response.SaleResponseDTO;
import com.dazayamil.tiendabel.service.SaleService;
import com.dazayamil.tiendabel.service.impl.SaleServiceImpl;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/sales")
public class SaleController {
    private final SaleServiceImpl saleService;

    public SaleController(SaleServiceImpl saleService){
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

    // GET /v1/sales/daily-report?date=2026-05-04
    @GetMapping("/daily-report")
    public ResponseEntity<DailyReportResponseDTO> getDailyReport(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate date){
        return ResponseEntity.ok(this.saleService.getDailyReport(date));
    }

    // POST /v1/sales -> 201 Create con Location header
    @PostMapping
    public ResponseEntity<SaleResponseDTO> createSale(@Valid @RequestBody SaleCreateRequestDTO request){
        SaleResponseDTO created = this.saleService.createSale(request);
        URI location = URI.create("/v1/sales/" + created.getSaleId());

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> updateSale(@PathVariable Long id, @Valid @RequestBody SaleUpdateRequestDTO request){
        return ResponseEntity.ok(this.saleService.updateSaleById(id, request));
    }


}
