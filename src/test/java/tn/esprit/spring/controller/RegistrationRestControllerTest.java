package tn.esprit.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.controllers.RegistrationRestController;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.services.IRegistrationServices;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RegistrationRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IRegistrationServices registrationServices;

    @InjectMocks
    private RegistrationRestController registrationRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registrationRestController).build();
    }

    @Test
    void testAddAndAssignToSkier() throws Exception {
        Registration registration = new Registration();
        registration.setNumWeek(1); // Set numWeek
        when(registrationServices.addRegistrationAndAssignToSkier(any(Registration.class), anyLong())).thenReturn(registration);

        mockMvc.perform(put("/registration/addAndAssignToSkier/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numWeek\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numWeek").value(1));

        verify(registrationServices, times(1)).addRegistrationAndAssignToSkier(any(Registration.class), anyLong());
    }

    @Test
    void testAssignToCourse() throws Exception {
        Registration registration = new Registration();
        registration.setNumWeek(1); // Set numWeek
        when(registrationServices.assignRegistrationToCourse(anyLong(), anyLong())).thenReturn(registration);

        mockMvc.perform(put("/registration/assignToCourse/1/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numWeek").value(1));

        verify(registrationServices, times(1)).assignRegistrationToCourse(anyLong(), anyLong());
    }

    @Test
    void testAddAndAssignToSkierAndCourse() throws Exception {
        Registration registration = new Registration();
        registration.setNumWeek(1); // Set numWeek
        when(registrationServices.addRegistrationAndAssignToSkierAndCourse(any(Registration.class), anyLong(), anyLong())).thenReturn(registration);

        mockMvc.perform(put("/registration/addAndAssignToSkierAndCourse/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numWeek\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numWeek").value(1));

        verify(registrationServices, times(1)).addRegistrationAndAssignToSkierAndCourse(any(Registration.class), anyLong(), anyLong());
    }

    @Test
    void testNumWeeksCourseOfInstructorBySupport() throws Exception {
        List<Integer> weeks = Arrays.asList(1, 2, 3);
        when(registrationServices.numWeeksCourseOfInstructorBySupport(anyLong(), any(Support.class))).thenReturn(weeks);

        mockMvc.perform(get("/registration/numWeeks/1/SKI")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(1))
                .andExpect(jsonPath("$[1]").value(2))
                .andExpect(jsonPath("$[2]").value(3));

        verify(registrationServices, times(1)).numWeeksCourseOfInstructorBySupport(anyLong(), any(Support.class));
    }
}