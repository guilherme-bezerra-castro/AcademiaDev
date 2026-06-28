package com.academiadev.domain.entities;

import com.academiadev.domain.exceptions.BusinessException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Enrollment {
	private final String id;
    private final Student student;
    private final Course course;
    private int progress;
    private LocalDateTime enrolledAt;
    
    public Enrollment(String id, Student student, Course course) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.progress = 0;
        this.enrolledAt = LocalDateTime.now();
    }

    public String getId() { 
    	return id; 
    }
    public Student getStudent() { 
    	return student; 
    }
    public Course getCourse() { 
    	return course; 
    }
    public int getProgress() { 
    	return progress; 
    }
    public LocalDateTime getEnrolledAt() { 
    	return enrolledAt; 
    }

    public void updateProgress(int percent) {
        if (percent < 0 || percent > 100) {
            throw new com.academiadev.domain.exceptions.BusinessException("Progresso deve ser entre 0 e 100.");
        }
        this.progress = percent;
    }
    
    @Override
    public String toString() {
        String data = enrolledAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        
        return String.format("Curso: %-40s | Progresso: %3d%% | Matriculado em: %s", course.getTitle(), progress, data);
    }
}
