package com.academiadev.application.repositories;

import com.academiadev.domain.entities.Student;
import com.academiadev.domain.entities.User;
 
import java.util.List;
import java.util.Optional;

public interface UserRepository {
	void save(User user);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    List<Student> findAllStudents();
}
