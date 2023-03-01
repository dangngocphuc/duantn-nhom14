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
@Table(name = "cpu")
@Getter
@Setter

public class Cpu implements Serializable {

	@Id
	@SequenceGenerator(name = "seqCpu", sequenceName = "SEQ_CPU", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqCpu")
	private Long id;

	@Column(name = "cpu")
	private String cpu;

	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "update_date")
	private Date updateDate;
	
	@Column(name = "status")
	private Integer status;
	
}
