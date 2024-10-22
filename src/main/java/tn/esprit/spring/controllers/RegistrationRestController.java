package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.dto.RegistrationDTO;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.services.IRegistrationServices;

import java.util.List;

@Tag(name = "📋 Registration Management")
@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationRestController {

    private final IRegistrationServices registrationServices;

    @Operation(description = "Add Registration and Assign to Skier")
    @PutMapping("/addAndAssignToSkier/{numSkieur}")
    public RegistrationDTO addAndAssignToSkier(@RequestBody RegistrationDTO registrationDTO,
                                               @PathVariable("numSkieur") Long numSkieur) {
        Registration registration = convertToEntity(registrationDTO);
        Registration savedRegistration = registrationServices.addRegistrationAndAssignToSkier(registration, numSkieur);
        return convertToDTO(savedRegistration);
    }

    @Operation(description = "Assign Registration to Course")
    @PutMapping("/assignToCourse/{numRegis}/{numCourse}")
    public RegistrationDTO assignToCourse(@PathVariable("numRegis") Long numRegistration,
                                          @PathVariable("numCourse") Long numCourse) {
        Registration registration = registrationServices.assignRegistrationToCourse(numRegistration, numCourse);
        return convertToDTO(registration);
    }

    @Operation(description = "Add Registration and Assign to Skier and Course")
    @PutMapping("/addAndAssignToSkierAndCourse/{numSkieur}/{numCourse}")
    public RegistrationDTO addAndAssignToSkierAndCourse(@RequestBody RegistrationDTO registrationDTO,
                                                        @PathVariable("numSkieur") Long numSkieur,
                                                        @PathVariable("numCourse") Long numCourse) {
        Registration registration = convertToEntity(registrationDTO);
        Registration savedRegistration = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, numSkieur, numCourse);
        return convertToDTO(savedRegistration);
    }

    @Operation(description = "Numbers of the weeks when an instructor has given lessons in a given support")
    @GetMapping("/numWeeks/{numInstructor}/{support}")
    public List<Integer> numWeeksCourseOfInstructorBySupport(@PathVariable("numInstructor") Long numInstructor,
                                                             @PathVariable("support") Support support) {
        return registrationServices.numWeeksCourseOfInstructorBySupport(numInstructor, support);
    }

    // Méthodes utilitaires pour la conversion entre Registration et RegistrationDTO
    private Registration convertToEntity(RegistrationDTO registrationDTO) {
        Registration registration = new Registration();
        registration.setNumRegistration(registrationDTO.getNumRegistration());
        registration.setNumWeek(registrationDTO.getNumWeek());
        registration.setSkier(registrationDTO.getSkier());
        registration.setCourse(registrationDTO.getCourse());
        return registration;
    }

    private RegistrationDTO convertToDTO(Registration registration) {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setNumRegistration(registration.getNumRegistration());
        registrationDTO.setNumWeek(registration.getNumWeek());
        registrationDTO.setSkier(registration.getSkier());
        registrationDTO.setCourse(registration.getCourse());
        return registrationDTO;
    }
}