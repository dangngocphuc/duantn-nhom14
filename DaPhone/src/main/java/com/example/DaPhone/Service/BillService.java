package com.example.DaPhone.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.DaPhone.Entity.Bill;
import com.example.DaPhone.Request.BillRequest;

@Service
public interface BillService {
	public Page<Bill> findBill(BillRequest billParam, Pageable pageable);
	public List<Long> statisticBillByWeek();
	public List<Long> statisticBillByMonth();
	public ByteArrayInputStream exportExcel(BillRequest billParam) throws IOException;
	public boolean saveBill(Bill bill);
	public void cancelBill(Bill bill);
	public void deleteBill(Long id);
	public Bill paymentBill(Bill bill);
}
