package com.example.DaPhone.Entity;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	private String productPrice;

	@Column(name = "product_marketprice")
	private String productMarketprice;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productDetail", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductDetailValue> listProductDetailValue;

	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productDetail", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Imei> listImei;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productDetail", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Review> listReview;

	@Transient
	private long quantity;

	public double calculateAverageRating() {
		double sum = 0;
		for(Review review : listReview) {
			sum += review.getReviewStar();
		}
		return sum / listReview.size();
	}

}
