package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SkierServicesImplTest {

    @InjectMocks
    private SkierServicesImpl skierServices;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveAllSkiers() {
        skierServices.retrieveAllSkiers();
        verify(skierRepository, times(1)).findAll();
    }

    @Test
    public void testAddSkier() {
        Skier skier = new Skier();
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        skier.setSubscription(subscription);

        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        Skier result = skierServices.addSkier(skier);
        assertEquals(subscription, result.getSubscription());
        assertNotNull(result.getSubscription().getEndDate());
        verify(skierRepository, times(1)).save(skier);
    }

    @Test
    public void testAssignSkierToSubscription() {
        Skier skier = new Skier();
        Subscription subscription = new Subscription();

        when(skierRepository.findById(anyLong())).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(anyLong())).thenReturn(Optional.of(subscription));

        Skier result = skierServices.assignSkierToSubscription(1L, 1L);
        assertEquals(subscription, result.getSubscription());
        verify(skierRepository, times(1)).save(skier);
    }
}