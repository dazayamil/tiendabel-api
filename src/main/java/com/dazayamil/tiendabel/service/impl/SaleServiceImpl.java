package com.dazayamil.tiendabel.service.impl;

import com.dazayamil.tiendabel.dto.request.SaleCreateRequestDTO;
import com.dazayamil.tiendabel.dto.request.SaleItemRequestDTO;
import com.dazayamil.tiendabel.dto.response.SaleResponseDTO;
import com.dazayamil.tiendabel.model.entity.Product;
import com.dazayamil.tiendabel.model.entity.Sale;
import com.dazayamil.tiendabel.model.entity.SaleItem;
import com.dazayamil.tiendabel.model.entity.User;
import com.dazayamil.tiendabel.model.enums.Status;
import com.dazayamil.tiendabel.repository.ProductRepository;
import com.dazayamil.tiendabel.repository.SaleRepository;
import com.dazayamil.tiendabel.repository.UserRepository;
import com.dazayamil.tiendabel.service.SaleService;
import org.springframework.stereotype.Service;

import javax.security.sasl.SaslException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public SaleServiceImpl(SaleRepository saleRepository, UserRepository userRepository, ProductRepository productRepository){
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
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

        List<SaleItem> items = new ArrayList<>();
        BigDecimal total =  BigDecimal.ZERO;

        for (SaleItemRequestDTO itemDTO : request.getItems()){
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

            BigDecimal subtotal = finalPrice.multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            total = total.add(subtotal);
            items.add(item);
        }

        sale.setItems(items);
        sale.setTotalAmount(total);
        Sale savedSale = saleRepository.save(sale);
        return new SaleResponseDTO(
                savedSale.getId(),
                savedSale.getCreatedAt(),
                savedSale.getTotalAmount(),
                savedSale.getStatus()
        );
    }

    @Override
    public SaleResponseDTO getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        return new SaleResponseDTO(
                sale.getId(),
                sale.getCreatedAt(),
                sale.getTotalAmount(),
                sale.getStatus()
        );
    }
}
