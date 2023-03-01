package com.fpoly.datn.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest extends BaseRequest {
	private String userName;
	private long phone;
	private String email;
}
