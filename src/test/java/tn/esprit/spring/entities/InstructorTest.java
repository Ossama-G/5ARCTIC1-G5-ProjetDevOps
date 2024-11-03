package tn.esprit.spring.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

class InstructorTest {

    @Test
    void testInstructorGettersAndSetters() {
        Instructor instructor = new Instructor();

        // Test numInstructor
        instructor.setNumInstructor(1L);
        assertEquals(1L, instructor.getNumInstructor());

        // Test firstName
        instructor.setFirstName("John");
        assertEquals("John", instructor.getFirstName());

        // Test lastName
        instructor.setLastName("Doe");
        assertEquals("Doe", instructor.getLastName());

        // Test dateOfHire
        LocalDate dateOfHire = LocalDate.now();
        instructor.setDateOfHire(dateOfHire);
        assertEquals(dateOfHire, instructor.getDateOfHire());

        // Test courses
        Set<Course> courses = new HashSet<>();
        instructor.setCourses(courses);
        assertEquals(courses, instructor.getCourses());
    }

    @Test
    void testAddCourse() {
        Instructor instructor = new Instructor();
        Course course = new Course();

        instructor.addCourse(course);

        assertNotNull(instructor.getCourses());
        assertTrue(instructor.getCourses().contains(course));
        assertEquals(instructor, course.getInstructor());
    }

    @Test
    void testRemoveCourse() {
        Instructor instructor = new Instructor();
        Course course = new Course();

        instructor.addCourse(course);
        instructor.removeCourse(course);

        assertFalse(instructor.getCourses().contains(course));
        assertNull(course.getInstructor());
    }
}