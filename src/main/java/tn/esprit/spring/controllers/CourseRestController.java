package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.DTO.CourseDTO;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.services.ICourseServices;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "\uD83C\uDFC2 Course Management")
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseRestController {

    private final ICourseServices courseServices;

    @Operation(description = "Add Course")
    @PostMapping("/add")
    public CourseDTO addCourse(@RequestBody CourseDTO courseDTO) {
        Course course = convertToEntity(courseDTO);
        Course savedCourse = courseServices.addCourse(course);
        return convertToDTO(savedCourse);
    }

    @Operation(description = "Retrieve Course by Id")
    @GetMapping("/get/{id-course}")
    public CourseDTO getById(@PathVariable("id-course") Long numCourse) {
        Course course = courseServices.retrieveCourse(numCourse);
        return convertToDTO(course);
    }

    @Operation(description = "Retrieve all Courses")
    @GetMapping("/all")
    public List<CourseDTO> getAllCourses() {
        return courseServices.retrieveAllCourses().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Operation(description = "Update Course")
    @PutMapping("/update")
    public CourseDTO updateCourse(@RequestBody CourseDTO courseDTO) {
        Course course = convertToEntity(courseDTO);
        Course updatedCourse = courseServices.updateCourse(course);
        return convertToDTO(updatedCourse);
    }

    public CourseDTO convertToDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setNumCourse(course.getNumCourse());
        courseDTO.setLevel(course.getLevel());
        courseDTO.setPrice(course.getPrice());
        courseDTO.setTimeSlot(course.getTimeSlot());
        courseDTO.setTypeCourse(course.getTypeCourse() != null ? course.getTypeCourse().name() : null);
        return courseDTO;
    }

    public Course convertToEntity(CourseDTO courseDTO) {
        Course course = new Course();
        course.setNumCourse(courseDTO.getNumCourse());
        course.setLevel(courseDTO.getLevel());
        course.setPrice(courseDTO.getPrice());
        course.setTimeSlot(courseDTO.getTimeSlot());
        if (courseDTO.getTypeCourse() != null) {
            course.setTypeCourse(TypeCourse.valueOf(courseDTO.getTypeCourse()));
        }
        return course;
    }
}