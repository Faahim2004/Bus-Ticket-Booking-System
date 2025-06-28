package com.example.BusBooking.Repository;

import com.example.BusBooking.Model.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Tickets,Integer> {

    @Query("SELECT COALESCE(SUM(t.seat_count), 0) FROM Tickets t WHERE t.user.id = :userId")
    int sumSeatCountByUserId(@Param("userId") int userId);

    @Query("SELECT COALESCE(SUM(t.seat_count), 0) FROM Tickets t WHERE t.user.id = :userId")
    int getTotalSeatsBookedByUser(@Param("userId") int userId);
}
