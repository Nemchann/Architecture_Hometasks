package com.nemchann.fitness.booking_service.service;

import com.nemchann.fitness.booking_service.model.Workout;
import com.nemchann.fitness.booking_service.model.Booking;
import com.nemchann.fitness.booking_service.repository.WorkoutRepository;
import com.nemchann.fitness.booking_service.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class WorkoutService {
    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // Получить все тренировки
    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    // Получить тренировку по ID
    public Workout getSheduleById(Long id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workout not found with id: " + id));
    }

    // Получить доступные тренировки
    public List<Workout> getAvailableWorkouts() {
        return workoutRepository.findAvailableWorkouts();
    }

    // Создать тренировку
    public Workout createWorkout(Workout workout) {
        // Валидация
        if (workout.getStartTime().isAfter(workout.getEndTime())) {
            throw new RuntimeException("Start time must be before end time");
        }
        if (workout.getMaxParticipants() <= 0) {
            throw new RuntimeException("Max participants must be positive");
        }

        workout.setCurrentParticipants(0);
        workout.setCreatedAt(LocalDateTime.now());
        return workoutRepository.save(workout);
    }

    // Обновить тренировку
    public Workout updateWorkout(Long id, Workout workoutDetails) {
        Workout workout = getSheduleById(id);

        workout.setDescription(workoutDetails.getDescription());
        workout.setStartTime(workoutDetails.getStartTime());
        workout.setEndTime(workoutDetails.getEndTime());
        workout.setMaxParticipants(workoutDetails.getMaxParticipants());
        workout.setRoomId(workoutDetails.getRoomId());

        return workoutRepository.save(workout);
    }

    // Удалить тренировку
    @Transactional
    public void deleteWorkout(Long id) {
        // Проверяем, есть ли активные бронирования
        int activeBookings = bookingRepository.countBySheduleIdAndStatus(id, (long)1);
        if (activeBookings > 0) {
            throw new RuntimeException("Cannot delete workout with active bookings");
        }
        workoutRepository.deleteById(id);
    }

    // Записаться на тренировку
    @Transactional
    public Booking bookWorkout(Long sheduleId, UUID clientId, String notes) {
        Workout workout = getSheduleById(sheduleId);

        // Проверяем, есть ли места
        if (workout.getCurrentParticipants() >= workout.getMaxParticipants()) {
            throw new RuntimeException("No available spots for this workout");
        }

        // Проверяем, не записан ли уже клиент
        if (bookingRepository.existsBySheduleIdAndClientId(sheduleId, clientId)) {
            throw new RuntimeException("Client already booked this workout");
        }

        // Создаем бронирование
        Booking booking = new Booking();
        booking.setSheduleId(sheduleId);
        booking.setClientId(clientId);
        booking.setStatus((long)1);
        booking.setNotes(notes);
        booking.setBookingTime(LocalDateTime.now());

        // Увеличиваем счетчик участников
        workout.setCurrentParticipants(workout.getCurrentParticipants() + 1);
        workoutRepository.save(workout);

        return bookingRepository.save(booking);
    }

    // Отменить запись
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Уменьшаем счетчик участников
        Workout workout = getSheduleById(booking.getSheduleId());
        workout.setCurrentParticipants(workout.getCurrentParticipants() - 1);
        workoutRepository.save(workout);

        booking.setStatus((long)2);
        bookingRepository.save(booking);
    }
}
