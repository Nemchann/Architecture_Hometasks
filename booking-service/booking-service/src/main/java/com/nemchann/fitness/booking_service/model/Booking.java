package com.nemchann.fitness.booking_service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shedule_id", nullable = false)
    private Long sheduleId;

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @Column(name = "status_id", nullable = false)
    private Long status; // confirmed, cancelled, completed

    @Column(name = "created_at")
    private LocalDateTime bookingTime = LocalDateTime.now();

    private String notes;

    //геттеры

    public Long getId() {
        return id;
    }

    public UUID getClientId() {
        return clientId;
    }

    public Long getSheduleId() {
        return sheduleId;
    }

    public Long getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    //сеттеры


    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setSheduleId(Long sheduleId) {
        this.sheduleId = sheduleId;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

}
