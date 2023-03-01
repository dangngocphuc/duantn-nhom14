package com.fpoly.datn.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fpoly.datn.entity.BillDetail;
@Service
public interface BillDetailService {
	public List<BillDetail> findByBill(Long id);
	public BillDetail saveBillDetail(BillDetail billDetail);
}
