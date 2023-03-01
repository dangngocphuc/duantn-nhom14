package com.fpoly.datn.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_details")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seqProductDetail", sequenceName = "SEQ_PRODUCT_DETAIL", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqProductDetail")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = true)
	private Product product;

	@Column(name = "product_name")
	private String productName;

	@Column(name = "product_price")
	private Double productPrice;

	@Column(name = "product_marketprice")
	private Double productMarketprice;

	@ManyToOne
	@JoinColumn(name = "cpu_id")
	private Cpu cpu;

	@ManyToOne
	@JoinColumn(name = "ram_id")
	private Ram ram;

	@ManyToOne
	@JoinColumn(name = "rom_id")
	private Rom rom;

	@ManyToOne
	@JoinColumn(name = "gpu_id")
	private Gpu gpu;

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productDetail", cascade = CascadeType.ALL, orphanRemoval = true)
//	private Set<ProductDetailValue> listProductDetailValue;

	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productDetail", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Imei> listImei;

	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productDetail", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Review> listReview;

	@Column(name = "product_code")
	private String productCode;

	@Column(name = "product_size")
	private String productSize;

	@Column(name = "product_weight")
	private String productWeight;

	@Column(name = "demand")
	private String demand;

	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "update_date")
	private Date updateDate;

	@Transient
	private long quantity;

	public double calculateAverageRating() {
		double sum = 0;
		for (Review review : listReview) {
			sum += review.getReviewStar();
		}
		return sum / listReview.size();
	}
}
