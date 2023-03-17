package com.fpoly.datn.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@JsonIdentityInfo(scope = Bill.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Bill implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seqBill", sequenceName = "SEQ_BILL", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqBill")
    private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "total")
    private Double total;
	
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
	
	@Column(name = "note")
    private String note;
	
	@Column(name = "payment_status")
    private String paymentStatus;
	
	@Transient
	private String products;
	
	@Column(name="bill_code")
	private String billCode;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bill",  cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<BillDetail> listBillDetail;
}
