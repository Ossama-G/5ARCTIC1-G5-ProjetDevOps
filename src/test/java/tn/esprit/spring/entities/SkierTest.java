package tn.esprit.spring.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

class SkierTest {

    @Test
    void testSkierGettersAndSetters() {
        Skier skier = new Skier();

        // Test numSkier
        skier.setNumSkier(1L);
        assertEquals(1L, skier.getNumSkier());

        // Test firstName
        skier.setFirstName("John");
        assertEquals("John", skier.getFirstName());

        // Test lastName
        skier.setLastName("Doe");
        assertEquals("Doe", skier.getLastName());

        // Test dateOfBirth
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        skier.setDateOfBirth(dateOfBirth);
        assertEquals(dateOfBirth, skier.getDateOfBirth());

        // Test city
        skier.setCity("New York");
        assertEquals("New York", skier.getCity());

        // Test subscription
        Subscription subscription = new Subscription();
        skier.setSubscription(subscription);
        assertEquals(subscription, skier.getSubscription());

        // Test pistes
        Set<Piste> pistes = new HashSet<>();
        skier.setPistes(pistes);
        assertEquals(pistes, skier.getPistes());

        // Test registrations
        Set<Registration> registrations = new HashSet<>();
        skier.setRegistrations(registrations);
        assertEquals(registrations, skier.getRegistrations());
    }
}