<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Book Ticket</title>
</head>
<body>
    <h2>Book Your Bus Ticket</h2>

    <!-- Assume userId is passed from controller -->
   <form method="post" action="/bookTicket">
       <input type="hidden" name="user_id" value="${userId}" />

       <label>Select Bus:</label><br>
       <input type="radio" name="bus_id" value="1" required> Express Bus - Thanjavur - Chennai<br>
       <input type="radio" name="bus_id" value="2" required> Fastline - Trichy - Madurai<br><br>

       <label>Number of Seats:</label>
       <input type="number" name="seat_count" min="1" max="5" required><br><br>

       <input type="submit" value="Book Ticket">
   </form>



</body>
</html>
