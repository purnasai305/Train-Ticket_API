package com.example.train_ticket_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.train_ticket_api.model.Passenger;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Passenger, Long> {
    List<Passenger> findBySeatSection(String seatSection);
}
