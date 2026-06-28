package com.academiadev.infrastructure.persistence;

import com.academiadev.application.repositories.SupportTicketQueue;
import com.academiadev.domain.entities.SupportTicket;
 
import java.util.ArrayDeque;
import java.util.Queue;

public class SupportTicketQueueInMemory implements SupportTicketQueue {
	private final Queue<SupportTicket> queue = new ArrayDeque<>();
	
	@Override
    public void addTicket(SupportTicket ticket) {
        queue.offer(ticket);
    }
 
    @Override
    public SupportTicket nextTicket() {
        return queue.poll();
    }
 
    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
 
    @Override
    public int size() {
        return queue.size();
    }
}
