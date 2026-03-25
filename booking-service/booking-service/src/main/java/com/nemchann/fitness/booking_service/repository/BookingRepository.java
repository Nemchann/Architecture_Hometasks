package com.nemchann.fitness.booking_service.repository;

import com.nemchann.fitness.booking_service.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>{
    List<Booking> findByClientId(UUID clientId);

    List<Booking> findBySheduleId(Long sheduleId);

    boolean existsBySheduleIdAndClientId(Long sheduleId, UUID clientId);

    int countBySheduleIdAndStatus(Long sheduleId, Long status);
}
