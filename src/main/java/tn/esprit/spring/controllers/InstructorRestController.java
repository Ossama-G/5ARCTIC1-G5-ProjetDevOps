package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.dto.InstructorDTO;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.services.IInstructorServices;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "📚 Instructor Management")
@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorRestController {

    private final IInstructorServices instructorServices;

    @Operation(description = "Add Instructor")
    @PostMapping("/add")
    public InstructorDTO addInstructor(@RequestBody InstructorDTO instructorDTO) {
        Instructor instructor = convertToEntity(instructorDTO);
        Instructor savedInstructor = instructorServices.addInstructor(instructor);
        return convertToDTO(savedInstructor);
    }

    @Operation(description = "Add Instructor and Assign To Course")
    @PutMapping("/addAndAssignToCourse/{numCourse}")
    public InstructorDTO addAndAssignToInstructor(@RequestBody InstructorDTO instructorDTO, @PathVariable("numCourse") Long numCourse) {
        Instructor instructor = convertToEntity(instructorDTO);
        Instructor assignedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, numCourse);
        return convertToDTO(assignedInstructor);
    }

    @Operation(description = "Retrieve all Instructors")
    @GetMapping("/all")
    public List<InstructorDTO> getAllInstructors() {
        return instructorServices.retrieveAllInstructors()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Operation(description = "Update Instructor")
    @PutMapping("/update")
    public InstructorDTO updateInstructor(@RequestBody InstructorDTO instructorDTO) {
        Instructor instructor = convertToEntity(instructorDTO);
        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);
        return convertToDTO(updatedInstructor);
    }

    @Operation(description = "Retrieve Instructor by Id")
    @GetMapping("/get/{id-instructor}")
    public InstructorDTO getById(@PathVariable("id-instructor") Long id) {
        Instructor instructor = instructorServices.retrieveInstructor(id);
        return convertToDTO(instructor);
    }

    // Méthodes utilitaires pour la conversion entre Instructor et InstructorDTO
    private Instructor convertToEntity(InstructorDTO instructorDTO) {
        Instructor instructor = new Instructor();
        instructor.setNumInstructor(instructorDTO.getNumInstructor());
        instructor.setFirstName(instructorDTO.getFirstName());
        instructor.setLastName(instructorDTO.getLastName());
        instructor.setDateOfHire(instructorDTO.getDateOfHire());
        return instructor;
    }

    private InstructorDTO convertToDTO(Instructor instructor) {
        InstructorDTO instructorDTO = new InstructorDTO();
        instructorDTO.setNumInstructor(instructor.getNumInstructor());
        instructorDTO.setFirstName(instructor.getFirstName());
        instructorDTO.setLastName(instructor.getLastName());
        instructorDTO.setDateOfHire(instructor.getDateOfHire());
        return instructorDTO;
    }
}
