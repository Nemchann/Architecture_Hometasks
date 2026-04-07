package com.nemchann.bookingservice.orchestrator.states;

import com.nemchann.bookingservice.orchestrator.BookingOrchestrator;
import com.nemchann.bookingservice.orchestrator.BookingState;

public class PendingPaymentState implements BookingState {
    @Override
    public void handleSuccess(BookingOrchestrator context) {
        System.out.println("Оплата прошла успешно! Отправляем запрос на уведомление...");

        // Отправляем сообщение в Notification Service
        String message = context.getClientName() + " успешно забронировал(а) " + context.getWorkoutName();
        context.getRabbitTemplate().convertAndSend("notification.request", message);

        // Переходим в финальное состояние
        context.setState(new CompletedState());
    }

    @Override
    public void handleFailure(BookingOrchestrator context) {
        System.out.println("Ошибка оплаты. Отменяем бронирование...");
        context.setState(new CancelledState());
    }

    @Override
    public String getStateName() { return "PENDING_PAYMENT"; }
}
