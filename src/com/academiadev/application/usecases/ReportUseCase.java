package com.academiadev.application.usecases;

import com.academiadev.application.repositories.CourseRepository;
import com.academiadev.application.repositories.EnrollmentRepository;
import com.academiadev.application.repositories.UserRepository;
import com.academiadev.domain.entities.Course;
import com.academiadev.domain.entities.Enrollment;
import com.academiadev.domain.entities.Student;
import com.academiadev.domain.enums.CourseStatus;
import com.academiadev.domain.enums.DifficultyLevel;

import java.util.*;
import java.util.stream.Collectors;

public class ReportUseCase {
	 private final CourseRepository     courseRepository;
	    private final EnrollmentRepository enrollmentRepository;
	    private final UserRepository       userRepository;
	 
	    public ReportUseCase(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository, UserRepository userRepository) {
	        this.courseRepository     = courseRepository;
	        this.enrollmentRepository = enrollmentRepository;
	        this.userRepository       = userRepository;
	    }
	 
	    public List<Course> coursesByLevel(DifficultyLevel level) {
	        return courseRepository.findAll().stream()
	                .filter(c -> c.getDifficultyLevel() == level)
	                .sorted(Comparator.comparing(Course::getTitle))
	                .collect(Collectors.toList());
	    }
	 
	    public Set<String> activeInstructors() {
	        return courseRepository.findAll().stream()
	                .filter(c -> c.getStatus() == CourseStatus.ACTIVE)
	                .map(Course::getInstructorName)
	                .collect(Collectors.toSet());
	    }
	 
	    public Map<String, List<Student>> studentsByPlan() {
	        return userRepository.findAllStudents().stream()
	                .collect(Collectors.groupingBy(Student::getSubscriptionPlan));
	    }
	    
	    public double averageProgress() {
	        OptionalDouble avg = enrollmentRepository.findAll().stream()
	                .mapToInt(Enrollment::getProgress)
	                .average();
	        return avg.orElse(0.0);
	    }

	    public Optional<Student> studentWithMostEnrollments() {
	        return userRepository.findAllStudents().stream()
	                .max(Comparator.comparingInt(
	                        s -> enrollmentRepository.countActiveByStudent(s)));
	    }
	 
	    public List<Course> allCourses() {
	        return courseRepository.findAll();
	    }
	 
	    public List<Student> allStudents() {
	        return userRepository.findAllStudents();
	    }
	 
	    public List<Enrollment> allEnrollments() {
	        return enrollmentRepository.findAll();
	    }
}
