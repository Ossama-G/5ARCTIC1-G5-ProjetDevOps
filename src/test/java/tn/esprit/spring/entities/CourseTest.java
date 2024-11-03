package tn.esprit.spring.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

class CourseTest {

    @Test
    void testCourseGettersAndSetters() {
        Course course = new Course();

        // Test numCourse
        course.setNumCourse(1L);
        assertEquals(1L, course.getNumCourse());

        // Test level
        course.setLevel(2);
        assertEquals(2, course.getLevel());

        // Test typeCourse
        TypeCourse typeCourse = TypeCourse.COLLECTIVE_CHILDREN; // Use actual enum value
        course.setTypeCourse(typeCourse);
        assertEquals(typeCourse, course.getTypeCourse());

        // Test support
        Support support = Support.SKI; // Use actual enum value
        course.setSupport(support);
        assertEquals(support, course.getSupport());

        // Test price
        course.setPrice(100.0f);
        assertEquals(100.0f, course.getPrice());

        // Test timeSlot
        course.setTimeSlot(3);
        assertEquals(3, course.getTimeSlot());

        // Test registrations
        Set<Registration> registrations = new HashSet<>();
        course.setRegistrations(registrations);
        assertEquals(registrations, course.getRegistrations());

        // Test instructor
        Instructor instructor = new Instructor();
        course.setInstructor(instructor);
        assertEquals(instructor, course.getInstructor());
    }
}