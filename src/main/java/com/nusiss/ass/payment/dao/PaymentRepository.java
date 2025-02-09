package com.nusiss.ass.payment.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nusiss.ass.payment.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
