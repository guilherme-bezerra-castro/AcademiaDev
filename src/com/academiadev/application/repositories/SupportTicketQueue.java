package com.academiadev.application.repositories;

import com.academiadev.domain.entities.SupportTicket;

public interface SupportTicketQueue {
	void addTicket(SupportTicket ticket);
    SupportTicket nextTicket();
    boolean isEmpty();
    int size();
}
