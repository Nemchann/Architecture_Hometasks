package com.nemchann.bookingservice.orchestrator;

public interface BookingState {
    // Метод для успешного развития событий
    void handleSuccess(BookingOrchestrator context);

    // Метод для обработки ошибок (например, отказ в оплате)
    void handleFailure(BookingOrchestrator context);

    // Получить текстовое имя состояния (для сохранения в MongoDB)
    String getStateName();
}
