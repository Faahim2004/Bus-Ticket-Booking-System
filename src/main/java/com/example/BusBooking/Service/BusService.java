package com.example.BusBooking.Service;

import com.example.BusBooking.Model.Buses;
import com.example.BusBooking.Model.Tickets;
import com.example.BusBooking.Model.Users;
import com.example.BusBooking.Repository.BusRepository;
import com.example.BusBooking.Repository.TicketRepository;
import com.example.BusBooking.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusService {

    @Autowired
    private BusRepository busRepo;

    @Autowired
    private TicketRepository ticketRepo;

    @Autowired
    private UserRepository userRepo;

    public Iterable<Buses> busesList() {
        return busRepo.findAll();
    }

    public String updateBus(int id, Buses buses) {
        Optional<Buses> busOpt = busRepo.findById(id);
        if (busOpt.isPresent()) {
            Buses existBus = busOpt.get();
            existBus.setBus_name(buses.getBus_name());
            existBus.setBooked_seat(buses.getBooked_seat());
            existBus.setTotal_seat(buses.getTotal_seat());
            busRepo.save(existBus);
            return "Updated Successfully";
        } else {
            return "ID not Found";
        }
    }

    // Booking method with userId passed as parameter
    public String bookTicket(int userId, int busId, int seat_count) {

        Optional<Users> userOpt = userRepo.findById(userId);
        if (userOpt.isEmpty()) {
            return "User not found!";
        }
        Users user = userOpt.get();

        Optional<Buses> busOpt = busRepo.findById(busId);
        if (busOpt.isEmpty()) {
            return "Bus not found!";
        }
        Buses bus = busOpt.get();

        int availableSeats = bus.getTotal_seat() - bus.getBooked_seat();
        if (seat_count > availableSeats) {
            return "Not enough seats available. Only " + availableSeats + " left.";
        }

        int totalBookedSeats = ticketRepo.sumSeatCountByUserId(userId);
        if (totalBookedSeats + seat_count > 5) {
            return "Booking failed: You can only book a total of 5 seats.";
        }

        // Save ticket
        Tickets ticket = new Tickets();
        ticket.setUser(user);
        ticket.setBus(bus);
        ticket.setSeat_count(seat_count);
        ticketRepo.save(ticket);

        // Update bus booked seats
        bus.setBooked_seat(bus.getBooked_seat() + seat_count);
        busRepo.save(bus);

        return "Booking successful for Bus " + bus.getBus_name() + " with " + seat_count + " seats.";
    }
}
