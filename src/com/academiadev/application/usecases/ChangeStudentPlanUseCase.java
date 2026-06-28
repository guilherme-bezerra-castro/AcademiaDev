package com.academiadev.application.usecases;

import com.academiadev.application.repositories.UserRepository;
import com.academiadev.domain.entities.Student;
import com.academiadev.domain.entities.User;
import com.academiadev.domain.exceptions.BusinessException;

public class ChangeStudentPlanUseCase {
	private final UserRepository userRepository;
	 
    public ChangeStudentPlanUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
 
    public Student execute(String studentEmail, String newPlan) {
        if (!newPlan.equalsIgnoreCase("BASIC") && !newPlan.equalsIgnoreCase("PREMIUM")) {
            throw new BusinessException("Plano inválido. Use BASIC ou PREMIUM.");
        }
 
        User user = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new BusinessException(
                        "Usuário não encontrado: " + studentEmail));
 
        if (!(user instanceof Student)) {
            throw new BusinessException("O usuário informado não é um aluno.");
        }
 
        Student student = (Student) user;
        student.setSubscriptionPlan(newPlan.toUpperCase());
        return student;
    }
}
