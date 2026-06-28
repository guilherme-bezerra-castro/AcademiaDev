package com.academiadev.infrastructure.persistence;

import com.academiadev.application.repositories.EnrollmentRepository;
import com.academiadev.domain.entities.Course;
import com.academiadev.domain.entities.Enrollment;
import com.academiadev.domain.entities.Student;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EnrollmentRepositoryInMemory implements EnrollmentRepository {
	
	private final List<Enrollment> storage = new ArrayList<>();
	
	@Override
    public void save(Enrollment enrollment) {
        storage.add(enrollment);
    }
 
    @Override
    public void delete(Enrollment enrollment) {
        storage.remove(enrollment);
    }
 
    @Override
    public List<Enrollment> findByStudent(Student student) {
        return storage.stream().filter(e -> e.getStudent().getId().equals(student.getId())).collect(Collectors.toList());
    }
 
    @Override
    public List<Enrollment> findAll() {
        return new ArrayList<>(storage);
    }
 
    @Override
    public int countActiveByStudent(Student student) {
        return (int) storage.stream()
                .filter(e -> e.getStudent().getId().equals(student.getId())).count();
    }
 
    @Override
    public Optional<Enrollment> findByStudentAndCourse(Student student, Course course) {
        return storage.stream()
                .filter(e -> e.getStudent().getId().equals(student.getId()) && e.getCourse().getId().equals(course.getId())).findFirst();
    }

}
