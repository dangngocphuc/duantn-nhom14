package com.fpoly.datn.model;

import com.fpoly.datn.request.BaseRequest;

public class OptionRequest extends BaseRequest{
	private String optionMa;
	private String optionTen;

	public String getOptionMa() {
		return optionMa;
	}

	public void setOptionMa(String optionMa) {
		this.optionMa = optionMa;
	}

	public String getOptionTen() {
		return optionTen;
	}

	public void setOptionTen(String optionTen) {
		this.optionTen = optionTen;
	}

}
