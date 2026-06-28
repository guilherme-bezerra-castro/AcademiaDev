package com.academiadev.application.usecases;

import com.academiadev.application.repositories.SupportTicketQueue;
import com.academiadev.domain.entities.SupportTicket;
import com.academiadev.domain.exceptions.BusinessException;

public class ProcessTicketUseCase {
	private final SupportTicketQueue ticketQueue;
	 
    public ProcessTicketUseCase(SupportTicketQueue ticketQueue) {
        this.ticketQueue = ticketQueue;
    }
 
    public SupportTicket execute() {
        if (ticketQueue.isEmpty()) {
            throw new BusinessException("Não há tickets na fila de suporte.");
        }
        return ticketQueue.nextTicket();
    }
 
    public int pendingCount() {
        return ticketQueue.size();
    }
}
