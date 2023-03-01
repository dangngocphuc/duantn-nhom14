package com.fpoly.datn.model;

public enum RoleName {
	USER(2L), STAFF(3L), ADMIN(1L);

	private final Long role;

	private RoleName(Long role) {
		this.role = role;
	}

	public Long getRole() {
		return role;
	}	

}