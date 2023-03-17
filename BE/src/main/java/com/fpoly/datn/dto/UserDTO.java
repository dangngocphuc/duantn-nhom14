package com.fpoly.datn.dto;

import java.util.ArrayList;
import java.util.List;

import com.fpoly.datn.entity.Address;
import com.fpoly.datn.entity.Bill;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class UserDTO {

	private Long userID;

	private String userName;

	private String userEmail;

	private String userPass;

	private String userPhone;

	private boolean enabled;

	private List<Bill> listBill;

	private List<Address> listAddress;
	
}
