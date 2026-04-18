package com.nemchann.__laba_architecture.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bookings")
@Getter @Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "client_name")
    private String client;

    @Column(name = "schedule")
    private String schedule;

    public Booking(){

    }

    public Booking(String client, String schedule){
        this.client = client;
        this.schedule = schedule;
    }

}
