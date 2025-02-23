package com.nusiss.ass.payment.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_payment")
@Data
public class Payment {

    @Id
    @Column(name = "payment_id", length = 255)
    private String paymentId;

    // Assuming Booking's primary key is a String
    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking bookingId;

    // Map to the integer column using the ordinal of the enum.
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "transaction_id", length = 255)
    private String transactionId;

    @Column(name = "amount", precision = 20, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10)
    private Status status = Status.PENDING;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate = LocalDateTime.now();

    public enum PaymentMethod {
        CASH,       // ordinal 0
        CARD_VISA,  // ordinal 1
        CARD_MASTER,// ordinal 2
        BANK_A       // ordinal 3
    }

    public enum Status {
        PENDING, SUCCESS, FAILED
    }
}
