package com.nemchann.paymentservice.listener;

import com.nemchann.paymentservice.dto.PaymentResponseMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentListener {
    private final RabbitTemplate rabbitTemplate;
    private final Random random = new Random();

    public PaymentListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // Слушаем очередь запросов на оплату
    @RabbitListener(queues = "payment.request")
    public void processPayment(String bookingId) {
        System.out.println("💳 Получен запрос на оплату брони: " + bookingId);

        // Имитируем вероятность успеха 80% (20% на ошибку)
        boolean isSuccess = random.nextInt(100) >= 20;

        // Создаем объект ответа
        PaymentResponseMessage response = new PaymentResponseMessage();
        response.setBookingId(bookingId);
        response.setSuccess(isSuccess);
        response.setMessage(isSuccess ? "Оплата прошла успешно" : "Ошибка: недостаточно средств");

        // Имитируем небольшую задержку сервера
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        // ОТПРАВЛЯЕМ ОБРАТНО в очередь ответов
        System.out.println("📤 Отправляем результат оплаты: " + isSuccess);
        rabbitTemplate.convertAndSend("payment.response", response);
    }
}
