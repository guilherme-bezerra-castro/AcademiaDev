package com.academiadev.domain.entities;

import java.time.LocalDateTime;

public class Enrollment {
	private String id;
    private Student student;
    private Course course;
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
            throw new com.academiadev.domain.exceptions.BusinessException(
                "Progresso deve ser entre 0 e 100.");
        }
        this.progress = percent;
    }
}
