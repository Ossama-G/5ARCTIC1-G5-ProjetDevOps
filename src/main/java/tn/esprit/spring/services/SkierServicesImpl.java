package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class SkierServicesImpl implements ISkierServices {

    private static final String SKIER_NOT_FOUND_MESSAGE = "Skier not found";

    private ISkierRepository skierRepository;
    private IPisteRepository pisteRepository;
    private ICourseRepository courseRepository;
    private IRegistrationRepository registrationRepository;
    private ISubscriptionRepository subscriptionRepository;

    @Override
    public List<Skier> retrieveAllSkiers() {
        return skierRepository.findAll();
    }

    @Override
    public Skier addSkier(Skier skier) {
        switch (skier.getSubscription().getTypeSub()) {
            case ANNUAL:
                skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusYears(1));
                break;
            case SEMESTRIEL:
                skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusMonths(6));
                break;
            case MONTHLY:
                skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusMonths(1));
                break;
        }
        return skierRepository.save(skier);
    }

    @Override
    public Skier assignSkierToSubscription(Long numSkier, Long numSubscription) {
        // Added null check and exception handling to avoid NullPointerException
        Skier skier = skierRepository.findById(numSkier).orElseThrow(() -> new IllegalArgumentException(SKIER_NOT_FOUND_MESSAGE));
        Subscription subscription = subscriptionRepository.findById(numSubscription).orElseThrow(() -> new IllegalArgumentException("Subscription not found"));
        skier.setSubscription(subscription);
        return skierRepository.save(skier);
    }

    @Override
    public Skier addSkierAndAssignToCourse(Skier skier, Long numCourse) {
        // Added null check and exception handling to avoid NullPointerException
        Skier savedSkier = skierRepository.save(skier);
        Course course = courseRepository.findById(numCourse).orElseThrow(() -> new IllegalArgumentException("Course not found"));
        Set<Registration> registrations = savedSkier.getRegistrations();
        for (Registration r : registrations) {
            r.setSkier(savedSkier);
            r.setCourse(course);
            registrationRepository.save(r);
        }
        return savedSkier;
    }

    @Override
    public void removeSkier(Long numSkier) {
        skierRepository.deleteById(numSkier);
    }

    @Override
    public Skier retrieveSkier(Long numSkier) {
        // Added null check and exception handling to avoid NullPointerException
        return skierRepository.findById(numSkier).orElseThrow(() -> new IllegalArgumentException(SKIER_NOT_FOUND_MESSAGE));
    }

    @Override
    public Skier assignSkierToPiste(Long numSkieur, Long numPiste) {
        // Added null check and exception handling to avoid NullPointerException
        Skier skier = skierRepository.findById(numSkieur).orElseThrow(() -> new IllegalArgumentException(SKIER_NOT_FOUND_MESSAGE));
        Piste piste = pisteRepository.findById(numPiste).orElseThrow(() -> new IllegalArgumentException("Piste not found"));
        Set<Piste> pistes = skier.getPistes();
        if (pistes == null) {
            // Initialize pistes set if null to avoid NullPointerException
            pistes = new HashSet<>();
            skier.setPistes(pistes);
        }
        pistes.add(piste);
        return skierRepository.save(skier);
    }

    @Override
    public List<Skier> retrieveSkiersBySubscriptionType(TypeSubscription typeSubscription) {
        return skierRepository.findBySubscription_TypeSub(typeSubscription);
    }
}
