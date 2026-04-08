package com.nemchann.bookingservice.orchestrator.states;

import com.nemchann.bookingservice.orchestrator.BookingOrchestrator;
import com.nemchann.bookingservice.orchestrator.BookingState;

public class  PendingPaymentState implements BookingState {
    @Override
    public void handleSuccess(BookingOrchestrator context) {
        System.out.println("✅ Оплата подтверждена для брони: " + context.getBookingId());

        // Формируем уведомление
        String msg = "Уважаемая " + context.getClientName() + ", ждем вас на " + context.getWorkoutName() + "!";
        context.getRabbitTemplate().convertAndSend("notification.request", msg);

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
