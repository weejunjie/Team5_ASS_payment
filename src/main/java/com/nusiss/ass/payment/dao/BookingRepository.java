package com.nusiss.ass.payment.dao;

import com.nusiss.ass.payment.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, String> {
}

