package com.example.DaPhone.Entity;

import java.io.Serializable;

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
@Table(name = "ram")
@Getter
@Setter

public class Ram implements Serializable{
	
	@Id
	@SequenceGenerator(name = "seqRam", sequenceName = "SEQ_RAM", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqRam")
	private Long id;
	
	@Column(name="ram")
	private String ram;
	
}
