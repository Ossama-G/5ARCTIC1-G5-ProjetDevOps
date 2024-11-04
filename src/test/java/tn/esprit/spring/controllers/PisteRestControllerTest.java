package tn.esprit.spring.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.services.IPisteServices;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PisteRestControllerTest {
    private MockMvc mockMvc;

    @Mock
    private IPisteServices pisteServices;

    @InjectMocks
    private PisteRestController pisteRestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pisteRestController).build();
    }

    @Test
    public void testAddPiste() throws Exception {
        Piste piste = new Piste();
        piste.setNumPiste(1L);
        piste.setNamePiste("Test Piste");

        when(pisteServices.addPiste(any(Piste.class))).thenReturn(piste);

        mockMvc.perform(post("/piste/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"namePiste\":\"Test Piste\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numPiste").value(1L))
                .andExpect(jsonPath("$.namePiste").value("Test Piste"));

        verify(pisteServices, times(1)).addPiste(any(Piste.class));
    }

    @Test
    public void testGetPisteById() throws Exception {
        Piste piste = new Piste();
        piste.setNumPiste(1L);
        piste.setNamePiste("Test Piste");

        when(pisteServices.retrievePiste(1L)).thenReturn(piste);

        mockMvc.perform(get("/piste/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numPiste").value(1L))
                .andExpect(jsonPath("$.namePiste").value("Test Piste"));

        verify(pisteServices, times(1)).retrievePiste(1L);
    }

    @Test
    public void testDeletePisteById() throws Exception {
        doNothing().when(pisteServices).removePiste(1L);

        mockMvc.perform(delete("/piste/delete/1"))
                .andExpect(status().isOk());

        verify(pisteServices, times(1)).removePiste(1L);
    }
}