package com.example.DaPhone.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "brand")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "ma_hang")
    private String maHang;
	
	@Column(name = "ten_hang")
    private String tenHang;
	
	@Column(name = "ngay_tao")
    private Date ngayTao;
	
	@ManyToOne
	@JoinColumn(name = "nguoi_tao")
    private User nguoiTao;
	
	@Column(name = "ngay_sua")
    private Date ngaySua;
	
	@ManyToOne
	@JoinColumn(name = "nguoi_sua")
    private User nguoiSua;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "brand", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<Product> listProduct = new ArrayList<>();
}
