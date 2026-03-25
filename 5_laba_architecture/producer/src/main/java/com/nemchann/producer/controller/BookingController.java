package com.nemchann.producer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Бронирование", description = "API для бронирования тренировок")
public class BookingController {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queue.notification}")
    private String queueName;

    // Внедряем RabbitTemplate (инструмент Spring для отправки сообщений)
    public BookingController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/create")
    @Operation(summary = "Создать бронирование и отправить уведомление")
    public String createBooking(@RequestParam String clientName, @RequestParam String workoutName) {

        // 1. Формируем текст сообщения
        String message = String.format("Клиент %s успешно забронировал тренировку: %s", clientName, workoutName);

        // 2. Отправляем сообщение в очередь "notification.queue"
        rabbitTemplate.convertAndSend(queueName, message);

        return "Бронирование создано! Сообщение отправлено в RabbitMQ: " + message;
    }
}
