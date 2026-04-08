package com.nemchann.bookingservice.orchestrator.states;

import com.nemchann.bookingservice.orchestrator.BookingOrchestrator;
import com.nemchann.bookingservice.orchestrator.BookingState;

public class NewState implements BookingState {
    @Override
    public void handleSuccess(BookingOrchestrator context) {
        System.out.println("Отправляем запрос на оплату в RabbitMQ...");
        // Отправляем сообщение в очередь payment.request
        context.getRabbitTemplate().convertAndSend("payment.request", context.getBookingId());

        // Переключаем состояние автомата на Ожидание Оплаты
        context.setState(new PendingPaymentState());
    }

    @Override
    public void handleFailure(BookingOrchestrator context) {
        context.setState(new CancelledState());
    }

    @Override
    public String getStateName() { return "NEW"; }
}
