package tn.esprit.spring.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Instructor implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long numInstructor;
	String firstName;
	String lastName;
	LocalDate dateOfHire;

	@OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Course> courses;

	public void addCourse(Course course) {
		if (this.courses == null) {
			this.courses = new HashSet<>();
		}
		this.courses.add(course);
		course.setInstructor(this); // Mise à jour du côté Course de la relation
	}

	public void removeCourse(Course course) {
		if (this.courses != null) {
			this.courses.remove(course);
			course.setInstructor(null); // Supprimer la relation côté Course
		}
	}
}
