package com.example.DaPhone.Service;

import org.springframework.stereotype.Service;

import com.example.DaPhone.Model.LoginRequest;

@Service
public interface UserAdminService {
	public Long loginAdmin(LoginRequest loginRequest);
	
}
