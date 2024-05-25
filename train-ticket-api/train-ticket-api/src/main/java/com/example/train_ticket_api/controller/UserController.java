package com.example.train_ticket_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.train_ticket_api.model.Passenger;
import com.example.train_ticket_api.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    private final Map<Integer, Boolean> sectionASeats = new HashMap<>();
    private final Map<Integer, Boolean> sectionBSeats = new HashMap<>();

    public UserController() {
        // Initialize seats for section A and section B
        initializeSeats(sectionASeats);
        initializeSeats(sectionBSeats);
    }

    private void initializeSeats(Map<Integer, Boolean> sectionSeats) {
        for (int i = 1; i <= 20; i++) {
            sectionSeats.put(i, false); // false indicates the seat is available
        }
    }

    // Purchase Ticket
    @PostMapping("/purchase")
    public ResponseEntity<Passenger> purchaseTicket(@RequestBody Passenger user) throws Exception {

        // Validate user information
        if (user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null) {
            throw new IllegalArgumentException("Please provide all user information (first name, last name, email)");
        }

        // Check seat availability
        String[] seatAllocation = allocateSeat();

        if (seatAllocation == null) {
            throw new Exception("No seats available. Please try again later.");
        }

        user.setFromLocation("London");
        user.setToLocation("France");
        user.setPricePaid(20.0);
        user.setSeatSection(seatAllocation[0]);
        user.setSeatNumber(Integer.parseInt(seatAllocation[1]));

        Passenger savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // Get User Receipt
    @GetMapping("/receipt/{id}")
    public ResponseEntity<Passenger> getUserReceipt(@PathVariable Long id) {
        Optional<Passenger> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get Users by Section
    @GetMapping("/section/{section}")
    public ResponseEntity<List<Passenger>> getUsersBySection(@PathVariable String section) {
        List<Passenger> users = userService.getUsersBySection(section);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Remove User
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/user/{id}/seat")
    public ResponseEntity<Passenger> modifyUserSeat(@PathVariable Long id, @RequestParam String section, @RequestParam int seatNumber) {
        // Get the user by ID
        Optional<Passenger> optionalUser = userService.getUserById(id);

        // Check if the user exists
        if (optionalUser.isPresent()) {
            Passenger user = optionalUser.get();

            // Check if the chosen seat is available in the specified section
            boolean isSeatAvailable;
            if ("A".equals(section)) {
                isSeatAvailable = !sectionASeats.getOrDefault(seatNumber, true);
            } else if ("B".equals(section)) {
                isSeatAvailable = !sectionBSeats.getOrDefault(seatNumber, true);
            } else {
                throw new IllegalArgumentException("Invalid section. Please choose either 'A' or 'B'.");
            }

            // If the chosen seat is available, modify the user's seat
            if (isSeatAvailable) {
                // Free the user's current seat
                freeSeat(user.getSeatSection(), user.getSeatNumber());

                // Occupy the chosen seat
                occupySeat(section, seatNumber);

                // Update the user's seat information
                user.setSeatSection(section);
                user.setSeatNumber(seatNumber);

                // Save the updated user
                Passenger updatedUser = userService.updateUserSeat(user.getId(), section, seatNumber);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                // The chosen seat is not available
                throw new IllegalArgumentException("The chosen seat is not available.");
            }
        } else {
            // User with the given ID not found
            throw new IllegalArgumentException("User with ID " + id + " not found.");
        }
    }


    // Free a seat
    private void freeSeat(String section, int seatNumber) {
        if ("A".equals(section)) {
            sectionASeats.put(seatNumber, false); // Mark seat as available (false)
        } else if ("B".equals(section)) {
            sectionBSeats.put(seatNumber, false); // Mark seat as available (false)
        }
    }

    // Occupy a seat
    private void occupySeat(String section, int seatNumber) {
        if ("A".equals(section)) {
            sectionASeats.put(seatNumber, true); // Mark seat as occupied (true)
        } else if ("B".equals(section)) {
            sectionBSeats.put(seatNumber, true); // Mark seat as occupied (true)
        }
    }


    // Allocate seat
    private String[] allocateSeat() {
        String[] result = new String[2];

        // Find the first available seat in section A
        Map.Entry<Integer, Boolean> availableSeat = sectionASeats.entrySet().stream()
                .filter(entry -> !entry.getValue()) // Filter out occupied seats
                .findFirst() // Find the first available seat
                .orElse(null); // If no available seat is found, return null

        if (availableSeat != null) {
            result[0] = "A";
            result[1] = String.valueOf(availableSeat.getKey());
            sectionASeats.put(availableSeat.getKey(), true); // Mark seat as occupied
            return result;
        }

        // If no available seat in section A, find the first available seat in section B
        availableSeat = sectionBSeats.entrySet().stream()
                .filter(entry -> !entry.getValue()) // Filter out occupied seats
                .findFirst() // Find the first available seat
                .orElse(null); // If no available seat is found, return null

        if (availableSeat != null) {
            result[0] = "B";
            result[1] = String.valueOf(availableSeat.getKey());
            sectionBSeats.put(availableSeat.getKey(), true); // Mark seat as occupied
            return result;
        }

        // No available seats in both sections
        return null;
    }
}
