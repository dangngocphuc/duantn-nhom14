package com.fpoly.datn.service;

import org.springframework.stereotype.Service;

import com.fpoly.datn.model.LoginRequest;

@Service
public interface UserAdminService {
	public Long loginAdmin(LoginRequest loginRequest);
	
}
