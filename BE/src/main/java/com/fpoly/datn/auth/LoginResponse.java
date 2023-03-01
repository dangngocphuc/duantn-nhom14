package com.fpoly.datn.auth;

public class LoginResponse {
	private String errorCode;
	private String errorMessage;
	private boolean authenticated=false;
	private String authorization;
	private UserDetail userDetail;

	public LoginResponse(String errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public LoginResponse() {
		super();
	}



	public LoginResponse(boolean authenticated,String authorization,UserDetail userDetail) {
		super();
		this.authenticated = authenticated;
		this.authorization = authorization;
		this.userDetail = userDetail;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

}
