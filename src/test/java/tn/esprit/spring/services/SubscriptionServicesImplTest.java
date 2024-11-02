package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServicesImplTest {

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ISkierRepository skierRepository;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSubscription() {
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusYears(1));

        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        Subscription result = subscriptionServices.addSubscription(subscription);

        assertNotNull(result);
        assertEquals(subscription.getEndDate(), result.getEndDate());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void testUpdateSubscription() {
        Subscription subscription = new Subscription();
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        Subscription result = subscriptionServices.updateSubscription(subscription);

        assertNotNull(result);
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void testRetrieveSubscriptionById() {
        Subscription subscription = new Subscription();
        when(subscriptionRepository.findById(anyLong())).thenReturn(java.util.Optional.of(subscription));

        Subscription result = subscriptionServices.retrieveSubscriptionById(1L);

        assertNotNull(result);
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetSubscriptionByType() {
        Set<Subscription> subscriptions = new HashSet<>(Arrays.asList(new Subscription(), new Subscription()));
        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(any(TypeSubscription.class))).thenReturn(subscriptions);

        Set<Subscription> result = subscriptionServices.getSubscriptionByType(TypeSubscription.ANNUAL);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(TypeSubscription.ANNUAL);
    }

    @Test
    void testRetrieveSubscriptionsByDates() {
        List<Subscription> subscriptions = Arrays.asList(new Subscription(), new Subscription());
        when(subscriptionRepository.getSubscriptionsByStartDateBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(subscriptions);

        List<Subscription> result = subscriptionServices.retrieveSubscriptionsByDates(LocalDate.now(), LocalDate.now().plusDays(1));

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(subscriptionRepository, times(1)).getSubscriptionsByStartDateBetween(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void testRetrieveSubscriptions() {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L); // Ensure numSub is set
        subscription.setEndDate(LocalDate.now().plusYears(1)); // Set endDate
        Skier skier = new Skier();
        skier.setFirstName("John");
        skier.setLastName("Doe");

        when(subscriptionRepository.findDistinctOrderByEndDateAsc()).thenReturn(Arrays.asList(subscription));
        when(skierRepository.findBySubscription(any(Subscription.class))).thenReturn(skier);

        subscriptionServices.retrieveSubscriptions();

        verify(subscriptionRepository, times(1)).findDistinctOrderByEndDateAsc();
        verify(skierRepository, times(1)).findBySubscription(subscription);
    }

    @Test
    void testShowMonthlyRecurringRevenue() {
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)).thenReturn(1000F);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL)).thenReturn(6000F);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL)).thenReturn(12000F);

        subscriptionServices.showMonthlyRecurringRevenue();

        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL);
    }
}