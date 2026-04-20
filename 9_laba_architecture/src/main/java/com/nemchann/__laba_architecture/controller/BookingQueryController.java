package com.nemchann.__laba_architecture.controller;

import com.nemchann.__laba_architecture.model.BookingReadModel;
import com.nemchann.__laba_architecture.repository.BookingRepository;
import com.nemchann.__laba_architecture.service.BookingQueryService;
import com.nemchann.__laba_architecture.service.BookingQueryServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/queries/bookings")
@Tag(name = "Контроллер для запросов к базе данных")
public class BookingQueryController {
    private final BookingQueryServiceImpl service;

    public BookingQueryController(BookingQueryServiceImpl service){
        this.service = service;
    }

    @GetMapping("/all")
    public List<BookingReadModel> getAll() {
        return service.findAll();
    }

    @GetMapping("/get_by_id")
    public BookingReadModel getById(@RequestParam Integer id){
        return service.findById(id);
    }

}
