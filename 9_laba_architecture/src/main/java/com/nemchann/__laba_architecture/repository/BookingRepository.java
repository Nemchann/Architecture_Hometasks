package com.nemchann.__laba_architecture.repository;

import com.nemchann.__laba_architecture.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
