package com.example.DaPhone.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	@Column(name = "bill_detail_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long billDetailID;
	
	@ManyToOne
	@JoinColumn(name = "bill_id", nullable = false)
	private Bill bill;
	
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
	
	@Column(name = "price")
    private long price;
	@Column(name = "quantity")
    private int quantity;
}
