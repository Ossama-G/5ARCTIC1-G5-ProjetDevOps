package tn.esprit.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.controllers.SkierRestController;
import tn.esprit.spring.dto.SkierDTO;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISkierServices;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SkierRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ISkierServices skierServices;

    @InjectMocks
    private SkierRestController skierRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(skierRestController).build();
    }

    @Test
    void testAddSkier() throws Exception {
        Skier skier = new Skier();
        skier.setFirstName("John");
        skier.setLastName("Doe");
        when(skierServices.addSkier(any(Skier.class))).thenReturn(skier);

        mockMvc.perform(post("/skier/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\", \"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(skierServices, times(1)).addSkier(any(Skier.class));
    }

    @Test
    void testAddSkierAndAssignToCourse() throws Exception {
        Skier skier = new Skier();
        skier.setFirstName("John");
        skier.setLastName("Doe");
        when(skierServices.addSkierAndAssignToCourse(any(Skier.class), anyLong())).thenReturn(skier);

        mockMvc.perform(post("/skier/addAndAssign/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\", \"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(skierServices, times(1)).addSkierAndAssignToCourse(any(Skier.class), anyLong());
    }

    @Test
    void testAssignToSubscription() throws Exception {
        Skier skier = new Skier();
        skier.setFirstName("John");
        skier.setLastName("Doe");
        when(skierServices.assignSkierToSubscription(anyLong(), anyLong())).thenReturn(skier);

        mockMvc.perform(put("/skier/assignToSub/1/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(skierServices, times(1)).assignSkierToSubscription(anyLong(), anyLong());
    }

    @Test
    void testAssignToPiste() throws Exception {
        Skier skier = new Skier();
        skier.setFirstName("John");
        skier.setLastName("Doe");
        when(skierServices.assignSkierToPiste(anyLong(), anyLong())).thenReturn(skier);

        mockMvc.perform(put("/skier/assignToPiste/1/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(skierServices, times(1)).assignSkierToPiste(anyLong(), anyLong());
    }

    @Test
    void testRetrieveSkiersBySubscriptionType() throws Exception {
        Skier skier1 = new Skier();
        skier1.setFirstName("Skier");
        skier1.setLastName("One");
        Skier skier2 = new Skier();
        skier2.setFirstName("Skier");
        skier2.setLastName("Two");
        List<Skier> skiers = Arrays.asList(skier1, skier2);
        when(skierServices.retrieveSkiersBySubscriptionType(any(TypeSubscription.class))).thenReturn(skiers);

        mockMvc.perform(get("/skier/getSkiersBySubscription")
                        .param("typeSubscription", "ANNUAL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(skierServices, times(1)).retrieveSkiersBySubscriptionType(any(TypeSubscription.class));
    }

    @Test
    void testGetById() throws Exception {
        Skier skier = new Skier();
        skier.setFirstName("John");
        skier.setLastName("Doe");
        when(skierServices.retrieveSkier(anyLong())).thenReturn(skier);

        mockMvc.perform(get("/skier/get/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(skierServices, times(1)).retrieveSkier(anyLong());
    }

    @Test
    void testDeleteById() throws Exception {
        doNothing().when(skierServices).removeSkier(anyLong());

        mockMvc.perform(delete("/skier/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(skierServices, times(1)).removeSkier(anyLong());
    }

    @Test
    void testGetAllSkiers() throws Exception {
        Skier skier1 = new Skier();
        skier1.setFirstName("Skier");
        skier1.setLastName("One");
        Skier skier2 = new Skier();
        skier2.setFirstName("Skier");
        skier2.setLastName("Two");
        List<Skier> skiers = Arrays.asList(skier1, skier2);
        when(skierServices.retrieveAllSkiers()).thenReturn(skiers);

        mockMvc.perform(get("/skier/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(skierServices, times(1)).retrieveAllSkiers();
    }

    @Test
    void testConvertToEntity() {
        SkierDTO skierDTO = new SkierDTO();
        skierDTO.setNumSkier(1L);
        skierDTO.setFirstName("John");
        skierDTO.setLastName("Doe");
        skierDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        skierDTO.setCity("City");

        Skier skier = skierRestController.convertToEntity(skierDTO);

        assertNotNull(skier);
        assertEquals(skierDTO.getNumSkier(), skier.getNumSkier());
        assertEquals(skierDTO.getFirstName(), skier.getFirstName());
        assertEquals(skierDTO.getLastName(), skier.getLastName());
        assertEquals(skierDTO.getDateOfBirth(), skier.getDateOfBirth());
        assertEquals(skierDTO.getCity(), skier.getCity());
    }
}