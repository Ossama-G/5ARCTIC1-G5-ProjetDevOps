package tn.esprit.spring.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.services.ISkierServices;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SkierRestControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ISkierServices skierServices;

    @InjectMocks
    private SkierRestController skierRestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(skierRestController).build();
    }

    @Test
    public void testAddSkier() throws Exception {
        Skier skier = new Skier();
        skier.setNumSkier(1L);
        skier.setFirstName("John");
        skier.setLastName("Doe");

        when(skierServices.addSkier(any(Skier.class))).thenReturn(skier);

        mockMvc.perform(post("/skier/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\", \"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSkier").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(skierServices, times(1)).addSkier(any(Skier.class));
    }

    @Test
    public void testGetSkierById() throws Exception {
        Skier skier = new Skier();
        skier.setNumSkier(1L);
        skier.setFirstName("John");
        skier.setLastName("Doe");

        when(skierServices.retrieveSkier(1L)).thenReturn(skier);

        mockMvc.perform(get("/skier/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSkier").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(skierServices, times(1)).retrieveSkier(1L);
    }

    @Test
    public void testDeleteSkierById() throws Exception {
        doNothing().when(skierServices).removeSkier(1L);

        mockMvc.perform(delete("/skier/delete/1"))
                .andExpect(status().isOk());

        verify(skierServices, times(1)).removeSkier(1L);
    }
}