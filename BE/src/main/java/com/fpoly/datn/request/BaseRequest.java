package com.fpoly.datn.request;

import lombok.Data;

@Data
public class BaseRequest {
	private int pageIndex;
	private int pageSize;
	private String sortField;
	private String sortOrder;
//	public Long brandID;
}
