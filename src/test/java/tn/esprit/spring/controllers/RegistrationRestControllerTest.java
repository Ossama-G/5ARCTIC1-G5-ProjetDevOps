package tn.esprit.spring.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.services.IRegistrationServices;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RegistrationRestControllerTest {
    private MockMvc mockMvc;

    @Mock
    private IRegistrationServices registrationServices;

    @InjectMocks
    private RegistrationRestController registrationRestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registrationRestController).build();
    }

    @Test
    public void testAddAndAssignToSkier() throws Exception {
        Registration registration = new Registration();
        registration.setNumRegistration(1L);
        registration.setNumWeek(1);

        when(registrationServices.addRegistrationAndAssignToSkier(any(Registration.class), eq(1L))).thenReturn(registration);

        mockMvc.perform(put("/registration/addAndAssignToSkier/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numWeek\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numRegistration").value(1L))
                .andExpect(jsonPath("$.numWeek").value(1));

        verify(registrationServices, times(1)).addRegistrationAndAssignToSkier(any(Registration.class), eq(1L));
    }

    @Test
    public void testAssignToCourse() throws Exception {
        Registration registration = new Registration();
        registration.setNumRegistration(1L);
        registration.setNumWeek(1);

        when(registrationServices.assignRegistrationToCourse(eq(1L), eq(1L))).thenReturn(registration);

        mockMvc.perform(put("/registration/assignToCourse/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numRegistration").value(1L))
                .andExpect(jsonPath("$.numWeek").value(1));

        verify(registrationServices, times(1)).assignRegistrationToCourse(eq(1L), eq(1L));
    }

    @Test
    public void testAddAndAssignToSkierAndCourse() throws Exception {
        Registration registration = new Registration();
        registration.setNumRegistration(1L);
        registration.setNumWeek(1);

        when(registrationServices.addRegistrationAndAssignToSkierAndCourse(any(Registration.class), eq(1L), eq(1L))).thenReturn(registration);

        mockMvc.perform(put("/registration/addAndAssignToSkierAndCourse/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numWeek\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numRegistration").value(1L))
                .andExpect(jsonPath("$.numWeek").value(1));

        verify(registrationServices, times(1)).addRegistrationAndAssignToSkierAndCourse(any(Registration.class), eq(1L), eq(1L));
    }

    @Test
    public void testNumWeeksCourseOfInstructorBySupport() throws Exception {
        List<Integer> weeks = Arrays.asList(1, 2, 3);

        when(registrationServices.numWeeksCourseOfInstructorBySupport(eq(1L), eq(Support.SKI))).thenReturn(weeks);

        mockMvc.perform(get("/registration/numWeeks/1/SKI"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(1))
                .andExpect(jsonPath("$[1]").value(2))
                .andExpect(jsonPath("$[2]").value(3));

        verify(registrationServices, times(1)).numWeeksCourseOfInstructorBySupport(eq(1L), eq(Support.SKI));
    }
}