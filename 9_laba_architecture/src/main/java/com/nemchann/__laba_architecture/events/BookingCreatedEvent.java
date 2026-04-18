package com.nemchann.__laba_architecture.events;


public record BookingCreatedEvent(
        Integer id,
        String clientName
) {}