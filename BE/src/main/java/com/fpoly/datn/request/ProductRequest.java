package com.fpoly.datn.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest extends BaseRequest {
	private long productID;
	private long categoryID;
	private long brandID;
	private String productName;
	private long priceFrom;
	private long priceTo;
	private boolean inventory;

}
