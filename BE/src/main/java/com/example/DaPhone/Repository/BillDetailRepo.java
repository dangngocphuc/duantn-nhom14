package com.example.DaPhone.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.DaPhone.Entity.Bill;
import com.example.DaPhone.Entity.BillDetail;
@Repository
public interface BillDetailRepo extends JpaRepository<BillDetail, Long>{
	public List<BillDetail> findByBill(Bill bill);
}
