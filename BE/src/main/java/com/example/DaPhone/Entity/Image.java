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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Image")
@Getter
@Setter
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIdentityInfo(scope = Imei.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Image implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seqImei", sequenceName = "SEQ_IMEI", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqImei")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonBackReference
	private Product product;

	@Column(name="thumb_image")
	private String thumbImage;
	
	@Column(name="images")
	private String image;


}
