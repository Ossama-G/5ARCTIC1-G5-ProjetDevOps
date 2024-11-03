package tn.esprit.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.controllers.CourseRestController;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.services.ICourseServices;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CourseRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ICourseServices courseServices;

    @InjectMocks
    private CourseRestController courseRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseRestController).build();
    }

    @Test
    void testAddCourse() throws Exception {
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(2);

        when(courseServices.addCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/course/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"level\":2, \"price\":100.0, \"timeSlot\":4}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numCourse", is(1)))
                .andExpect(jsonPath("$.level", is(2)));

        verify(courseServices, times(1)).addCourse(any(Course.class));
    }

    @Test
    void testGetById() throws Exception {
        Long courseId = 1L;
        Course course = new Course();
        course.setNumCourse(courseId);

        when(courseServices.retrieveCourse(courseId)).thenReturn(course);

        mockMvc.perform(get("/course/get/{id-course}", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numCourse", is(1)));

        verify(courseServices, times(1)).retrieveCourse(courseId);
    }

    @Test
    void testGetAllCourses() throws Exception {
        List<Course> courses = Arrays.asList(new Course(), new Course());
        when(courseServices.retrieveAllCourses()).thenReturn(courses);

        mockMvc.perform(get("/course/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        verify(courseServices, times(1)).retrieveAllCourses();
    }

    @Test
    void testUpdateCourse() throws Exception {
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(3);

        when(courseServices.updateCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(put("/course/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numCourse\":1, \"level\":3, \"price\":150.0, \"timeSlot\":5}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numCourse", is(1)))
                .andExpect(jsonPath("$.level", is(3)));

        verify(courseServices, times(1)).updateCourse(any(Course.class));
    }
}