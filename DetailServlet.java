package Pjct;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DetailServlet")
public class DetailServlet extends HttpServlet {
	
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	int userid = Integer.parseInt(request.getParameter("userid"));
    	PrintWriter out = response.getWriter();
    	String message = "";
    	try {
    	
			Connection con = DBConnection.getconnection();
			String detailQuery = "SELECT * FROM tickets WHERE id=? ";
			PreparedStatement pst = con.prepareStatement(detailQuery);
			pst.setInt(1, userid);
			ResultSet rst = pst.executeQuery();
			 if (rst.next()) {
	                message = "Ticket ID: " + rst.getInt("id") + ", Total Number of Seats : " + rst.getInt("seat_number");
	            } else {
	                message = "No ticket found for this User ID.";
	            }
			 
	        } catch (NumberFormatException | SQLException | ClassNotFoundException e) {
	            message = "Error: " + e.getMessage();
	        }

	        request.setAttribute("message", message);
	        request.getRequestDispatcher("details.jsp").forward(request, response);
		}
}

