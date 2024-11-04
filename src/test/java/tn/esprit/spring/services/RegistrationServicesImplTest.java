package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.services.RegistrationServicesImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class RegistrationServicesImplTest {

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddRegistrationAndAssignToSkier() {
        Skier skier = new Skier();
        skier.setNumSkier(1L);
        Registration registration = new Registration();
        when(skierRepository.findById(anyLong())).thenReturn(Optional.of(skier));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.addRegistrationAndAssignToSkier(registration, 1L);

        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        verify(skierRepository, times(1)).findById(anyLong());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void testAddRegistrationAndAssignToSkier_SkierNotFound() {
        when(skierRepository.findById(anyLong())).thenReturn(Optional.empty());

        Registration result = registrationServices.addRegistrationAndAssignToSkier(new Registration(), 1L);

        assertNull(result);
        verify(skierRepository, times(1)).findById(anyLong());
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    void testAssignRegistrationToCourse() {
        Registration registration = new Registration();
        Course course = new Course();
        when(registrationRepository.findById(anyLong())).thenReturn(Optional.of(registration));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.assignRegistrationToCourse(1L, 1L);

        assertNotNull(result);
        assertEquals(course, result.getCourse());
        verify(registrationRepository, times(1)).findById(anyLong());
        verify(courseRepository, times(1)).findById(anyLong());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void testAssignRegistrationToCourse_RegistrationNotFound() {
        when(registrationRepository.findById(anyLong())).thenReturn(Optional.empty());

        Registration result = registrationServices.assignRegistrationToCourse(1L, 1L);

        assertNull(result);
        verify(registrationRepository, times(1)).findById(anyLong());
        verify(courseRepository, never()).findById(anyLong());
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    void testAssignRegistrationToCourse_CourseNotFound() {
        Registration registration = new Registration();
        when(registrationRepository.findById(anyLong())).thenReturn(Optional.of(registration));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        Registration result = registrationServices.assignRegistrationToCourse(1L, 1L);

        assertNull(result);
        verify(registrationRepository, times(1)).findById(anyLong());
        verify(courseRepository, times(1)).findById(anyLong());
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse() {
        Skier skier = new Skier();
        skier.setNumSkier(1L);
        skier.setDateOfBirth(LocalDate.of(2000, 1, 1));
        Course course = new Course();
        course.setNumCourse(1L);
        course.setTypeCourse(TypeCourse.INDIVIDUAL);
        Registration registration = new Registration();
        registration.setNumWeek(1);
        when(skierRepository.findById(anyLong())).thenReturn(Optional.of(skier));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        assertEquals(course, result.getCourse());
        verify(skierRepository, times(1)).findById(anyLong());
        verify(courseRepository, times(1)).findById(anyLong());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_SkierNotFound() {
        when(skierRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(new Course()));

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(new Registration(), 1L, 1L);

        assertNull(result);
        verify(skierRepository, times(1)).findById(anyLong());
        verify(courseRepository, times(1)).findById(anyLong());
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_CourseNotFound() {
        when(skierRepository.findById(anyLong())).thenReturn(Optional.of(new Skier()));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(new Registration(), 1L, 1L);

        assertNull(result);
        verify(skierRepository, times(1)).findById(anyLong());
        verify(courseRepository, times(1)).findById(anyLong());
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_AlreadyRegistered() {
        Skier skier = new Skier();
        skier.setNumSkier(1L);
        Course course = new Course();
        course.setNumCourse(1L);
        Registration registration = new Registration();
        registration.setNumWeek(1);
        when(skierRepository.findById(anyLong())).thenReturn(Optional.of(skier));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong())).thenReturn(1L);

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        assertNull(result);
        verify(skierRepository, times(1)).findById(anyLong());
        verify(courseRepository, times(1)).findById(anyLong());
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    void testHandleCourseRegistration_CourseTypeNull() {
        Registration registration = new Registration();
        Skier skier = new Skier();
        Course course = new Course();
        course.setTypeCourse(null);

        Registration result = registrationServices.handleCourseRegistration(registration, skier, course, 20);

        assertNull(result);
    }

    @Test
    void testHandleChildrenCourseRegistration_Child() {
        Skier skier = new Skier();
        skier.setDateOfBirth(LocalDate.of(2010, 1, 1)); // Age < 16

        Course course = new Course();
        course.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);

        Registration registration = new Registration();
        registration.setNumWeek(1);

        when(skierRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(skier));
        when(courseRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(course));
        when(registrationRepository.countByCourseAndNumWeek(any(Course.class), any(Integer.class))).thenReturn(0L);
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.handleChildrenCourseRegistration(registration, skier, course, 10);

        assertNotNull(result);
    }

    @Test
    void testHandleChildrenCourseRegistration_Child_FullCourse() {
        Registration registration = new Registration();
        registration.setNumWeek(1);
        Skier skier = new Skier();
        Course course = new Course();
        course.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);
        when(registrationRepository.countByCourseAndNumWeek(any(Course.class), anyInt())).thenReturn(6L);
        Registration result = registrationServices.handleChildrenCourseRegistration(registration, skier, course, 10);

        assertNull(result);
    }

    @Test
    void testHandleChildrenCourseRegistration_Adult() {
        Registration registration = new Registration();
        registration.setNumWeek(1);
        Skier skier = new Skier();
        Course course = new Course();
        course.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);
        Registration result = registrationServices.handleChildrenCourseRegistration(registration, skier, course, 20);

        assertNull(result);
    }

    @Test
    void testHandleAdultCourseRegistration_Adult() {
        Registration registration = new Registration();
        registration.setNumWeek(1);
        Skier skier = new Skier();
        Course course = new Course();
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);

        // Mock the repository to return the expected values
        when(registrationRepository.countByCourseAndNumWeek(any(Course.class), anyInt())).thenReturn(5L);
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // Call the method under test
        Registration result = registrationServices.handleAdultCourseRegistration(registration, skier, course, 20);

        // Assert that the result is not null
        assertNotNull(result);
        assertEquals(registration, result);
    }

    @Test
    void testHandleAdultCourseRegistration_Adult_FullCourse() {
        Registration registration = new Registration();
        registration.setNumWeek(1);
        Skier skier = new Skier();
        Course course = new Course();
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        when(registrationRepository.countByCourseAndNumWeek(any(Course.class), anyInt())).thenReturn(6L);
        Registration result = registrationServices.handleAdultCourseRegistration(registration, skier, course, 20);

        assertNull(result);
    }

    @Test
    void testHandleAdultCourseRegistration_Child() {
        Registration registration = new Registration();
        registration.setNumWeek(1);
        Skier skier = new Skier();
        Course course = new Course();
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        Registration result = registrationServices.handleAdultCourseRegistration(registration, skier, course, 10);

        assertNull(result);
    }

    @Test
    void testNumWeeksCourseOfInstructorBySupport() {
        List<Integer> weeks = Arrays.asList(1, 2, 3);
        when(registrationRepository.numWeeksCourseOfInstructorBySupport(anyLong(), any(Support.class))).thenReturn(weeks);

        List<Integer> result = registrationServices.numWeeksCourseOfInstructorBySupport(1L, Support.SKI);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(weeks, result);
        verify(registrationRepository, times(1)).numWeeksCourseOfInstructorBySupport(anyLong(), any(Support.class));
    }
}