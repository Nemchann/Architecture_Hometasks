package com.nemchann.bookingservice.controller;

import com.nemchann.bookingservice.entity.Booking;
import com.nemchann.bookingservice.orchestrator.BookingOrchestrator;
import com.nemchann.bookingservice.orchestrator.BookingSagaManager;
import com.nemchann.bookingservice.repository.BookingRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Оркестрация бронирования", description = "API для запуска Saga-процесса бронирования")
public class BookingController {
    private final BookingRepository bookingRepository;
    private final BookingSagaManager sagaManager;

    public BookingController(BookingRepository bookingRepository, BookingSagaManager sagaManager) {
        this.bookingRepository = bookingRepository;
        this.sagaManager = sagaManager;
    }

    @PostMapping("/create")
    @Operation(summary = "Создать заявку на бронирование",
            description = "Сохраняет бронь со статусом NEW и запускает конечный автомат Оркестранта")
    public Booking createBooking(@RequestParam String clientName,
                                 @RequestParam String workoutName) {

        // 1. Создаем начальную сущность со статусом NEW
        Booking newBooking = new Booking(clientName, workoutName, "NEW");

        // 2. Сохраняем в MongoDB (база сама сгенерирует ID)
        Booking savedBooking = bookingRepository.save(newBooking);

        // TODO: 3. Создать Оркестранта, передать ему savedBooking.getId()
        // и запустить процесс processSuccess() для отправки сообщения в Payment Service
        return sagaManager.startBookingSaga(clientName, workoutName);
    }
}
