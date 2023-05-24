//package com.fpoly.datn.entity;
//
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToOne;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.Table;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
////@Entity
////@Table(name = "return_bill")
//@Getter
//@Setter
//@NoArgsConstructor
//@Data
//public class ReturnBill {
//	@Id
//	@SequenceGenerator(name = "seqReturn", sequenceName = "SEQ_RETURN", allocationSize = 1, initialValue = 1)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqReturn")
//	private Long id;
//
//	@ManyToOne
//	@JoinColumn(name = "user_id")
//	private User user;
//
//	@Column(name = "return_code")
//	private String returnCode;
//	
//	@OneToOne
//	@JoinColumn(name = "imei")
//	private Imei imei;
//
//	@Column(name = "phone")
//	private String phone;
//
//	@Column(name ="returned_date")
//	private Date returnDate;
//
//	@Column(name ="created_date")
//	private Date createDate;
//
//	@Column(name = "status")
//	private String status;
//
//	@Column(name = "note")
//	private String note;
//}
