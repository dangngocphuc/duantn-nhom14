package com.example.DaPhone.Model;

public enum RoleName {
	USER(1L), STAFF(3L), ADMIN(2L);

	private final Long role;

	private RoleName(Long role) {
		this.role = role;
	}

	public Long getRole() {
		return role;
	}	

}