package com.fpoly.datn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpoly.datn.entity.Bill;
import com.fpoly.datn.entity.BillDetail;
@Repository
public interface BillDetailRepo extends JpaRepository<BillDetail, Long>{
	
	public List<BillDetail> findByBill(Bill bill);
	
}
