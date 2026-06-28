package com.academiadev.main;

import com.academiadev.application.repositories.CourseRepository;
import com.academiadev.application.repositories.UserRepository;
import com.academiadev.domain.entities.Admin;
import com.academiadev.domain.entities.Course;
import com.academiadev.domain.entities.Student;
import com.academiadev.domain.enums.DifficultyLevel;

public class InitialData {
	public static void populate(CourseRepository courseRepo, UserRepository userRepo) {
		
        Course c1 = new Course("C001", "Java do Zero ao Avançado",
	            "Domine Java do básico ao avançado com projetos reais.",
	            "Prof. Carlos Silva", 80, DifficultyLevel.BEGINNER);
	 
        Course c2 = new Course("C002", "Spring Boot com Clean Architecture",
	            "Construa APIs RESTful seguindo os princípios da Clean Architecture.",
	            "Prof. Ana Oliveira", 60, DifficultyLevel.INTERMEDIATE);
 
        Course c3 = new Course("C003", "Microserviços com Docker e Kubernetes",
	            "Orquestre microserviços em produção com containers.",
	            "Prof. Ricardo Mendes", 100, DifficultyLevel.ADVANCED);
 
        Course c4 = new Course("C004", "Algoritmos e Estruturas de Dados",
	            "Fundamentos essenciais para entrevistas técnicas.",
	            "Prof. Ana Oliveira", 40, DifficultyLevel.INTERMEDIATE);
 
        Course c5 = new Course("C005", "Python para Iniciantes",
        		"Primeiros passos com programação usando Python.",
        		"Prof. Fernanda Costa", 30, DifficultyLevel.BEGINNER);
 
        Course c6 = new Course("C006", "Machine Learning na Prática",
	            "Modelos preditivos com scikit-learn e TensorFlow.",
	            "Prof. Fernanda Costa", 120, DifficultyLevel.ADVANCED);
 
        // Inativa um curso para testar a regra de matrícula
        Course c7 = new Course("C007", "jQuery para Manutenção de Legado",
	    		"Manutenção de sistemas legados com jQuery.",
	            "Prof. João Antigo", 20, DifficultyLevel.BEGINNER);
        c7.deactivate();
 
        courseRepo.save(c1);
        courseRepo.save(c2);
        courseRepo.save(c3);
        courseRepo.save(c4);
        courseRepo.save(c5);
        courseRepo.save(c6);
        courseRepo.save(c7);

        Admin admin = new Admin("U001", "Admin Master", 
        		"admin@academiadev.com");
 
        Student s1 = new Student("U002", "Lucas Ferreira",
        		"lucas@email.com", "BASIC");
 
        Student s2 = new Student("U003", "Mariana Santos",
        		"mariana@email.com", "PREMIUM");
        
        Student s3 = new Student("U004", "Pedro Alves",
        		"pedro@email.com", "BASIC");
 
        userRepo.save(admin);
        userRepo.save(s1);
        userRepo.save(s2);
        userRepo.save(s3);
 
        System.out.println("  [InitialData] " + courseRepo.findAll().size() +
	                       " cursos e " + userRepo.findAll().size() +
	                       " usuários carregados.");
    }
}
