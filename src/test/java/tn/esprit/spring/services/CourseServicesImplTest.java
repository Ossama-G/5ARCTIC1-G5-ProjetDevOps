package tn.esprit.spring.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repositories.ICourseRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServicesImplTest {

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private CourseServicesImpl courseServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCourse() {
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);

        when(courseRepository.save(course)).thenReturn(course);

        Course result = courseServices.addCourse(course);

        assertNotNull(result);
        assertEquals(1L, result.getNumCourse());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testRetrieveAllCourses() {
        List<Course> courses = Arrays.asList(new Course(), new Course());
        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> result = courseServices.retrieveAllCourses();

        assertEquals(2, result.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCourse() {
        Course course = new Course();
        course.setNumCourse(1L);

        when(courseRepository.save(course)).thenReturn(course);

        Course result = courseServices.updateCourse(course);

        assertNotNull(result);
        assertEquals(1L, result.getNumCourse());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testRetrieveCourse() {
        Long courseId = 1L;
        Course course = new Course();
        course.setNumCourse(courseId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        Course result = courseServices.retrieveCourse(courseId);

        assertNotNull(result);
        assertEquals(courseId, result.getNumCourse());
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void testRetrieveCourseNotFound() {
        Long courseId = 99L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        Course result = courseServices.retrieveCourse(courseId);

        assertNull(result);
        verify(courseRepository, times(1)).findById(courseId);
    }
}