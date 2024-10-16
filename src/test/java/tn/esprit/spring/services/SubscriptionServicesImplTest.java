package tn.esprit.spring.services;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import tn.esprit.spring.services.SubscriptionServicesImpl;

@SpringBootTest
public class SubscriptionServicesImplTest {

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ISkierRepository skierRepository;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;

    private Subscription subscription;

    @BeforeEach
    public void setup() {
        subscription = new Subscription();
        subscription.setStartDate(LocalDate.of(2024, 1, 1));
        subscription.setTypeSub(TypeSubscription.ANNUAL);
    }

    @Test
    public void testAddSubscription_Annual() {
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);
        Subscription result = subscriptionServices.addSubscription(subscription);
        assertEquals(LocalDate.of(2025, 1, 1), result.getEndDate()); // Annual adds 1 year
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    public void testRetrieveSubscriptionById() {
        when(subscriptionRepository.findById(anyLong())).thenReturn(Optional.of(subscription));
        Subscription result = subscriptionServices.retrieveSubscriptionById(1L);
        assertNotNull(result);
        assertEquals(subscription.getNumSub(), result.getNumSub());
    }

    @Test
    public void testGetSubscriptionByType() {
        Set<Subscription> subscriptions = Set.of(subscription);
        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(TypeSubscription.ANNUAL)).thenReturn(subscriptions);
        Set<Subscription> result = subscriptionServices.getSubscriptionByType(TypeSubscription.ANNUAL);
        assertEquals(subscriptions, result);
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(TypeSubscription.ANNUAL);
    }

    @Test
    public void testRetrieveSubscriptionsByDates() {
        List<Subscription> subscriptions = List.of(subscription);
        when(subscriptionRepository.getSubscriptionsByStartDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(subscriptions);
        List<Subscription> result = subscriptionServices.retrieveSubscriptionsByDates(
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        assertEquals(subscriptions, result);
        verify(subscriptionRepository, times(1)).getSubscriptionsByStartDateBetween(any(LocalDate.class), any(LocalDate.class));
    }
}
