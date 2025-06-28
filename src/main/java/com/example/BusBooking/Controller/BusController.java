package com.example.BusBooking.Controller;

import com.example.BusBooking.Service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BusController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/busBookTicket")
    public String bookTicket(@RequestParam int bus_id,
                             @RequestParam int seat_count,
                             HttpSession session,
                             Model model) {

        String message = ticketService.bookTicket(bus_id, seat_count, session, model);
        model.addAttribute("message", message);
        return "busBookingSuccess";  // your success JSP page
    }
}

