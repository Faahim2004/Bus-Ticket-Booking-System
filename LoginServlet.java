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

@WebServlet("/login")
public class LoginServlet extends HttpServlet{

	
	     public void doPost(HttpServletRequest req , HttpServletResponse res) throws IOException, ServletException {
	    	 
	    	 
	    	     String email=   req.getParameter("email");
	    	     String password =req.getParameter("pname");
	    	     
	    	    PrintWriter out=     res.getWriter();
	    	                        res.setContentType("text/html");
	    	   
	    	    	   int flag=0;
	    	    	   try {
	    	    		    String query="select * from users where email=? and password =?";
	    	    		   Connection con=   DBConnection.getconnection();
	    	    		   PreparedStatement pst=   con.prepareStatement(query);
	    	    		   
	    	    		            pst.setString(1, email);
	    	    		            pst.setString(2, password);
	    	    		         ResultSet rst=   pst.executeQuery();
	    	    		         
	    	    		         while(rst.next()) {
	    	    		        	 
	    	    		        	if(email.equals(rst.getString(3)) && password.equals(rst.getString(4))) {
	    	    		        		
	    	    		        		flag=1;
	    	    		        		
	    	    		        		 RequestDispatcher rd =  req.getRequestDispatcher("booking.jsp");
	    	    		        		                   rd.forward(req, res);    
	    	    		        	} 
	    	    		         }
	    	    		         
	    	    		         if(flag==0) {
	    	    		        	 
	    	    		        	 RequestDispatcher rd=    req.getRequestDispatcher("login.jsp");
	    	    		        	                   rd.include(req, res);
	    	    		        	                   out.print("Invalid user name or Password");
	    	    		         }         
	    	    	   }
	    	    	   catch(SQLException e) {e.printStackTrace();}
	    	    	   catch(ClassNotFoundException e) {e.printStackTrace();}

	    	       }       
	     }     
	    