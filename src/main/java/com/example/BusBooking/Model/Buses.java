package com.example.BusBooking.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Buses {

    @Id
    private int bus_id;
    private String bus_name;
    private String source;
    private String destination;
    private int total_seat;
    private int booked_seat;
}
