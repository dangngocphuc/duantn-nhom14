package com.example.DaPhone.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ram")
@Getter
@Setter
public class Ram implements Serializable {

	@Id
	@SequenceGenerator(name = "seqRam", sequenceName = "SEQ_Ram", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqRam")
	private Long id;

	@Column(name = "ram")
	private String ram;
}
