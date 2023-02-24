package com.example.DaPhone.Dto;

import java.util.Set;

import com.example.DaPhone.Entity.OptionValue;
import com.example.DaPhone.Entity.ProductDetailValue;

public class OptionDto {

	private long id;

	private String optionCode;

	private String optionName;

	private Integer status;
	
	private Set<OptionValue> listOptionValue;
	
	private Set<ProductDetailValue> lisProductDetailValue;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOptionCode() {
		return optionCode;
	}

	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Set<OptionValue> getListOptionValue() {
		return listOptionValue;
	}

	public void setListOptionValue(Set<OptionValue> listOptionValue) {
		this.listOptionValue = listOptionValue;
	}

	public Set<ProductDetailValue> getLisProductDetailValue() {
		return lisProductDetailValue;
	}

	public void setLisProductDetailValue(Set<ProductDetailValue> lisProductDetailValue) {
		this.lisProductDetailValue = lisProductDetailValue;
	}

}