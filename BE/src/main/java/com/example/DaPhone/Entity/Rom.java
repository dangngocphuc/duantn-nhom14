package com.example.DaPhone.Entity;

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
@Table(name = "rom")
@Getter
@Setter

public class Rom implements Serializable {

	@Id
	@SequenceGenerator(name = "seqRom", sequenceName = "SEQ_ROM", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqRom")
	private Long id;

	@Column(name = "rom")
	private String rom;
	
	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "update_date")
	private Date updateDate;
	
	@Column(name = "status")
	private Integer status;
}
