package com.example.DaPhone.Model;

import com.example.DaPhone.Entity.ProductDetail;

import lombok.Data;

@Data
public class TranformImei {
	private Long id;
	private ProductDetail productDetail;
	private String imei;
	private Integer status;
}