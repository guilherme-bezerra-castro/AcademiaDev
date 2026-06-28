package com.academiadev.application.repositories;

import com.academiadev.domain.entities.Course;
import com.academiadev.domain.entities.Enrollment;
import com.academiadev.domain.entities.Student;
 
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository {
	void save(Enrollment enrollment);
    void delete(Enrollment enrollment);
    List<Enrollment> findByStudent(Student student);
    List<Enrollment> findAll();
    int countActiveByStudent(Student student);
    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);
}
