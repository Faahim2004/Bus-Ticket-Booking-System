package com.example.BusBooking.Repository;

import com.example.BusBooking.Model.Buses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusRepository extends JpaRepository<Buses,Integer> {
}
