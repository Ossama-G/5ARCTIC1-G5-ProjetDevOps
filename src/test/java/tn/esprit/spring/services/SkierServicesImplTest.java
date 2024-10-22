package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SkierServicesImplTest {

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private IPisteRepository pisteRepository;

    @InjectMocks
    private SkierServicesImpl skierServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllSkiers() {
        Skier skier1 = new Skier();
        Skier skier2 = new Skier();
        when(skierRepository.findAll()).thenReturn(Arrays.asList(skier1, skier2));

        List<Skier> skiers = skierServices.retrieveAllSkiers();
        assertEquals(2, skiers.size());
        verify(skierRepository, times(1)).findAll();
    }

    @Test
    void testAddSkier() {
        Skier skier = new Skier();
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.now());
        skier.setSubscription(subscription);

        when(skierRepository.save(skier)).thenReturn(skier);

        Skier savedSkier = skierServices.addSkier(skier);
        assertNotNull(savedSkier);
        verify(skierRepository, times(1)).save(skier);
    }

    @Test
    void testRetrieveSkier() {
        Skier skier = new Skier();
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));

        Skier retrievedSkier = skierServices.retrieveSkier(1L);
        assertNotNull(retrievedSkier);
        verify(skierRepository, times(1)).findById(1L);
    }

    @Test
    void testAssignSkierToSubscription() {
        Skier skier = new Skier();
        Subscription subscription = new Subscription();
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));
        when(skierRepository.save(skier)).thenReturn(skier);

        Skier result = skierServices.assignSkierToSubscription(1L, 1L);
        assertNotNull(result);
        assertEquals(subscription, result.getSubscription());
        verify(skierRepository, times(1)).findById(1L);
        verify(subscriptionRepository, times(1)).findById(1L);
        verify(skierRepository, times(1)).save(skier);
    }

    @Test
    void testAddSkierAndAssignToCourse() {
        Skier skier = new Skier();
        Course course = new Course();
        Registration registration = new Registration();
        skier.setRegistrations(new HashSet<>(Arrays.asList(registration)));
        when(skierRepository.save(skier)).thenReturn(skier);
        when(courseRepository.getById(1L)).thenReturn(course);
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Skier result = skierServices.addSkierAndAssignToCourse(skier, 1L);
        assertNotNull(result);
        assertEquals(course, registration.getCourse());
        verify(skierRepository, times(1)).save(skier);
        verify(courseRepository, times(1)).getById(1L);
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void testRemoveSkier() {
        doNothing().when(skierRepository).deleteById(1L);

        skierServices.removeSkier(1L);
        verify(skierRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAssignSkierToPiste() {
        Skier skier = new Skier();
        Piste piste = new Piste();
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));
        when(skierRepository.save(skier)).thenReturn(skier);

        Skier result = skierServices.assignSkierToPiste(1L, 1L);
        assertNotNull(result);
        assertTrue(result.getPistes().contains(piste));
        verify(skierRepository, times(1)).findById(1L);
        verify(pisteRepository, times(1)).findById(1L);
        verify(skierRepository, times(1)).save(skier);
    }

    @Test
    void testRetrieveSkiersBySubscriptionType() {
        Skier skier1 = new Skier();
        Skier skier2 = new Skier();
        when(skierRepository.findBySubscription_TypeSub(TypeSubscription.ANNUAL)).thenReturn(Arrays.asList(skier1, skier2));

        List<Skier> skiers = skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.ANNUAL);
        assertEquals(2, skiers.size());
        verify(skierRepository, times(1)).findBySubscription_TypeSub(TypeSubscription.ANNUAL);
    }
}