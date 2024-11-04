package tn.esprit.spring.DTO;

import lombok.Data;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Skier;

@Data
public class RegistrationDTO {
    private Long numRegistration;
    private int numWeek;
    private Skier skier;
    private Course course;
}