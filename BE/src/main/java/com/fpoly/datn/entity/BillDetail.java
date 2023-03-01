package com.fpoly.datn.entity;

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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bill_detail")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BillDetail {
	@Id
	@SequenceGenerator(name = "seqBillDetail", sequenceName = "SEQ_BILL_DETAIL", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqBillDetail")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "bill_id")
	private Bill bill;

	@ManyToOne
	@JoinColumn(name = "product_detail_id")
	private ProductDetail productDetail;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productDetail",  cascade = CascadeType.MERGE, orphanRemoval = true)
	private Set<Imei> listImei;

	@Column(name = "price")
	private Double price;

	@Column(name = "quantity")
	private int quantity;
	
	
}
