package com.nemchann.bookingservice.repository;

import com.nemchann.bookingservice.entity.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface BookingRepository extends MongoRepository<Booking, String> {
}
