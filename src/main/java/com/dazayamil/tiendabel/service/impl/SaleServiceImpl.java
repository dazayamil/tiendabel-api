package com.dazayamil.tiendabel.service.impl;

import com.dazayamil.tiendabel.dto.request.SaleCreateRequestDTO;
import com.dazayamil.tiendabel.dto.request.SaleItemRequestDTO;
import com.dazayamil.tiendabel.dto.request.SaleUpdateRequestDTO;
import com.dazayamil.tiendabel.dto.response.DailyReportResponseDTO;
import com.dazayamil.tiendabel.dto.response.PaymentBreakdownDTO;
import com.dazayamil.tiendabel.dto.response.SaleResponseDTO;
import com.dazayamil.tiendabel.mapper.SaleMapper;
import com.dazayamil.tiendabel.model.entity.Product;
import com.dazayamil.tiendabel.model.entity.Sale;
import com.dazayamil.tiendabel.model.entity.SaleItem;
import com.dazayamil.tiendabel.model.entity.User;
import com.dazayamil.tiendabel.model.enums.Payment;
import com.dazayamil.tiendabel.model.enums.Status;
import com.dazayamil.tiendabel.repository.ProductRepository;
import com.dazayamil.tiendabel.repository.SaleRepository;
import com.dazayamil.tiendabel.repository.UserRepository;
import com.dazayamil.tiendabel.service.SaleService;
import org.springframework.stereotype.Service;

import javax.security.sasl.SaslException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SaleMapper saleMapper;

    public SaleServiceImpl(SaleRepository saleRepository, UserRepository userRepository, ProductRepository productRepository, SaleMapper saleMapper){
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.saleMapper = saleMapper;
    }

    private List<SaleItem> buildSaleItems(Sale sale, List<SaleItemRequestDTO> itemsDTO){
        List<SaleItem> items = new ArrayList<>();

        for (SaleItemRequestDTO itemDTO : itemsDTO){
            Product product = productRepository.findById(itemDTO.getProductoId())
                    .orElseThrow( () -> new RuntimeException("Product not found"));

            BigDecimal finalPrice = itemDTO.getPriceAtMoment();
            if(finalPrice == null){
                finalPrice = product.getPrice();
            }

            SaleItem item = new SaleItem();
            item.setSale(sale);
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setProductSize(itemDTO.getProductSize());
            item.setPriceAtMoment(finalPrice);
            items.add(item);
        }
        return items;
    }

    private BigDecimal calculateTotal(List<SaleItem> items){
        return items.stream()
                .map(item -> item.getPriceAtMoment().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @Override
    public SaleResponseDTO createSale(SaleCreateRequestDTO request) {
        if(request.getItems() == null || request.getItems().isEmpty()){
            throw new RuntimeException("A Sale must hace at least one item");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow( () -> new RuntimeException("user not found"));

        Sale sale = new Sale();
        sale.setUser(user);
        sale.setCreatedAt(LocalDateTime.now());
        sale.setPaymentMethod(request.getPaymentMethod());
        sale.setStatus(Status.COMPLETED);

        List<SaleItem> items = buildSaleItems(sale, request.getItems());
        BigDecimal total = calculateTotal(items);

        sale.setItems(items);
        sale.setTotalAmount(total);
        Sale savedSale = saleRepository.save(sale);
        return this.saleMapper.toResponseDTO(savedSale);
    }

    @Override
    public List<SaleResponseDTO> getAllSales() {
        List<Sale> sales = this.saleRepository.findAll();
        return this.saleMapper.toResponseDTOList(sales);
    }

    @Override
    public SaleResponseDTO getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        return this.saleMapper.toResponseDTO(sale);
    }

    @Override
    public SaleResponseDTO updateSaleById(Long id, SaleUpdateRequestDTO request) {
        Sale updateSale = this.saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        if(updateSale.getStatus() != Status.COMPLETED){
            throw new RuntimeException("Sale cannot be updated unless it is COMPLETED");
        }

        updateSale.setPaymentMethod(request.getPaymentMethod());
        updateSale.getItems().clear();

        List<SaleItem> items = buildSaleItems(updateSale, request.getItems());
        BigDecimal total = calculateTotal(items);
        updateSale.setItems(items);
        updateSale.setTotalAmount(total);
        Sale savedSale = saleRepository.save(updateSale);
        return this.saleMapper.toResponseDTO(savedSale);
    }

    private List<PaymentBreakdownDTO> buildPaymentBreakdown(List<Sale> sales) {

        Map<Payment, List<Sale>> salesByPaymentMethod = sales.stream()
                .collect(Collectors.groupingBy(sale -> sale.getPaymentMethod()));

        return salesByPaymentMethod.entrySet().stream()
                .map(entry -> mapToPaymentBreakdown(entry))
                .collect(Collectors.toList());
    }

    private PaymentBreakdownDTO mapToPaymentBreakdown(Map.Entry<Payment, List<Sale>> entry) {

        Payment paymentMethod = entry.getKey();
        List<Sale> salesGroup = entry.getValue();

        BigDecimal totalAmount = salesGroup.stream()
                .map(Sale::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return PaymentBreakdownDTO.builder()
                .paymentMethod(paymentMethod)
                .count(salesGroup.size())
                .total(totalAmount)
                .build();
    }

    @Override
    public DailyReportResponseDTO getDailyReport(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Sale> sales = this.saleRepository.findByCreatedAtBetween(start, end);

        BigDecimal totalRevenue = sales.stream()
                .map(sale -> sale.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        List<PaymentBreakdownDTO> breakdown = buildPaymentBreakdown(sales);

        return DailyReportResponseDTO.builder()
                .date(date)
                .totalSales(sales.size())
                .totalRevenue(totalRevenue)
                .paymentBreakdown(breakdown)
                .build();

    }
}
