package com.example.DaPhone.Request;

public class ProductDetailRequest extends BaseRequest {

	public String brandID;

	public String optionValueID;

	private long priceFrom;

	private long priceTo;
	
	private boolean inventory;

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

	public long getPriceFrom() {
		return priceFrom;
	}

	public void setPriceFrom(long priceFrom) {
		this.priceFrom = priceFrom;
	}

	public long getPriceTo() {
		return priceTo;
	}

	public void setPriceTo(long priceTo) {
		this.priceTo = priceTo;
	}

	public boolean isInventory() {
		return inventory;
	}

	public void setInventory(boolean inventory) {
		this.inventory = inventory;
	}
}
