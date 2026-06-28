package com.academiadev.application.usecases;

import com.academiadev.application.repositories.CourseRepository;
import com.academiadev.application.repositories.EnrollmentRepository;
import com.academiadev.domain.entities.Course;
import com.academiadev.domain.entities.Enrollment;
import com.academiadev.domain.entities.Student;
import com.academiadev.domain.exceptions.BusinessException;
import com.academiadev.domain.exceptions.EnrollmentException;

import java.util.UUID;

public class EnrollStudentUseCase {
	private final CourseRepository     courseRepository;
    private final EnrollmentRepository enrollmentRepository;
 
    public EnrollStudentUseCase(CourseRepository courseRepository,
                                EnrollmentRepository enrollmentRepository) {
        this.courseRepository     = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }
 
    public Enrollment execute(Student student, String courseTitle) {
        Course course = courseRepository.findByTitle(courseTitle)
                .orElseThrow(() -> new BusinessException(
                        "Curso não encontrado: " + courseTitle));
 
        if (!course.isActive()) {
            throw new EnrollmentException(
                    "O curso \"" + course.getTitle() + "\" está INATIVO e não aceita matrículas.");
        }
 
        enrollmentRepository.findByStudentAndCourse(student, course).ifPresent(e -> {
            throw new EnrollmentException("Você já está matriculado em \"" + course.getTitle() + "\".");
        });
 
        int active = enrollmentRepository.countActiveByStudent(student);
        if (!student.canEnroll(active)) {
            throw new EnrollmentException(
                    "Limite de matrículas atingido para o plano " +
                    student.getSubscriptionPlan() + ". Atualize para o plano PREMIUM.");
        }
 
        Enrollment enrollment = new Enrollment(UUID.randomUUID().toString(), student, course);
        enrollmentRepository.save(enrollment);
        return enrollment;
    }
}
