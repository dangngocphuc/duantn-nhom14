package com.fpoly.datn.model;

import java.util.ArrayList;
import java.util.List;

import com.fpoly.datn.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
	private String auth;
	private String username;
	private Long userId;
	private List<String> permissions = new ArrayList<String>();

	public LoginResponse(String auth, String username) {
		super();
		this.auth = auth;
		this.username = username;
	}

	public LoginResponse(String auth, String username,Long userId) {
		super();
		this.auth = auth;
		this.username = username;
		this.userId   = userId;
	}

}
