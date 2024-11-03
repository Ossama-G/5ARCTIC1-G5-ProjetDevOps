package tn.esprit.spring.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CourseDTOTest {

    @Test
    void testCourseDTOGettersAndSetters() {
        CourseDTO courseDTO = new CourseDTO();

        courseDTO.setNumCourse(1L);
        assertEquals(1L, courseDTO.getNumCourse());

        courseDTO.setLevel(2);
        assertEquals(2, courseDTO.getLevel());

        courseDTO.setTypeCourse("Ski");
        assertEquals("Ski", courseDTO.getTypeCourse());

        courseDTO.setSupport("Online");
        assertEquals("Online", courseDTO.getSupport());

        courseDTO.setPrice(99.99f);
        assertEquals(99.99f, courseDTO.getPrice());

        courseDTO.setTimeSlot(3);
        assertEquals(3, courseDTO.getTimeSlot());
    }
}