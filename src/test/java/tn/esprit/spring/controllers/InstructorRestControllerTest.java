package tn.esprit.spring.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.services.IInstructorServices;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class InstructorRestControllerTest {
    private MockMvc mockMvc;

    @Mock
    private IInstructorServices instructorServices;

    @InjectMocks
    private InstructorRestController instructorRestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(instructorRestController).build();
    }

    @Test
    public void testAddInstructor() throws Exception {
        Instructor instructor = new Instructor();
        instructor.setNumInstructor(1L);
        instructor.setFirstName("John");
        instructor.setLastName("Doe");
        instructor.setDateOfHire(LocalDate.of(2020, 1, 1));
        instructor.setCourses(new HashSet<>());

        when(instructorServices.addInstructor(any(Instructor.class))).thenReturn(instructor);

        mockMvc.perform(post("/instructor/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\", \"lastName\":\"Doe\", \"dateOfHire\":\"2020-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numInstructor").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.dateOfHire[0]").value(2020))
                .andExpect(jsonPath("$.dateOfHire[1]").value(1))
                .andExpect(jsonPath("$.dateOfHire[2]").value(1));

        verify(instructorServices, times(1)).addInstructor(any(Instructor.class));
    }

    @Test
    public void testGetAllInstructors() throws Exception {
        Instructor instructor1 = new Instructor();
        instructor1.setNumInstructor(1L);
        instructor1.setFirstName("John");
        instructor1.setLastName("Doe");
        instructor1.setDateOfHire(LocalDate.of(2020, 1, 1));
        instructor1.setCourses(new HashSet<>());

        Instructor instructor2 = new Instructor();
        instructor2.setNumInstructor(2L);
        instructor2.setFirstName("Jane");
        instructor2.setLastName("Smith");
        instructor2.setDateOfHire(LocalDate.of(2021, 2, 2));
        instructor2.setCourses(new HashSet<>());

        List<Instructor> instructors = Arrays.asList(instructor1, instructor2);

        when(instructorServices.retrieveAllInstructors()).thenReturn(instructors);

        mockMvc.perform(get("/instructor/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numInstructor").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].dateOfHire[0]").value(2020))
                .andExpect(jsonPath("$[0].dateOfHire[1]").value(1))
                .andExpect(jsonPath("$[0].dateOfHire[2]").value(1))
                .andExpect(jsonPath("$[1].numInstructor").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].dateOfHire[0]").value(2021))
                .andExpect(jsonPath("$[1].dateOfHire[1]").value(2))
                .andExpect(jsonPath("$[1].dateOfHire[2]").value(2));

        verify(instructorServices, times(1)).retrieveAllInstructors();
    }

    @Test
    public void testUpdateInstructor() throws Exception {
        Instructor instructor = new Instructor();
        instructor.setNumInstructor(1L);
        instructor.setFirstName("John");
        instructor.setLastName("Doe");
        instructor.setDateOfHire(LocalDate.of(2020, 1, 1));
        instructor.setCourses(new HashSet<>());

        when(instructorServices.updateInstructor(any(Instructor.class))).thenReturn(instructor);

        mockMvc.perform(put("/instructor/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numInstructor\":1, \"firstName\":\"John\", \"lastName\":\"Doe\", \"dateOfHire\":\"2020-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numInstructor").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.dateOfHire[0]").value(2020))
                .andExpect(jsonPath("$.dateOfHire[1]").value(1))
                .andExpect(jsonPath("$.dateOfHire[2]").value(1));

        verify(instructorServices, times(1)).updateInstructor(any(Instructor.class));
    }

    @Test
    public void testGetInstructorById() throws Exception {
        Instructor instructor = new Instructor();
        instructor.setNumInstructor(1L);
        instructor.setFirstName("John");
        instructor.setLastName("Doe");
        instructor.setDateOfHire(LocalDate.of(2020, 1, 1));
        instructor.setCourses(new HashSet<>());

        when(instructorServices.retrieveInstructor(1L)).thenReturn(instructor);

        mockMvc.perform(get("/instructor/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numInstructor").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.dateOfHire[0]").value(2020))
                .andExpect(jsonPath("$.dateOfHire[1]").value(1))
                .andExpect(jsonPath("$.dateOfHire[2]").value(1));

        verify(instructorServices, times(1)).retrieveInstructor(1L);
    }
}