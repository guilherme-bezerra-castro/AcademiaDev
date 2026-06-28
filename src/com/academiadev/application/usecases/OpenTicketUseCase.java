package com.academiadev.application.usecases;

import com.academiadev.application.repositories.SupportTicketQueue;
import com.academiadev.domain.entities.SupportTicket;
import com.academiadev.domain.entities.User;
 
import java.util.UUID;

public class OpenTicketUseCase {
	private final SupportTicketQueue ticketQueue;
	 
    public OpenTicketUseCase(SupportTicketQueue ticketQueue) {
        this.ticketQueue = ticketQueue;
    }
 
    public SupportTicket execute(User user, String title, String message) {
        SupportTicket ticket = new SupportTicket(
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                title, message, user);
        ticketQueue.addTicket(ticket);
        return ticket;
    }
}
