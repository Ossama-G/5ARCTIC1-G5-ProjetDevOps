package tn.esprit.spring.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;

import java.time.LocalDate;
import java.util.*;

class InstructorServicesImplTest {

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddInstructor() {
        Instructor instructor = new Instructor(null, "John", "Doe", LocalDate.now(), null);
        when(instructorRepository.save(instructor)).thenReturn(instructor);

        Instructor result = instructorServices.addInstructor(instructor);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testRetrieveInstructor() {
        Long instructorId = 1L;
        Instructor instructor = new Instructor(instructorId, "Jane", "Doe", LocalDate.now(), null);
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        Instructor result = instructorServices.retrieveInstructor(instructorId);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        verify(instructorRepository, times(1)).findById(instructorId);
    }

    @Test
    void testRetrieveInstructorNotFound() {
        Long instructorId = 99L;
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        Instructor result = instructorServices.retrieveInstructor(instructorId);

        assertNull(result);
        verify(instructorRepository, times(1)).findById(instructorId);
    }

    @Test
    void testRetrieveAllInstructors() {
        List<Instructor> instructors = Arrays.asList(
                new Instructor(1L, "John", "Doe", LocalDate.now(), null),
                new Instructor(2L, "Jane", "Smith", LocalDate.now(), null)
        );
        when(instructorRepository.findAll()).thenReturn(instructors);

        List<Instructor> result = instructorServices.retrieveAllInstructors();

        assertEquals(2, result.size());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveAllInstructorsEmpty() {
        when(instructorRepository.findAll()).thenReturn(Collections.emptyList());

        List<Instructor> result = instructorServices.retrieveAllInstructors();

        assertTrue(result.isEmpty());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void testUpdateInstructor() {
        Instructor instructor = new Instructor(1L, "John", "Smith", LocalDate.now(), null);
        when(instructorRepository.save(instructor)).thenReturn(instructor);

        Instructor result = instructorServices.updateInstructor(instructor);

        assertEquals("John", result.getFirstName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    /*@Test
    void testAddInstructorAndAssignToCourse() {
        Long courseId = 99L;
        Instructor instructor = new Instructor();
        instructor.setFirstName("John");

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        Instructor result = instructorServices.addInstructorAndAssignToCourse(instructor, courseId);

        assertNull(result); // Verifies that the method returns null when the course doesn't exist
        verify(courseRepository, times(1)).findById(courseId);
        verify(instructorRepository, never()).save(instructor);
    }*/

    /*@Test
    void testAddInstructorAndAssignToNonExistingCourse() {
        Long courseId = 99L;
        Instructor instructor = new Instructor();
        instructor.setFirstName("John");

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        Instructor result = instructorServices.addInstructorAndAssignToCourse(instructor, courseId);

        assertNull(result); // Verifies that the method returns null when the course doesn't exist
        verify(courseRepository, times(1)).findById(courseId);
        verify(instructorRepository, never()).save(instructor); // Ensure save is never called
    }*/
}