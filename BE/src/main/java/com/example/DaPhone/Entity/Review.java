package com.example.DaPhone.Entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "review")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seqReview", sequenceName = "SEQ_REVIEW", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqReview")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_detail_id", nullable = true)
	@JsonBackReference
	private ProductDetail productDetail;

	@Column(name = "review_name")
	private String reviewName;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "review_star")
	private int reviewStar;

	@Column(name = "review_message")
	private String reviewMessage;

	@Column(nullable = true, updatable = false, name = "date")
	@CreationTimestamp
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate date;

	@Column(name = "status")
	private Long status;
}
