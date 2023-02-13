package com.example.DaPhone.Request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewParam extends BaseParam{
	private String reviewName;
	private long reviewStar;
	private long productId;
	private String productName;
	private Long status;
}
