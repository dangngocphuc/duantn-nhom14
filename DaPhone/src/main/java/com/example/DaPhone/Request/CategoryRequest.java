package com.example.DaPhone.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest extends BaseRequest {

	private long categoryID;
	private String categoryName;
}
