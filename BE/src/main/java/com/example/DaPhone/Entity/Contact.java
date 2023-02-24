package com.example.DaPhone.Entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "contact")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
	@Id
	@Column(name = "contact_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long contactID;
	@Column(name = "contact_name")
    private String contactName;
	@Column(name = "contact_web")
    private String contactWeb;
	@Column(name = "contact_email")
    private String contactEmail;
	@Column(name = "contact_title")
    private String contactTitle;
	@Column(name = "contact_message")
    private String contactMessage;
	@Column(name = "contact_date")
    private Date date;
}
