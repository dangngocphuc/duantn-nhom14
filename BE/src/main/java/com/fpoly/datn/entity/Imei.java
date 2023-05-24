package com.fpoly.datn.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "imei")
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIdentityInfo(scope = Imei.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Imei implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seqImei", sequenceName = "SEQ_IMEI", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqImei")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_detail_id")
	@JsonBackReference
	private ProductDetail productDetail;
	
	@ManyToOne
	@JoinColumn(name = "bill_detail_id")
//	@JsonBackReference
	private BillDetail billDetail;

	private String imei;

	private Integer status;

	@Transient
	private String productName;

//	@Column(name="returned_date")
//	private Date returnDate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProductDetail getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(ProductDetail productDetail) {
		this.productDetail = productDetail;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BillDetail getBillDetail() {
		return billDetail;
	}

	public void setBillDetail(BillDetail billDetail) {
		this.billDetail = billDetail;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	/*
	 * public Date getReturnDate() { return returnDate; }
	 * 
	 * public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
	 */

}
