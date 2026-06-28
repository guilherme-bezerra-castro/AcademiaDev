package com.academiadev.application.usecases;

import com.academiadev.application.repositories.CourseRepository;
import com.academiadev.domain.entities.Course;
import com.academiadev.domain.exceptions.BusinessException;

public class ChangeCourseStatusUseCase {
	private final CourseRepository courseRepository;
	 
    public ChangeCourseStatusUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
 
    public Course activate(String courseTitle) {
        Course course = courseRepository.findByTitle(courseTitle)
                .orElseThrow(() -> new BusinessException(
                        "Curso não encontrado: " + courseTitle));
        course.activate();
        return course;
    }
 
    public Course deactivate(String courseTitle) {
        Course course = courseRepository.findByTitle(courseTitle)
                .orElseThrow(() -> new BusinessException(
                        "Curso não encontrado: " + courseTitle));
        course.deactivate();
        return course;
    }
}
