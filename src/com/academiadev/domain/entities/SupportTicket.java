package com.academiadev.domain.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SupportTicket {
	private String id;
    private String title;
    private String message;
    private User openedBy;
    private LocalDateTime createdAt;

    public SupportTicket(String id, String title, String message, User openedBy) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.openedBy = openedBy;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { 
    	return id; 
    }
    public String getTitle() { 
    	return title; 
    }
    public String getMessage() { 
    	return message; 
    }
    public User getOpenedBy() { 
    	return openedBy; 
    }
    public LocalDateTime getCreatedAt() { 
    	return createdAt; 
    }
    
    @Override
    public String toString() {
        String data = createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        return String.format("[Ticket #%s] \"%s\"\n  Aberto por: %s\n  Mensagem: %s\n  Data: %s",
                id, title, openedBy.getName(), message, data);
    }
}
