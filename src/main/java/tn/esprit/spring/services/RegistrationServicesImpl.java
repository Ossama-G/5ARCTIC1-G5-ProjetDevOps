package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class RegistrationServicesImpl implements IRegistrationServices {

    private final IRegistrationRepository registrationRepository;
    private final ISkierRepository skierRepository;
    private final ICourseRepository courseRepository;

    @Override
    public Registration addRegistrationAndAssignToSkier(Registration registration, Long numSkier) {
        // Récupération du skieur par ID, ou lever une exception si non trouvé
        Skier skier = skierRepository.findById(numSkier).orElseThrow(() -> new IllegalArgumentException("Skier not found"));
        registration.setSkier(skier);
        return registrationRepository.save(registration);
    }

    @Override
    public Registration assignRegistrationToCourse(Long numRegistration, Long numCourse) {
        // Récupération de l'enregistrement et du cours par leurs ID, ou lever une exception si non trouvés
        Registration registration = registrationRepository.findById(numRegistration)
                .orElseThrow(() -> new IllegalArgumentException("Registration not found"));
        Course course = courseRepository.findById(numCourse)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        // Associer l'enregistrement au cours
        registration.setCourse(course);
        return registrationRepository.save(registration);
    }

    @Transactional
    @Override
    public Registration addRegistrationAndAssignToSkierAndCourse(Registration registration, Long numSkieur, Long numCours) {
        // Récupération du skieur et du cours par leurs ID, ou lever une exception si non trouvés
        Skier skier = skierRepository.findById(numSkieur)
                .orElseThrow(() -> new IllegalArgumentException("Skier not found"));
        Course course = courseRepository.findById(numCours)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        // Vérifier si l'utilisateur est déjà inscrit à ce cours pour cette semaine
        if (registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(
                registration.getNumWeek(), skier.getNumSkier(), course.getNumCourse()) >= 1) {
            log.info("Sorry, you're already registered to this course for the week: " + registration.getNumWeek());
            return null;
        }

        // Calculer l'âge du skieur
        int ageSkieur = Period.between(skier.getDateOfBirth(), LocalDate.now()).getYears();
        log.info("Age: " + ageSkieur);

        // Déterminer le type de cours et vérifier les conditions d'inscription
        switch (course.getTypeCourse()) {
            case INDIVIDUAL:
                log.info("Adding without further checks");
                return assignRegistration(registration, skier, course);
            case COLLECTIVE_CHILDREN:
                if (ageSkieur < 16) {
                    log.info("Ok CHILD!");
                    if (!isCourseFull(course, registration.getNumWeek())) {
                        log.info("Course successfully added!");
                        return assignRegistration(registration, skier, course);
                    } else {
                        log.info("Full Course! Please choose another week to register!");
                        return null;
                    }
                } else {
                    log.info("Sorry, your age doesn't allow you to register for this course! Try to Register to a Collective Adult Course...");
                }
                break;
            default:
                if (ageSkieur >= 16) {
                    log.info("Ok ADULT!");
                    if (!isCourseFull(course, registration.getNumWeek())) {
                        log.info("Course successfully added!");
                        return assignRegistration(registration, skier, course);
                    } else {
                        log.info("Full Course! Please choose another week to register!");
                        return null;
                    }
                }
                log.info("Sorry, your age doesn't allow you to register for this course! Try to Register to a Collective Child Course...");
        }
        return registration;
    }

    // Méthode utilitaire pour vérifier si le cours est plein pour une semaine donnée
    private boolean isCourseFull(Course course, int numWeek) {
        return registrationRepository.countByCourseAndNumWeek(course, numWeek) >= 6;
    }

    // Méthode utilitaire pour associer un enregistrement à un skieur et un cours
    private Registration assignRegistration(Registration registration, Skier skier, Course course) {
        registration.setSkier(skier);
        registration.setCourse(course);
        return registrationRepository.save(registration);
    }

    @Override
    public List<Integer> numWeeksCourseOfInstructorBySupport(Long numInstructor, Support support) {
        return registrationRepository.numWeeksCourseOfInstructorBySupport(numInstructor, support);
    }
}
