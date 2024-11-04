package tn.esprit.spring.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InstructorDTO {
    private Long numInstructor;
    private String firstName;
    private String lastName;
    private LocalDate dateOfHire;
}