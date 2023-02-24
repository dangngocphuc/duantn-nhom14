package com.example.DaPhone.Entity;

import java.io.Serializable;
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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(scope = Product.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Product implements Serializable{ 
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seqProduct", sequenceName = "SEQ_PRODUCT", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqProduct")
	private Long id;
	
	@Column(name = "ma_sanpham")
	private String maSanPham;

	@Column(name = "ten_sanpham")
	private String tenSanPham;
	
	@ManyToOne
	@JoinColumn(name = "brand_id")
    private Brand brand;
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product",  cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductDetail> listProductDetail;
	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product",  cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductOption> listProductOption;
	
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product",  cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Image> listImage;
	
}
