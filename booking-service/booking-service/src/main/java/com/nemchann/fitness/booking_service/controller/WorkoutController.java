package com.nemchann.fitness.booking_service.controller;

import com.nemchann.fitness.booking_service.model.Workout;
import com.nemchann.fitness.booking_service.model.Booking;
import com.nemchann.fitness.booking_service.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/workouts")
@Tag(name = "Тренировки", description = "Управление тренировками и бронированиями")

public class WorkoutController {
    @Autowired
    private WorkoutService workoutService;

    // GET все тренировки
    @GetMapping
    @Operation(summary = "Получить все тренировки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список тренировок получен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<Workout>> getAllWorkouts() {
        List<Workout> workouts = workoutService.getAllWorkouts();
        return ResponseEntity.ok(workouts);
    }

    // GET тренировка по ID
    @GetMapping("/{id}")
    @Operation(summary = "Получить тренировку по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Тренировка найдена"),
            @ApiResponse(responseCode = "404", description = "Тренировка не найдена")
    })
    public ResponseEntity<Workout> getSheduleById(@PathVariable Long id) {
        try {
            Workout workout = workoutService.getSheduleById(id);
            return ResponseEntity.ok(workout);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/test")
    public String test() {
        return "Сваггер работает!";
    }

    // GET доступные тренировки
    @GetMapping("/available")
    @Operation(summary = "Получить доступные тренировки (есть свободные места)")
    public ResponseEntity<List<Workout>> getAvailableWorkouts() {
        return ResponseEntity.ok(workoutService.getAvailableWorkouts());
    }

    // POST создать тренировку
    @PostMapping
    @Operation(summary = "Создать новую тренировку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Тренировка создана"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные"),
            @ApiResponse(responseCode = "409", description = "Конфликт в расписании")
    })
    public ResponseEntity<Workout> createWorkout(@Valid @RequestBody Workout workout) {
        try {
            Workout created = workoutService.createWorkout(workout);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT обновить тренировку
    @PutMapping("/{id}")
    @Operation(summary = "Обновить тренировку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Тренировка обновлена"),
            @ApiResponse(responseCode = "404", description = "Тренировка не найдена"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные")
    })
    public ResponseEntity<Workout> updateWorkout(
            @PathVariable Long id,
            @Valid @RequestBody Workout workout) {
        try {
            Workout updated = workoutService.updateWorkout(id, workout);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE удалить тренировку
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить тренировку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Тренировка удалена"),
            @ApiResponse(responseCode = "404", description = "Тренировка не найдена"),
            @ApiResponse(responseCode = "409", description = "Есть активные бронирования")
    })
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        try {
            workoutService.deleteWorkout(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // POST записаться на тренировку
    @PostMapping("/{id}/book")
    @Operation(summary = "Записаться на тренировку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Запись создана"),
            @ApiResponse(responseCode = "404", description = "Тренировка не найдена"),
            @ApiResponse(responseCode = "409", description = "Нет мест или уже записан")
    })
    public ResponseEntity<?> bookWorkout(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        try {
            String clientIdStr = request.get("clientId").toString();
            UUID clientId = UUID.fromString(clientIdStr);
            String notes = request.getOrDefault("notes", "").toString();

            Booking booking = workoutService.bookWorkout(id, clientId, notes);

            Map<String, Object> response = new HashMap<>();
            response.put("bookingId", booking.getId());
            response.put("status", booking.getStatus());
            response.put("message", "Вы успешно записаны на тренировку");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    // DELETE отменить запись
    @DeleteMapping("/bookings/{bookingId}")
    @Operation(summary = "Отменить запись")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Запись отменена"),
            @ApiResponse(responseCode = "404", description = "Запись не найдена"),
            @ApiResponse(responseCode = "409", description = "Нельзя отменить (меньше 2 часов)")
    })
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        try {
            workoutService.cancelBooking(bookingId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
