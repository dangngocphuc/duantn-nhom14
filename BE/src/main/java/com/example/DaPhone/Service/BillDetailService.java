package com.example.DaPhone.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.DaPhone.Entity.BillDetail;
@Service
public interface BillDetailService {
	public List<BillDetail> findByBill(Long id);
	public BillDetail saveBillDetail(BillDetail billDetail);
}
