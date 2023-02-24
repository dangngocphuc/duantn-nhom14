package com.example.DaPhone.Entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_detail_value")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailValue implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seqProductDetailValue", sequenceName = "SEQ_PRODUCT_DETAIL_VALUE", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqProductDetailValue")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "product_detail_id", nullable = true)
	private ProductDetail productDetail;
	
	@ManyToOne
	@JoinColumn(name = "option_id")
	private Option option;
	
	@ManyToOne
	@JoinColumn(name = "option_value_id")
	private OptionValue optionValue;
	
}
