package com.example.BusBooking.Controller;

import com.example.BusBooking.Model.Buses;
import com.example.BusBooking.Model.Tickets;
import com.example.BusBooking.Model.Users;
import com.example.BusBooking.Repository.BusRepository;
import com.example.BusBooking.Repository.TicketRepository;
import com.example.BusBooking.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BusRepository busRepo;

    @Autowired
    private TicketRepository ticketRepo;

    // Show login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Handle login
    @PostMapping("/login")
    public String login(@RequestParam String email, HttpSession session, Model model) {
        Optional<Users> userOpt = userRepo.findByEmail(email);
        if (userOpt.isPresent()) {
            Users user = userOpt.get();
            session.setAttribute("id", user.getId());
            session.setAttribute("user_name", user.getUser_name());

            List<Buses> buses = busRepo.findAll();
            model.addAttribute("buses", buses);
            return "book";  // book.jsp
        }

        model.addAttribute("error", "Invalid email ID");
        return "login";
    }

    // Handle booking
    @PostMapping("/bookTicket")
    public String bookTicket(
            @RequestParam int bus_id,
            @RequestParam int seat_count,
            HttpSession session,
            Model model) {

        Integer userId = (Integer) session.getAttribute("id");
        if (userId == null) {
            return "redirect:/login";
        }

        Optional<Users> userOpt = userRepo.findById(userId);
        Optional<Buses> busOpt = busRepo.findById(bus_id);

        if (userOpt.isEmpty() || busOpt.isEmpty()) {
            model.addAttribute("message", "Invalid bus or user.");
            model.addAttribute("buses", busRepo.findAll());
            return "book";
        }

        int totalBooked = ticketRepo.getTotalSeatsBookedByUser(userId);
        totalBooked = totalBooked < 0 ? 0 : totalBooked; // handle null or negative

        if (totalBooked + seat_count > 5) {
            model.addAttribute("message", "Cannot book more than 5 seats per user.");
            model.addAttribute("buses", busRepo.findAll());
            return "book";
        }

        Buses bus = busOpt.get();
        int availableSeats = bus.getTotal_seat() - bus.getBooked_seat();
        if (availableSeats < seat_count) {
            model.addAttribute("message", "Not enough available seats.");
            model.addAttribute("buses", busRepo.findAll());
            return "book";
        }

        // Update bus booked seats
        bus.setBooked_seat(bus.getBooked_seat() + seat_count);
        busRepo.save(bus);

        // Save ticket
        Tickets ticket = new Tickets();
        ticket.setUser(userOpt.get());
        ticket.setBus(bus);
        ticket.setSeat_count(seat_count);
        ticketRepo.save(ticket);

        model.addAttribute("message", "Ticket booked successfully!");

        model.addAttribute("buses", busRepo.findAll());
        return "bookingSuccess";


    }
}
