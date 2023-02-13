package com.example.DaPhone.Entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BILL")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
	@Id
	@Column(name = "bill_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long billID;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(name = "total")
    private long total;
	@Column(name = "payment")
    private String payment;
	@Column(name = "address")
    private String address;
	@Column(name = "date")
    private Date date;
	@Column(name = "name")
    private String name;
	@Column(name = "phone")
    private String phone;
	@Column(name = "status")
    private String status;
	@Transient
	private String products;
}
