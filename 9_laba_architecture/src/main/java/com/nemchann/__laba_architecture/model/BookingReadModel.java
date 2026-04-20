package com.nemchann.__laba_architecture.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingReadModel {
    private Integer id;

    private String client;

    private String schedule;

    private String status;

    public BookingReadModel(Integer id, String client, String schedule, String status){
        this.id = id;
        this.client = client;
        this.schedule = schedule;
        this.status = status;
    }
}
