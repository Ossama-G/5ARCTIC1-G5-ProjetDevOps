package tn.esprit.spring.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.services.ICourseServices;
import tn.esprit.spring.entities.TypeCourse;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CourseRestControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ICourseServices courseServices;

    @InjectMocks
    private CourseRestController courseRestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseRestController).build();
    }

    @Test
    public void testAddCourse() throws Exception {
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);
        course.setPrice(100.0f);
        course.setTimeSlot(2);

        when(courseServices.addCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/course/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"level\":1, \"typeCourse\":\"COLLECTIVE_CHILDREN\", \"price\":100.0, \"timeSlot\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numCourse").value(1L))
                .andExpect(jsonPath("$.level").value(1))
                .andExpect(jsonPath("$.typeCourse").value("COLLECTIVE_CHILDREN"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.timeSlot").value(2));

        verify(courseServices, times(1)).addCourse(any(Course.class));
    }

    @Test
    public void testGetAllCourses() throws Exception {
        Course course1 = new Course();
        course1.setNumCourse(1L);
        course1.setLevel(1);
        course1.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);
        course1.setPrice(100.0f);
        course1.setTimeSlot(2);

        Course course2 = new Course();
        course2.setNumCourse(2L);
        course2.setLevel(2);
        course2.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course2.setPrice(150.0f);
        course2.setTimeSlot(3);

        List<Course> courses = Arrays.asList(course1, course2);

        when(courseServices.retrieveAllCourses()).thenReturn(courses);

        mockMvc.perform(get("/course/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numCourse").value(1L))
                .andExpect(jsonPath("$[0].level").value(1))
                .andExpect(jsonPath("$[0].typeCourse").value("COLLECTIVE_CHILDREN"))
                .andExpect(jsonPath("$[0].price").value(100.0))
                .andExpect(jsonPath("$[0].timeSlot").value(2))
                .andExpect(jsonPath("$[1].numCourse").value(2L))
                .andExpect(jsonPath("$[1].level").value(2))
                .andExpect(jsonPath("$[1].typeCourse").value("COLLECTIVE_ADULT"))
                .andExpect(jsonPath("$[1].price").value(150.0))
                .andExpect(jsonPath("$[1].timeSlot").value(3));

        verify(courseServices, times(1)).retrieveAllCourses();
    }

    @Test
    public void testUpdateCourse() throws Exception {
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);
        course.setPrice(100.0f);
        course.setTimeSlot(2);

        when(courseServices.updateCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(put("/course/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numCourse\":1, \"level\":1, \"typeCourse\":\"COLLECTIVE_CHILDREN\", \"price\":100.0, \"timeSlot\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numCourse").value(1L))
                .andExpect(jsonPath("$.level").value(1))
                .andExpect(jsonPath("$.typeCourse").value("COLLECTIVE_CHILDREN"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.timeSlot").value(2));

        verify(courseServices, times(1)).updateCourse(any(Course.class));
    }

    @Test
    public void testGetCourseById() throws Exception {
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);
        course.setPrice(100.0f);
        course.setTimeSlot(2);

        when(courseServices.retrieveCourse(1L)).thenReturn(course);

        mockMvc.perform(get("/course/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numCourse").value(1L))
                .andExpect(jsonPath("$.level").value(1))
                .andExpect(jsonPath("$.typeCourse").value("COLLECTIVE_CHILDREN"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.timeSlot").value(2));

        verify(courseServices, times(1)).retrieveCourse(1L);
    }
}