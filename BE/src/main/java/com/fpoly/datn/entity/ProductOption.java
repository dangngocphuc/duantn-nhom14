package com.fpoly.datn.entity;

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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_options")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOption implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seqProductOption", sequenceName = "SEQ_PRODUCT_OPTION", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqProductOption")
	private Long id;

//	@ManyToOne
//	@JoinColumn(name = "product_id")
//	private Product product;

	@ManyToOne
	@JoinColumn(name = "option_id")
	private Option option;

}
