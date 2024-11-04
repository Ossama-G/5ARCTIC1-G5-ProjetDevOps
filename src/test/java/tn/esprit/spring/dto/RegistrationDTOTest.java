package tn.esprit.spring.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Skier;

class RegistrationDTOTest {

    @Test
    void testRegistrationDTOGettersAndSetters() {
        RegistrationDTO registrationDTO = new RegistrationDTO();

        registrationDTO.setNumRegistration(1L);
        assertEquals(1L, registrationDTO.getNumRegistration());

        registrationDTO.setNumWeek(2);
        assertEquals(2, registrationDTO.getNumWeek());

        Skier skier = new Skier();
        registrationDTO.setSkier(skier);
        assertEquals(skier, registrationDTO.getSkier());

        Course course = new Course();
        registrationDTO.setCourse(course);
        assertEquals(course, registrationDTO.getCourse());
    }
}