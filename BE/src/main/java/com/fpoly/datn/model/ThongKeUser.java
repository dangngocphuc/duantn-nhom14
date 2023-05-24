package com.fpoly.datn.model;

public class ThongKeUser {
	private String name;
	private Double count;
	
	public ThongKeUser(String name, Double count) {
		super();
		this.name = name;
		this.count = count;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getCount() {
		return count;
	}

	public void setCount(Double count) {
		this.count = count;
	}

}
