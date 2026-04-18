package com.nemchann.__laba_architecture.service;

import com.nemchann.__laba_architecture.events.BookingCreatedEvent;
import com.nemchann.__laba_architecture.model.Booking;
import com.nemchann.__laba_architecture.repository.BookingRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingCommandService {
    private final ApplicationEventPublisher publisher;
    private final BookingRepository repository;

    public BookingServiceImpl(ApplicationEventPublisher publisher, BookingRepository repository){
        this.publisher = publisher;
        this.repository = repository;
    }

    public void createBooking(String client, String schedule) {
        Booking entity = repository.save(new Booking(client, schedule));
        // Публикуем событие
        publisher.publishEvent(new BookingCreatedEvent(entity.getId(), client));
    }
}