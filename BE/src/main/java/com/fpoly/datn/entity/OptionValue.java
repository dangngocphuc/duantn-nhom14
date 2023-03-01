package com.fpoly.datn.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "option_values")
public class OptionValue implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seqOptionValue", sequenceName = "SEQ_OPTION_VALUE", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqOptionValue")
	private Long id;

	@Column(name = "option_value")
	private String optionValue;

	@ManyToOne
	@JoinColumn(name = "option_id")
	private Option option;

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "optionValue", cascade = CascadeType.ALL, orphanRemoval = true)
//	private Set<ProductDetailValue> listProductDetailValue;

	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public Option getOption() {
		return option;
	}

	public void setOption(Option option) {
		this.option = option;
	}

//	public Set<ProductDetailValue> getListProductDetailValue() {
//		return listProductDetailValue;
//	}
//
//	public void setListProductDetailValue(Set<ProductDetailValue> listProductDetailValue) {
//		this.listProductDetailValue = listProductDetailValue;
//	}

}
