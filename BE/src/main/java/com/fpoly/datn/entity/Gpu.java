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
@Table(name = "gpu")
@Getter
@Setter

public class Gpu implements Serializable {
	@Id
	@SequenceGenerator(name = "seqGpu", sequenceName = "SEQ_GPU", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGpu")
	private Long id;

	@Column(name = "gpu")
	private String gpu;

	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "update_date")
	private Date updateDate;

	@Column(name = "status")
	private Integer status;
}