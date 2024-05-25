package com.example.train_ticket_api.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PassengerTest {

    @Test
    public void testPassenger(){
        Passenger passenger=new Passenger("John", "Doe", "john.doe@example.com", "London", "France", 20.0, "A", 1);
        assertEquals("John",passenger.getFirstName(),"First Name should match");
        assertEquals("john.doe@example.com",passenger.getEmail(),"Email should match");
        assertEquals(passenger.getFromLocation(),"London","From Location Should Match");
    }
}
