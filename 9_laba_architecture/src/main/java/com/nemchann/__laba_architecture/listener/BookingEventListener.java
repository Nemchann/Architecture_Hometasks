package com.nemchann.__laba_architecture.listener;

import com.nemchann.__laba_architecture.events.BookingCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BookingEventListener {

    @EventListener
    public void onBookingCreated(BookingCreatedEvent event) {
        System.out.println("📢 [CQRS Event] Получено событие создания брони!");
        System.out.println("ID: " + event.id());
        System.out.println("Клиент: " + event.clientName());
    }
}