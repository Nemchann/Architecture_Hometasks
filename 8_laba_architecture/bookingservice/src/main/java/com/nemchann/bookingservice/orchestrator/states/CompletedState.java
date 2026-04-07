package com.nemchann.bookingservice.orchestrator.states;

import com.nemchann.bookingservice.orchestrator.BookingOrchestrator;
import com.nemchann.bookingservice.orchestrator.BookingState;

public class CompletedState implements BookingState {
    @Override
    public void handleSuccess(BookingOrchestrator context) { /* Уже завершено */ }
    @Override
    public void handleFailure(BookingOrchestrator context) { /* Уже завершено */ }
    @Override
    public String getStateName() { return "COMPLETED"; }
}
