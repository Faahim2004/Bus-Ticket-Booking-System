package com.example.BusBooking.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Tickets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketId;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "bus_id", referencedColumnName = "bus_id")
    private Buses bus;

    private int seat_count;
}
