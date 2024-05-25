package com.example.train_ticket_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.train_ticket_api.model.Passenger;
import com.example.train_ticket_api.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Passenger saveUser(Passenger user) {
        return userRepository.save(user);
    }

    public Optional<Passenger> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<Passenger> getUsersBySection(String section) {
        return userRepository.findBySeatSection(section);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Passenger updateUserSeat(Long id, String newSeatSection, int newSeatNumber) {
        Passenger user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setSeatSection(newSeatSection);
        user.setSeatNumber(newSeatNumber);
        return userRepository.save(user);
    }
}

