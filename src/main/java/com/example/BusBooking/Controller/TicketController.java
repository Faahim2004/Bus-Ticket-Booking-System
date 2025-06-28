package com.example.BusBooking.Controller;

import com.example.BusBooking.Model.Buses;
import com.example.BusBooking.Model.Tickets;
import com.example.BusBooking.Model.Users;
import com.example.BusBooking.Repository.BusRepository;
import com.example.BusBooking.Repository.TicketRepository;
import com.example.BusBooking.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class TicketController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TicketRepository ticketRepo;

    @Autowired
    private BusRepository busRepo;

    @PostMapping("/book")
    public String bookTickets(@RequestParam int userId,
                              @RequestParam int busId,
                              @RequestParam int seat_count,
                              Model model) {

        try {
            Optional<Users> userOpt = userRepo.findById(userId);
            if (userOpt.isEmpty()) {
                model.addAttribute("error", "User not found.");
                return "book";  // Your JSP page
            }
            Users user = userOpt.get();

            Optional<Buses> busOpt = busRepo.findById(busId);
            if (busOpt.isEmpty()) {
                model.addAttribute("error", "Bus not found.");
                return "book";
            }
            Buses bus = busOpt.get();

            int availableSeats = bus.getTotal_seat() - bus.getBooked_seat();

            if (seat_count > availableSeats) {
                model.addAttribute("error", "Booking failed: Not enough seats available.");
                return "book";
            }

            // Total seats booked by user
            int totalBookedSeats = ticketRepo.sumSeatCountByUserId(userId);
            if (totalBookedSeats + seat_count > 5) {
                model.addAttribute("error", "Booking failed: You can only book a total of 5 seats.");
                return "book";
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

            model.addAttribute("message", "Ticket booked successfully!");
        }
        catch (Exception e){
            model.addAttribute("message", "Booking failed: " + e.getMessage());
        }
        model.addAttribute("message", "Booking successful!");
        return "book";
    }
}
