package com.nusiss.ass.payment.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String transactionId;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum PaymentMethod {
        CASH, CARD_VISA, CARD_MASTER, BANK
    }

    public enum Status {
        PENDING, SUCCESS, FAILED
    }
}
