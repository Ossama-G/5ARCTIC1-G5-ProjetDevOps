package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.services.CourseServicesImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class GestionStationSkiApplicationTests {

	@InjectMocks
	private CourseServicesImpl courseServices;

	@Mock
	private ICourseRepository courseRepository;

	private Course course;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		course = new Course();
		course.setNumCourse(1L);
		course.setLevel(2);
		course.setPrice(100.0F);
		course.setTimeSlot(3);
	}

	@Test
	void contextLoads() {
		// Ce test vérifie simplement si le contexte Spring Boot se charge correctement
	}

	@Test
	void testAddCourse() {
		// Simuler le comportement du repository pour l'ajout d'un Course
		when(courseRepository.save(any(Course.class))).thenReturn(course);

		// Ajouter le course
		Course savedCourse = courseServices.addCourse(course);

		// Vérifications
		assertNotNull(savedCourse);
		assertEquals(1L, savedCourse.getNumCourse());
		verify(courseRepository, times(1)).save(course);
	}

	@Test
	void testRetrieveCourse() {
		// Simuler le comportement du repository pour la récupération d'un Course par ID
		when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

		// Récupérer le course
		Course retrievedCourse = courseServices.retrieveCourse(1L);

		// Vérifications
		assertNotNull(retrievedCourse);
		assertEquals(1L, retrievedCourse.getNumCourse());
		verify(courseRepository, times(1)).findById(1L);
	}
}
