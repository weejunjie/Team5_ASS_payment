package com.nusiss.ass.payment.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_booking")
@Data
public class Booking {

    @Id
    @Column(name = "booking_id", length = 255)
    private String bookingId;

    @Column(name = "amount ", precision = 20, scale = 2)
    private BigDecimal amount;

}
