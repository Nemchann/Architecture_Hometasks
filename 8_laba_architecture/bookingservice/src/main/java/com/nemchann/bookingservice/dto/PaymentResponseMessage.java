package com.nemchann.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseMessage {
    private String bookingId; // ID бронирования, чтобы понять, для кого пришел ответ
    private boolean success;  // Прошла оплата или нет
    private String message;
}
