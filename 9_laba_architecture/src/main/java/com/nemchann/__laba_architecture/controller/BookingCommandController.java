package com.nemchann.__laba_architecture.controller;


import com.nemchann.__laba_architecture.model.Booking;
import com.nemchann.__laba_architecture.repository.BookingRepository;
import com.nemchann.__laba_architecture.service.BookingServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/commands/bookings")
@Tag(name = "Контроллер создания бронирования")
public class BookingCommandController {
    private final BookingServiceImpl service;

    public BookingCommandController(BookingRepository repository, BookingServiceImpl service){
        this.service = service;
    }

    @PostMapping("/create")
    @Operation(summary = "Создать бронирование")
    public void createBooking(@RequestParam String client,
                                 @RequestParam String schedule){

        service.createBooking(client, schedule);
    }

}
