package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;

import java.util.Optional;
import java.util.Set; // Add this import

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SkierServicesImplTest {

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private IPisteRepository pisteRepository;

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SkierServicesImpl skierServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllSkiers() {
        skierServices.retrieveAllSkiers();
        verify(skierRepository, times(1)).findAll();
    }

    @Test
    void testAddSkier() {
        Skier skier = new Skier();
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        skier.setSubscription(subscription);

        skierServices.addSkier(skier);
        assertNotNull(skier.getSubscription().getEndDate());
        verify(skierRepository, times(1)).save(skier);
    }

    @Test
    void testAssignSkierToSubscription() {
        Skier skier = new Skier();
        Subscription subscription = new Subscription();
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        Skier result = skierServices.assignSkierToSubscription(1L, 1L);
        assertEquals(subscription, result.getSubscription());
        verify(skierRepository, times(1)).save(skier);
    }

    @Test
    void testRemoveSkier() {
        skierServices.removeSkier(1L);
        verify(skierRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRetrieveSkier() {
        Skier skier = new Skier();
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));

        Skier result = skierServices.retrieveSkier(1L);
        assertEquals(skier, result);
        verify(skierRepository, times(1)).findById(1L);
    }

    @Test
    void testAddSkierAndAssignToCourse() {
        Skier skier = new Skier();
        Course course = new Course();
        Registration registration = new Registration();
        skier.setRegistrations(Set.of(registration));
        when(skierRepository.save(skier)).thenReturn(skier);
        when(courseRepository.getById(1L)).thenReturn(course);

        Skier result = skierServices.addSkierAndAssignToCourse(skier, 1L);

        assertEquals(skier, result);
        assertEquals(course, registration.getCourse());
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void testAssignSkierToPiste() {
        Skier skier = new Skier();
        Piste piste = new Piste();
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));

        Skier result = skierServices.assignSkierToPiste(1L, 1L);

        assertTrue(result.getPistes().contains(piste));
        verify(skierRepository, times(1)).save(skier);
    }

    @Test
    void testRetrieveSkiersBySubscriptionType() {
        TypeSubscription typeSubscription = TypeSubscription.ANNUAL;
        skierServices.retrieveSkiersBySubscriptionType(typeSubscription);
        verify(skierRepository, times(1)).findBySubscription_TypeSub(typeSubscription);
    }
}