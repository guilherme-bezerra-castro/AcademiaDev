package com.academiadev.application.usecases;

import com.academiadev.application.repositories.CourseRepository;
import com.academiadev.application.repositories.EnrollmentRepository;
import com.academiadev.domain.entities.Course;
import com.academiadev.domain.entities.Student;
import com.academiadev.domain.exceptions.BusinessException;
import com.academiadev.domain.exceptions.EnrollmentException;

public class CancelEnrollmentUseCase {
	private final CourseRepository     courseRepository;
    private final EnrollmentRepository enrollmentRepository;
 
    public CancelEnrollmentUseCase(CourseRepository courseRepository,
                                   EnrollmentRepository enrollmentRepository) {
        this.courseRepository     = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }
 
    public void execute(Student student, String courseTitle) {
        Course course = courseRepository.findByTitle(courseTitle)
                .orElseThrow(() -> new BusinessException(
                        "Curso não encontrado: " + courseTitle));
 
        var enrollment = enrollmentRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new EnrollmentException(
                        "Você não está matriculado em \"" + course.getTitle() + "\"."));
 
        enrollmentRepository.delete(enrollment);
    }
}
