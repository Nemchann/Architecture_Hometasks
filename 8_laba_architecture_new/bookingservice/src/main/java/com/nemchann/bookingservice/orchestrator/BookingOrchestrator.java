package com.nemchann.bookingservice.orchestrator;

import com.nemchann.bookingservice.orchestrator.states.NewState;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class BookingOrchestrator {
    private BookingState state;
    private String bookingId;
    private String clientName;
    private String workoutName;

    // Ссылка на RabbitMQ для отправки сообщений
    private final RabbitTemplate rabbitTemplate;

    public BookingOrchestrator(String bookingId, String clientName, String workoutName, RabbitTemplate rabbitTemplate) {
        this.bookingId = bookingId;
        this.clientName = clientName;
        this.workoutName = workoutName;
        this.rabbitTemplate = rabbitTemplate;

        // Начальное состояние всегда NEW
        this.state = new NewState();
    }

    // Сеттер для смены состояния (используется внутри самих состояний)
    public void setState(BookingState state) {
        System.out.println("🔄 Переход автомата: " + this.state.getStateName() + " -> " + state.getStateName());
        this.state = state;
    }

    // Делегируем вызовы текущему состоянию
    public void processSuccess() {
        this.state.handleSuccess(this);
    }

    public void processFailure() {
        this.state.handleFailure(this);
    }

    // Геттеры
    public RabbitTemplate getRabbitTemplate() { return rabbitTemplate; }
    public String getBookingId() { return bookingId; }
    public String getClientName() { return clientName; }
    public String getWorkoutName() { return workoutName; }
    public String getCurrentStateName() { return state.getStateName(); }
}
