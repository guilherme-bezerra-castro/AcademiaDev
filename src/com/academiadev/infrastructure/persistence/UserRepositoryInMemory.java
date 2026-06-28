package com.academiadev.infrastructure.persistence;

import com.academiadev.application.repositories.UserRepository;
import com.academiadev.domain.entities.Student;
import com.academiadev.domain.entities.User;
import com.academiadev.domain.exceptions.BusinessException;
 
import java.util.*;
import java.util.stream.Collectors;
import java.util.Optional;

public class UserRepositoryInMemory implements UserRepository {
	private final Map<String, User> storage = new LinkedHashMap<>();
	
	@Override
	public void save(User user) {
		if (storage.containsKey(user.getEmail()) &&
	            !storage.get(user.getEmail()).getId().equals(user.getId())) {
	            throw new BusinessException(
	                    "Já existe um usuário com o e-mail \"" + user.getEmail() + "\".");
	        }
	        storage.put(user.getEmail(), user);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return Optional.ofNullable(storage.get(email));
	}

	@Override
	public List<User> findAll() {
		return new ArrayList<>(storage.values());
	}

	@Override
	public List<Student> findAllStudents() {
		// TODO Auto-generated method stub
		return storage.values().stream().filter(u -> u instanceof Student).map(u -> (Student) u).collect(Collectors.toList());
	}

}
