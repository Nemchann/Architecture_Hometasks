package com.nemchann.__laba_architecture.service;

import com.nemchann.__laba_architecture.model.BookingReadModel;

import java.util.List;

public interface BookingQueryService {
    BookingReadModel findById(Integer id);
    List<BookingReadModel> findAll();
}
