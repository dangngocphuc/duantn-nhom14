package com.example.DaPhone.Request;

import lombok.Data;

@Data
public class BaseRequest {
	private int pageIndex;
	private int pageSize;
	private String sortField;
	private String sortOrder;
}
