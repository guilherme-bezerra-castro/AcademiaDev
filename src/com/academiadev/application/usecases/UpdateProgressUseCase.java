package com.academiadev.application.usecases;

import com.academiadev.application.repositories.CourseRepository;
import com.academiadev.application.repositories.EnrollmentRepository;
import com.academiadev.domain.entities.Course;
import com.academiadev.domain.entities.Enrollment;
import com.academiadev.domain.entities.Student;
import com.academiadev.domain.exceptions.BusinessException;
import com.academiadev.domain.exceptions.EnrollmentException;

public class UpdateProgressUseCase {
	private final CourseRepository     courseRepository;
    private final EnrollmentRepository enrollmentRepository;
 
    public UpdateProgressUseCase(CourseRepository courseRepository,
                                 EnrollmentRepository enrollmentRepository) {
        this.courseRepository     = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }
 
    public Enrollment execute(Student student, String courseTitle, int newProgress) {
        Course course = courseRepository.findByTitle(courseTitle)
                .orElseThrow(() -> new BusinessException(
                        "Curso não encontrado: " + courseTitle));
 
        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new EnrollmentException(
                        "Você não está matriculado em \"" + course.getTitle() + "\"."));
 
        enrollment.updateProgress(newProgress);
        return enrollment;
    }
}
