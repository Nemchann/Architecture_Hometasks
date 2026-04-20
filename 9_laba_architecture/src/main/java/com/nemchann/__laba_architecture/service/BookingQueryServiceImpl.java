package com.nemchann.__laba_architecture.service;

import com.nemchann.__laba_architecture.model.Booking;
import com.nemchann.__laba_architecture.model.BookingReadModel;
import com.nemchann.__laba_architecture.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookingQueryServiceImpl implements BookingQueryService{

    private final BookingRepository repository;

    public BookingQueryServiceImpl(BookingRepository repository){
        this.repository = repository;
    }

    @Override
    public List<BookingReadModel> findAll() {
        List<Booking> entities = repository.findAll();

        // Превращаем список Entity в список ReadModel (DTO)
        return entities.stream()
                .map(this::mapToReadModel)
                .collect(Collectors.toList());
    }

    @Override
    public BookingReadModel findById(Integer id) {
        return repository.findById(id)
                .map(this::mapToReadModel)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));
    }

    private BookingReadModel mapToReadModel(Booking entity) {
        return new BookingReadModel(
                entity.getId(),
                "Клиент: " + entity.getClient(),
                "Тренировка: " + entity.getSchedule(),
                "СТАТУС: ПОДТВЕРЖДЕНО"
        );
    }
}
