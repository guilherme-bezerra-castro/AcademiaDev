package com.academiadev.domain.entities;

import com.academiadev.domain.enums.CourseStatus;
import com.academiadev.domain.enums.DifficultyLevel;

public class Course {
	private String id;
    private String title;
    private String description;
    private String instructorName;
    private int durationInHours;
    private DifficultyLevel difficultyLevel;
    private CourseStatus status;

    public Course(String id, String title, String description, String instructorName, int durationInHours, DifficultyLevel difficultyLevel) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.instructorName = instructorName;
        this.durationInHours = durationInHours;
        this.difficultyLevel = difficultyLevel;
        this.status = CourseStatus.ACTIVE;
    }

    public String getId() { 
    	return id; 
    }
    
    public String getTitle() { 
    	return title; 
    }
    
    public String getDescription() {
    	return description; 
    }
    
    public String getInstructorName() { 
    	return instructorName; 
    }
    public int getDurationInHours() { 
    	return durationInHours; 
    }
    
    public DifficultyLevel getDifficultyLevel() { 
    	return difficultyLevel; 
    }
    public CourseStatus getStatus() { 
    	return status; 
    }

    public boolean isActive() {
        return status == CourseStatus.ACTIVE;
    }

    public void activate() { 
    	this.status = CourseStatus.ACTIVE; 
    }
    
    public void deactivate() {
    	this.status = CourseStatus.INACTIVE; 
    }

}
