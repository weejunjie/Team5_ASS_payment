package com.nusiss.ass.payment.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nusiss.ass.payment.model.Payment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    @Query("SELECT p FROM Payment p WHERE p.bookingId.bookingId = :bookingId")
    Optional<Payment> findByBookingId(@Param("bookingId") String bookingId);
}
