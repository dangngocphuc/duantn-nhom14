package com.fpoly.datn.model;

import com.fpoly.datn.entity.ProductDetail;

import lombok.Data;

@Data
public class TranformImei {
	private Long id;
	private ProductDetail productDetail;
	private String imei;
	private Integer status;
}
