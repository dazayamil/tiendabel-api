package com.dazayamil.tiendabel.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class DailyReportResponseDTO {
    private LocalDate date;
    private int totalSales;
    private BigDecimal totalRevenue;
    private List<PaymentBreakdownDTO> paymentBreakdown;
}
