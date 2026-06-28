package com.academiadev.application.repositories;

import com.academiadev.domain.entities.Course;
import com.academiadev.domain.enums.DifficultyLevel;
 
import java.util.List;
import java.util.Optional;

public interface CourseRepository {
	void save(Course course);
    Optional<Course> findByTitle(String title);
    List<Course> findAll();
    List<Course> findByDifficultyLevel(DifficultyLevel level);
}
