package com.fpoly.datn.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ram")
@Getter
@Setter
public class Ram implements Serializable {

	@Id
	@SequenceGenerator(name = "seqRam", sequenceName = "SEQ_RAM", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqRam")
	private Long id;

	@Column(name = "ram")
	private String ram;
	
	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "update_date")
	private Date updateDate;
	
	@Column(name = "status")
	private Integer status;
}
