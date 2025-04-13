package Pjct;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/BookingServlet")
public class BookingServlet extends HttpServlet {
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        
        String busname1 = req.getParameter("bus1");
        String busname2 = req.getParameter("bus2");
        String username = req.getParameter("uname");
        int numofseat = Integer.parseInt(req.getParameter("seatno"));

        PrintWriter out = res.getWriter();

        if (numofseat > 5) {
            out.println("One user can book only 5 seats!!");
            return;
        }

        try {
            Connection con = DBConnection.getconnection();
            PreparedStatement pst;
            ResultSet rs;

            int busId = 0;
            String busName = "";

            if (busname1 != null) {
                busId = 1;
                busName = busname1;
            } else if (busname2 != null) {
                busId = 2;
                busName = busname2;
            }

            if (busId != 0) {
                // Get user ID from users table
                String userQuery = "SELECT id FROM users WHERE username = ?";
                PreparedStatement userPst = con.prepareStatement(userQuery);
                userPst.setString(1, username);
                ResultSet userRs = userPst.executeQuery();

                int userId = -1;
                if (userRs.next()) {
                    userId = userRs.getInt("id");
                } else {
                    out.println("Invalid username. Please log in.");
                    RequestDispatcher rd = req.getRequestDispatcher("login.jsp");
                    rd.include(req, res);
                    return;
                }

                // Check existing bookings by user in this bus
                String checkUserBookingQuery = "SELECT SUM(seat_number) AS totalBooked FROM tickets WHERE user_id = ? AND bus_id = ?";
                PreparedStatement checkPst = con.prepareStatement(checkUserBookingQuery);
                checkPst.setInt(1, userId);
                checkPst.setInt(2, busId);
                ResultSet checkRs = checkPst.executeQuery();

                int totalBookedSeats = 0;
                if (checkRs.next()) {
                    totalBookedSeats = checkRs.getInt("totalBooked");
                }

                // If user exceeds limit
                if (totalBookedSeats + numofseat > 5) {
                    out.println("Booking failed! You have already booked " + totalBookedSeats + 
                                " tickets. You can book only " + (5 - totalBookedSeats) + " more tickets in this bus.");
                    return;
                }

                // Check seat availability in buses table
                String checkQuery = "SELECT total_seats, booked_seats FROM buses WHERE id=?";
                pst = con.prepareStatement(checkQuery);
                pst.setInt(1, busId);
                rs = pst.executeQuery();

                if (rs.next()) {
                    int totalSeats = rs.getInt("total_seats");
                    int bookedSeats = rs.getInt("booked_seats");
                    int availableSeats = totalSeats - bookedSeats;

                    if (numofseat > availableSeats) {
                        out.println("Not enough seats available. Only " + availableSeats + " left.");
                    } else {
                        // Update booked seats in buses table
                        String updateQuery = "UPDATE buses SET booked_seats = booked_seats + ? WHERE id=?";
                        pst = con.prepareStatement(updateQuery);
                        pst.setInt(1, numofseat);
                        pst.setInt(2, busId);
                        pst.executeUpdate();

                        // Insert booking details into tickets table
                        String insertTicketQuery = "INSERT INTO tickets (user_id, bus_id, seat_number) VALUES (?, ?, ?)";
                        pst = con.prepareStatement(insertTicketQuery);
                        pst.setInt(1, userId);
                        pst.setInt(2, busId);
                        pst.setInt(3, numofseat);
                        pst.executeUpdate();

                        out.println("Booking Successful!");
                        RequestDispatcher rd = req.getRequestDispatcher("details.jsp");
                        rd.forward(req, res);
                    }
                } else {
                    out.println("Bus not found in the database.");
                }
            } else {
                out.println("No bus selected for booking.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
