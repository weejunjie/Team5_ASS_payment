package com.nusiss.ass.payment.dto;

import com.nusiss.ass.payment.model.Payment;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDto {
    private String paymentId;
    private String bookingId;
    private BigDecimal amount;
    private String transactionId;
    private Payment.PaymentMethod paymentMethod;
    private String status;
    private LocalDateTime createdDate;
}
