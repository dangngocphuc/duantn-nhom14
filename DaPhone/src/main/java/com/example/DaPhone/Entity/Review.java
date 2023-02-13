package com.example.DaPhone.Entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class Review {
	@Id
	@Column(name = "review_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewID;
	
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = true)
	private Product product;
	
	@Column(name = "review_name")
    private String reviewName;
	
	@Column(name = "user_id")
	private long userID;
	
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
