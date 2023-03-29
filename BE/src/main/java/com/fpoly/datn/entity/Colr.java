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
@Table(name = "color")
@Getter
@Setter
public class Colr implements Serializable {

	private static final long serialVersionUID = 1L;


	@Id
	@SequenceGenerator(name = "seqColor", sequenceName = "SEQ_COLOR", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqColor")
	private Long id;

	@Column(name = "color")
	private String color;
	
	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "update_date")
	private Date updateDate;
	
	@Column(name = "status")
	private Integer status;
}
