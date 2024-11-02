package tn.esprit.spring.controller;

import static org.mockito.Mockito.*;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.controllers.InstructorRestController;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.services.IInstructorServices;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class InstructorRestControllerTest {

    @Mock
    private IInstructorServices instructorServices;

    @InjectMocks
    private InstructorRestController instructorRestController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(instructorRestController).build();
    }

    @Test
    void testAddInstructor() throws Exception {
        Instructor instructor = new Instructor(null, "John", "Doe", LocalDate.now(), null);

        when(instructorServices.addInstructor(any(Instructor.class))).thenReturn(instructor);

        mockMvc.perform(post("/instructor/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"dateOfHire\": \"2024-10-12\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(instructorServices, times(1)).addInstructor(any(Instructor.class));
    }

    @Test
    void testAddInstructorAndAssignToCourse() throws Exception {
        Long courseId = 1L;
        Instructor instructor = new Instructor(null, "John", "Doe", LocalDate.now(), null);

        when(instructorServices.addInstructorAndAssignToCourse(any(Instructor.class), eq(courseId))).thenReturn(instructor);

        mockMvc.perform(put("/instructor/addAndAssignToCourse/{numCourse}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"dateOfHire\": \"2024-10-12\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(instructorServices, times(1)).addInstructorAndAssignToCourse(any(Instructor.class), eq(courseId));
    }

    @Test
    void testGetAllInstructors() throws Exception {
        List<Instructor> instructors = Arrays.asList(
                new Instructor(1L, "John", "Doe", LocalDate.now(), null),
                new Instructor(2L, "Jane", "Smith", LocalDate.now(), null)
        );

        when(instructorServices.retrieveAllInstructors()).thenReturn(instructors);

        mockMvc.perform(get("/instructor/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        verify(instructorServices, times(1)).retrieveAllInstructors();
    }

    @Test
    void testUpdateInstructor() throws Exception {
        Instructor instructor = new Instructor(1L, "John", "Doe", LocalDate.now(), null);

        when(instructorServices.updateInstructor(any(Instructor.class))).thenReturn(instructor);

        mockMvc.perform(put("/instructor/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"numInstructor\": 1, \"firstName\": \"John\", \"lastName\": \"Doe\", \"dateOfHire\": \"2024-10-12\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(instructorServices, times(1)).updateInstructor(any(Instructor.class));
    }

    @Test
    void testRetrieveInstructorById() throws Exception {
        Long instructorId = 1L;
        Instructor instructor = new Instructor(instructorId, "John", "Doe", LocalDate.now(), null);

        when(instructorServices.retrieveInstructor(instructorId)).thenReturn(instructor);

        mockMvc.perform(get("/instructor/get/{id-instructor}", instructorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(instructorServices, times(1)).retrieveInstructor(instructorId);
    }
}