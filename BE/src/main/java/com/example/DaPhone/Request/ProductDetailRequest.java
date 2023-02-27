package com.example.DaPhone.Request;

import java.util.Date;

public class ProductDetailRequest extends BaseRequest {
	
	public String brandId;

	public String productId;

	public String productCode;

	public String productName;

	public String lstCpu;

	public String lstRam;

	public String lstGpu;

	public String lstRom;

	public Date toDate;

	public Date fromDate;

	private long priceFrom;

	private long priceTo;

	private boolean inventory;

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

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getLstCpu() {
		return lstCpu;
	}

	public void setLstCpu(String lstCpu) {
		this.lstCpu = lstCpu;
	}

	public String getLstRam() {
		return lstRam;
	}

	public void setLstRam(String lstRam) {
		this.lstRam = lstRam;
	}

	public String getLstGpu() {
		return lstGpu;
	}

	public void setLstGpu(String lstGpu) {
		this.lstGpu = lstGpu;
	}

	public String getLstRom() {
		return lstRom;
	}

	public void setLstRom(String lstRom) {
		this.lstRom = lstRom;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

}
