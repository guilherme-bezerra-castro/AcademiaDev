package com.academiadev.domain.entities;

public class Student extends User{
	private String subscriptionPlan;
	
	public Student(String id, String name, String email, String plan) {
		super(id, name, email);
		this.subscriptionPlan = plan;
	}

	public String getSubscriptionPlan() {
		return subscriptionPlan;
	}

	public void setSubscriptionPlan(String plan) {
		this.subscriptionPlan = plan;
	}

	public boolean canEnroll(int activeEnrollments) {
		if (subscriptionPlan.equals("BASIC")) {
			return activeEnrollments < 3;
		}
	    return true;
	}
}
