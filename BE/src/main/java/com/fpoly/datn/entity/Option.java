package com.fpoly.datn.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "options")
public class Option implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seqOption", sequenceName = "SEQ_OPTION", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqOption")
	private Long id;

	@Column(name = "option_code")
	private String optionCode;

	@Column(name = "option_name")
	private String optionName;

	@Column(name = "status")
	private Integer status;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("optionValue ASC")
	private Set<OptionValue> listOptionValue;

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
//	private Set<ProductDetailValue> listProductDetailValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOptionCode() {
		return optionCode;
	}

	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Set<OptionValue> getListOptionValue() {
		return listOptionValue;
	}

	public void setListOptionValue(Set<OptionValue> listOptionValue) {
		this.listOptionValue = listOptionValue;
	}
}
