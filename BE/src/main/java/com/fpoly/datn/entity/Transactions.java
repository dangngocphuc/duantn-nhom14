package com.fpoly.datn.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Transactions implements Serializable{
	
	@Id
	@SequenceGenerator(name = "seqTransaction", sequenceName = "SEQ_TRANSACTION", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqTransaction")
	private Long id;

	@Column(name = "vnp_Amount")
	private String vnp_Amount;
	
	@Column(name = "vnp_BankCode")
	private String vnp_BankCode;
	
	@Column(name = "vnp_CardType")
	private String vnp_CardType;
	
	@Column(name = "vnp_OrderInfo")
	private String vnp_OrderInfo;
	
	@Column(name = "vnp_PayDate")
	private String vnp_PayDate;
	
	@Column(name = "vnp_SecureHash")
	private String vnp_SecureHash;
	
	@Column(name = "vnp_TmnCode")
	private String vnp_TmnCode;
	
	@Column(name = "vnp_TransactionNo")
	private String vnp_TransactionNo;
	
	@Column(name = "vnp_TransactionStatus")
	private String vnp_TransactionStatus;
	
	@Column(name = "vnp_TxnRef")
	private String vnp_TxnRef;
	
	@Column(name = "vnp_ResponseCode")
	private String vnp_ResponseCode;
	
	
}
