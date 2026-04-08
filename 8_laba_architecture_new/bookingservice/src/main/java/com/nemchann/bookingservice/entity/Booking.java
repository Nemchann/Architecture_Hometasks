package com.nemchann.bookingservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "bookings")
public class Booking {
    @Id
    private String id; // В MongoDB ID обычно строковый (генерируется сам, если не задать)

    private String clientName;
    private String workoutName;

    // Здесь мы будем хранить текущий статус: "NEW", "PENDING_PAYMENT", "COMPLETED", "CANCELLED"
    private String status;

    // Пустой конструктор для Spring
    public Booking() {}

    public Booking(String clientName, String workoutName, String status) {
        this.clientName = clientName;
        this.workoutName = workoutName;
        this.status = status;
    }
}
