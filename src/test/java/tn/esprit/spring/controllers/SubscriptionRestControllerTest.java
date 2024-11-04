package tn.esprit.spring.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISubscriptionServices;

@WebMvcTest(SubscriptionRestController.class)
class SubscriptionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISubscriptionServices subscriptionServices;

    private Subscription subscription;

    @BeforeEach
    void setup() {
        subscription = new Subscription();
        subscription.setStartDate(LocalDate.of(2024, 1, 1));
        subscription.setTypeSub(TypeSubscription.ANNUAL);
    }

    @Test
    void testAddSubscription() throws Exception {
        when(subscriptionServices.addSubscription(any(Subscription.class))).thenReturn(subscription);
        mockMvc.perform(post("/subscription/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(subscription)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.typeSub").value(TypeSubscription.ANNUAL.toString()));
    }

    @Test
    void testGetById() throws Exception {
        when(subscriptionServices.retrieveSubscriptionById(anyLong())).thenReturn(subscription);
        mockMvc.perform(get("/subscription/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.typeSub").value(TypeSubscription.ANNUAL.toString()));
    }

    @Test
    void testGetSubscriptionsByType() throws Exception {
        Set<Subscription> subscriptions = Collections.singleton(subscription);
        when(subscriptionServices.getSubscriptionByType(any(TypeSubscription.class))).thenReturn(subscriptions);
        mockMvc.perform(get("/subscription/all/ANNUAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testUpdateSubscription() throws Exception {
        when(subscriptionServices.updateSubscription(any(Subscription.class))).thenReturn(subscription);
        mockMvc.perform(put("/subscription/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(subscription)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.typeSub").value(TypeSubscription.ANNUAL.toString()));
    }

    @Test
    void testGetSubscriptionsByDates() throws Exception {
        List<Subscription> subscriptions = Collections.singletonList(subscription);
        when(subscriptionServices.retrieveSubscriptionsByDates(any(LocalDate.class), any(LocalDate.class))).thenReturn(subscriptions);
        mockMvc.perform(get("/subscription/all/2024-01-01/2024-12-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}