package com.example.BusBooking.Service;

import com.example.BusBooking.Model.Buses;
import com.example.BusBooking.Model.Tickets;
import com.example.BusBooking.Model.Users;
import com.example.BusBooking.Repository.BusRepository;
import com.example.BusBooking.Repository.TicketRepository;
import com.example.BusBooking.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepo;

    @Autowired
    private BusRepository busRepo;

    @Autowired
    private UserRepository userRepo;

    public String bookTicket(int bus_id, int seat_count, HttpSession session, Model model) {

        // Get logged-in user ID from session
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "Please login first.";
        }

        Optional<Users> userOpt = userRepo.findById(userId);
        if (userOpt.isEmpty()) {
            return "User not found.";
        }
        Users user = userOpt.get();

        Optional<Buses> busOpt = busRepo.findById(bus_id);
        if (busOpt.isEmpty()) {
            return "Bus not found.";
        }
        Buses bus = busOpt.get();

        int availableSeats = bus.getTotal_seat() - bus.getBooked_seat();
        if (seat_count > availableSeats) {
            return "Not enough seats available. Only " + availableSeats + " left.";
        }

        // Check total seats booked by user - limit to 5
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

        // Update booked seats count in bus
        bus.setBooked_seat(bus.getBooked_seat() + seat_count);
        busRepo.save(bus);

        return "Booking successful for Bus " + bus.getBus_name() + " with " + seat_count + " seats.";
    }
}
