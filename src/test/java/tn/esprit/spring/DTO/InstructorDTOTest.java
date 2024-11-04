package tn.esprit.spring.DTO;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class InstructorDTOTest {

    @Test
    void testInstructorDTOGettersAndSetters() {
        InstructorDTO instructorDTO = new InstructorDTO();

        instructorDTO.setNumInstructor(1L);
        assertEquals(1L, instructorDTO.getNumInstructor());

        instructorDTO.setFirstName("John");
        assertEquals("John", instructorDTO.getFirstName());

        instructorDTO.setLastName("Doe");
        assertEquals("Doe", instructorDTO.getLastName());

        LocalDate dateOfHire = LocalDate.of(2020, 1, 1);
        instructorDTO.setDateOfHire(dateOfHire);
        assertEquals(dateOfHire, instructorDTO.getDateOfHire());
    }
}