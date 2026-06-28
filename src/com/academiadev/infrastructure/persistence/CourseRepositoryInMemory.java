package com.academiadev.infrastructure.persistence;

import com.academiadev.application.repositories.CourseRepository;
import com.academiadev.domain.entities.Course;
import com.academiadev.domain.enums.DifficultyLevel;
import com.academiadev.domain.exceptions.BusinessException;
 
import java.util.*;
import java.util.stream.Collectors;

public class CourseRepositoryInMemory implements CourseRepository {
	private final Map<String, Course> storage = new LinkedHashMap<>();
	
	@Override
    public void save(Course course) {
        if (storage.containsKey(course.getTitle()) &&
            !storage.get(course.getTitle()).getId().equals(course.getId())) {
            throw new BusinessException(
                    "Já existe um curso com o título \"" + course.getTitle() + "\".");
        }
        storage.put(course.getTitle(), course);
    }
 
    @Override
    public Optional<Course> findByTitle(String title) {
        return Optional.ofNullable(storage.get(title));
    }
 
    @Override
    public List<Course> findAll() {
        return new ArrayList<>(storage.values());
    }
 
    @Override
    public List<Course> findByDifficultyLevel(DifficultyLevel level) {
        return storage.values().stream()
                .filter(c -> c.getDifficultyLevel() == level)
                .collect(Collectors.toList());
    }
}
