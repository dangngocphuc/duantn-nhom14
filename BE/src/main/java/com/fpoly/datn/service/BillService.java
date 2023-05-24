package com.fpoly.datn.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpoly.datn.entity.Bill;
import com.fpoly.datn.model.ThongKeUser;
import com.fpoly.datn.request.BillRequest;

@Service
public interface BillService {
	public Page<Bill> findBill(BillRequest billParam, Pageable pageable);
	public List<Long> statisticBillByWeek();
	public List<Long> statisticBillByMonth();
	public ByteArrayInputStream exportExcel(BillRequest billParam) throws IOException;
	public boolean saveBill(Bill bill);
	public void cancelBill(Long id);
	public void deleteBill(Long id);
	public Bill getById(Long id);
	public boolean paymentBill(Bill bill);
	public List<ThongKeUser> statisticalUser();
}
