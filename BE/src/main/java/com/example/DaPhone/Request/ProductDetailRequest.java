package com.example.DaPhone.Request;

public class ProductDetailRequest extends BaseRequest {

	public String brandID;

	public String optionValueID;

	public String getBrandID() {
		return brandID;
	}

	public void setBrandID(String brandID) {
		this.brandID = brandID;
	}

	public String getOptionValueID() {
		return optionValueID;
	}

	public void setOptionValueID(String optionValueID) {
		this.optionValueID = optionValueID;
	}

}
