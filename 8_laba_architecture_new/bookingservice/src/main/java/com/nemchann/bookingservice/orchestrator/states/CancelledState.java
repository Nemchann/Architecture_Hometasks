package com.nemchann.bookingservice.orchestrator.states;

import com.nemchann.bookingservice.orchestrator.BookingOrchestrator;
import com.nemchann.bookingservice.orchestrator.BookingState;

public class CancelledState implements BookingState {
    @Override
    public void handleSuccess(BookingOrchestrator context) { /* Отменено */ }
    @Override
    public void handleFailure(BookingOrchestrator context) { /* Отменено */ }
    @Override
    public String getStateName() { return "CANCELLED"; }
}
