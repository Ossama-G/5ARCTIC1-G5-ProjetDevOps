package tn.esprit.spring.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Skier;

class RegistrationTest {

    @Test
    void testRegistrationGettersAndSetters() {
        Registration registration = new Registration();

        // Test numRegistration
        registration.setNumRegistration(1L);
        assertEquals(1L, registration.getNumRegistration());

        // Test numWeek
        registration.setNumWeek(2);
        assertEquals(2, registration.getNumWeek());

        // Test skier
        Skier skier = new Skier();
        registration.setSkier(skier);
        assertEquals(skier, registration.getSkier());

        // Test course
        Course course = new Course();
        registration.setCourse(course);
        assertEquals(course, registration.getCourse());
    }
}