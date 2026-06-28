package com.academiadev.domain.entities;

public class Student extends User{
	private String subscriptionPlan;
	
	public Student(String id, String name, String email, String subscriptionPlan) {
		super(id, name, email);
		this.subscriptionPlan = subscriptionPlan;
	}

	public String getSubscriptionPlan() {
		return subscriptionPlan;
	}

	public void setSubscriptionPlan(String subscriptionPlan) {
		this.subscriptionPlan = subscriptionPlan;
	}

	public boolean canEnroll(int activeEnrollments) {
        if ("BASIC".equalsIgnoreCase(subscriptionPlan)) {
            return activeEnrollments < 3;
        }
	    return true;
	}
	@Override
    public String toString() {
        return super.toString() + " | Plano: " + subscriptionPlan;
    }
}
