package tn.esprit.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISubscriptionServices;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SubscriptionRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ISubscriptionServices subscriptionServices;

    @InjectMocks
    private SubscriptionRestController subscriptionRestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(subscriptionRestController)
                .build();
    }

    @Test
    public void testAddSubscription() throws Exception {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.of(2023, 1, 1));
        subscription.setEndDate(LocalDate.of(2024, 1, 1));
        subscription.setPrice(100.0f);

        when(subscriptionServices.addSubscription(any(Subscription.class))).thenReturn(subscription);

        mockMvc.perform(post("/subscription/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"typeSub\":\"ANNUAL\", \"startDate\":\"2023-01-01\", \"endDate\":\"2024-01-01\", \"price\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSub").value(1L))
                .andExpect(jsonPath("$.typeSub").value("ANNUAL"))
                .andExpect(jsonPath("$.startDate[0]").value(2023))
                .andExpect(jsonPath("$.startDate[1]").value(1))
                .andExpect(jsonPath("$.startDate[2]").value(1))
                .andExpect(jsonPath("$.endDate[0]").value(2024))
                .andExpect(jsonPath("$.endDate[1]").value(1))
                .andExpect(jsonPath("$.endDate[2]").value(1))
                .andExpect(jsonPath("$.price").value(100.0));

        verify(subscriptionServices, times(1)).addSubscription(any(Subscription.class));
    }
    @Test
    void getById_ShouldReturnSubscriptionIfExists() throws Exception {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.now());

        when(subscriptionServices.retrieveSubscriptionById(1L)).thenReturn(subscription);

        mockMvc.perform(get("/subscription/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSub").value(1L))
                .andExpect(jsonPath("$.typeSub").value("ANNUAL"));
    }





}