package com.fpoly.datn.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Transactions implements Serializable{
	
	@Id
	@SequenceGenerator(name = "seqTransaction", sequenceName = "SEQ_TRANSACTION", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqTransaction")
	private Long id;

	@Column(name = "vnp_amount")
	private String vnp_Amount;
	
	@Column(name = "vnp_bank_code")
	private String vnp_BankCode;
	
	@Column(name = "vnp_card_type")
	private String vnp_CardType;
	
	@Column(name = "vnp_order_info")
	private String vnp_OrderInfo;
	
	@Column(name = "vnp_pay_date")
	private String vnp_PayDate;
	
	@Column(name = "vnp_secure_hash")
	private String vnp_SecureHash;
	
	@Column(name = "vnp_tmn_code")
	private String vnp_TmnCode;
	
	@Column(name = "vnp_transaction_no")
	private String vnp_TransactionNo;
	
	@Column(name = "vnp_transaction_status")
	private String vnp_TransactionStatus;
	
	@Column(name = "vnp_txn_ref")
	private String vnp_TxnRef;
	
	@Column(name = "vnp_response_code")
	private String vnp_ResponseCode;
	
}
