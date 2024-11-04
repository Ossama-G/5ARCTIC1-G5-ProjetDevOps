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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PisteRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IPisteServices pisteServices;

    @InjectMocks
    private PisteRestController pisteRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pisteRestController).build();
    }

    @Test
    void testAddPiste() throws Exception {
        Piste piste = new Piste();
        piste.setNamePiste("Test Piste");
        when(pisteServices.addPiste(any(Piste.class))).thenReturn(piste);

        mockMvc.perform(post("/piste/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"namePiste\":\"Test Piste\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.namePiste").value("Test Piste"));

        verify(pisteServices, times(1)).addPiste(any(Piste.class));
    }

    @Test
    void testGetAllPistes() throws Exception {
        Piste piste1 = new Piste();
        Piste piste2 = new Piste();
        List<Piste> pistes = Arrays.asList(piste1, piste2);

        when(pisteServices.retrieveAllPistes()).thenReturn(pistes);

        mockMvc.perform(get("/piste/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(pisteServices, times(1)).retrieveAllPistes();
    }

    @Test
    void testGetById() throws Exception {
        Piste piste = new Piste();
        when(pisteServices.retrievePiste(anyLong())).thenReturn(piste);

        mockMvc.perform(get("/piste/get/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(pisteServices, times(1)).retrievePiste(anyLong());
    }

    @Test
    void testDeleteById() throws Exception {
        doNothing().when(pisteServices).removePiste(anyLong());

        mockMvc.perform(delete("/piste/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(pisteServices, times(1)).removePiste(anyLong());
    }
}