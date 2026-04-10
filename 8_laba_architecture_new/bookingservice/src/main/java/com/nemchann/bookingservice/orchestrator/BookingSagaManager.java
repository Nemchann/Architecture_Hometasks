package com.nemchann.bookingservice.orchestrator;

import com.nemchann.bookingservice.dto.PaymentResponseMessage;
import com.nemchann.bookingservice.entity.Booking;
import com.nemchann.bookingservice.orchestrator.states.PendingPaymentState;
import com.nemchann.bookingservice.repository.BookingRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
public class BookingSagaManager {
    private final BookingRepository repository;
    private final RabbitTemplate rabbitTemplate;

    public BookingSagaManager(BookingRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    // 1. Начало процесса
    public Booking startBookingSaga(String clientName, String workoutName) {
        Booking booking = new Booking(clientName, workoutName, "NEW");
        booking = repository.save(booking);

        BookingOrchestrator orchestrator = createOrchestrator(booking);
        orchestrator.processSuccess(); // Запускает переход из NEW в PENDING_PAYMENT

        updateBookingStatus(booking, orchestrator);
        return booking;
    }

    // 2. Обработка ответа от Payment Service (слушаем очередь)
    @RabbitListener(queues = "payment.response")
    public void handlePaymentResponse(PaymentResponseMessage response) {
        Booking booking = repository.findById(response.getBookingId())
                .orElseThrow(() -> new RuntimeException("Бронь не найдена"));

        BookingOrchestrator orchestrator = createOrchestrator(booking);

        if (response.isSuccess()) {
            orchestrator.processSuccess();
        } else {
            orchestrator.processFailure();
        }

        updateBookingStatus(booking, orchestrator);
    }

    // Хелпер для создания оркестратора с текущим состоянием
    private BookingOrchestrator createOrchestrator(Booking booking) {
        BookingOrchestrator orchestrator = new BookingOrchestrator(
                booking.getId(), booking.getClientName(), booking.getWorkoutName(), rabbitTemplate);

        if ("PENDING_PAYMENT".equals(booking.getStatus())) {
            orchestrator.setState(new PendingPaymentState());
        }
        return orchestrator;
    }

    private void updateBookingStatus(Booking booking, BookingOrchestrator orchestrator) {
        booking.setStatus(orchestrator.getCurrentStateName());
        repository.save(booking);
    }
}
