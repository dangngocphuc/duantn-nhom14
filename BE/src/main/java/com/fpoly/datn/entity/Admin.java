package com.fpoly.datn.entity;

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
@Table(name = "admin")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_id;
	@Column(name = "user_name")
	private String user_name;
	@Column(name = "user_pass")
	private String user_pass;
	@Column(name = "user_fullname")
	private String user_fullname;
	@Column(name = "user_roles")
	private String user_roles;
}
