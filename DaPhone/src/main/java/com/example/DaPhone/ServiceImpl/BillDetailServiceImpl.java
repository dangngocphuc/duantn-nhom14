package com.example.DaPhone.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DaPhone.Entity.Bill;
import com.example.DaPhone.Entity.BillDetail;
import com.example.DaPhone.Repository.BillDetailRepo;
import com.example.DaPhone.Service.BillDetailService;
@Service
public class BillDetailServiceImpl implements BillDetailService{
	
	@Autowired
	private BillDetailRepo billDetailRepo; 

	@Override
	public List<BillDetail> findByBill(Long id){
		Bill bill = new Bill();
		bill.setBillID(id);
		return billDetailRepo.findByBill(bill);
	}
	
	@Override
	public BillDetail saveBillDetail(BillDetail billDetail) {
		return billDetailRepo.save(billDetail);
	}
}
