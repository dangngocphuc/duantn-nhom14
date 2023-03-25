package com.fpoly.datn.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "promotion")
@Getter
@Setter
public class Promotion implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seqPromotion", sequenceName = "SEQ_PROMOTION", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqPromotion")
	private Long id;

	@Column(name = "code")
	private String code;
	
	@Column(name = "type")
	private Integer type;

	@Column(name = "value")
	private Double value;
	
	@Column(name = "date_from")
	private Date dateFrom;
	
	@Column(name = "date_to")
	private Date dateTo;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "count")
	private Integer count;
	
}
