package com.fpoly.datn.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.datn.entity.Bill;
import com.fpoly.datn.entity.BillDetail;
import com.fpoly.datn.repository.BillDetailRepo;
import com.fpoly.datn.service.BillDetailService;

@Service
public class BillDetailServiceImpl implements BillDetailService {

	@Autowired
	private BillDetailRepo billDetailRepo;

	@Override
	public List<BillDetail> findByBill(Long id) {
		Bill bill = new Bill();
		bill.setId(id);
		List<BillDetail> billDetail = billDetailRepo.findByBill(bill);
		return billDetail;
	}

	@Override
	public BillDetail saveBillDetail(BillDetail billDetail) {
		return billDetailRepo.save(billDetail);
	}
}
