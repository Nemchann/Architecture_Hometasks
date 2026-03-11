package com.nemchann.fitness.booking_service.repository;

import com.nemchann.fitness.booking_service.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long>{
    List<Workout> findByTrainerId(Long trainerId);

    List<Workout> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);


    @Query("SELECT s FROM Workout s WHERE s.currentParticipants < s.maxParticipants")
    List<Workout> findAvailableWorkouts();
}
