package tn.esprit.spring.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

class SkierDTOTest {

    @Test
    void testSkierDTOGettersAndSetters() {
        SkierDTO skierDTO = new SkierDTO();

        skierDTO.setNumSkier(1L);
        assertEquals(1L, skierDTO.getNumSkier());

        skierDTO.setFirstName("John");
        assertEquals("John", skierDTO.getFirstName());

        skierDTO.setLastName("Doe");
        assertEquals("Doe", skierDTO.getLastName());

        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        skierDTO.setDateOfBirth(dateOfBirth);
        assertEquals(dateOfBirth, skierDTO.getDateOfBirth());

        skierDTO.setCity("New York");
        assertEquals("New York", skierDTO.getCity());

        skierDTO.setSubscriptionId(1L);
        assertEquals(1L, skierDTO.getSubscriptionId());
    }
}