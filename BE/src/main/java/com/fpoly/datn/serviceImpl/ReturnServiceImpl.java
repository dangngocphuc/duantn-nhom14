package com.fpoly.datn.serviceImpl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpoly.datn.entity.ReturnBill;
import com.fpoly.datn.repository.ImeiRepository;
import com.fpoly.datn.repository.ReturnRepo;
import com.fpoly.datn.request.ImeiRequest;
import com.fpoly.datn.service.ReturnService;

@Service
public class ReturnServiceImpl implements ReturnService {

	@Autowired
	private ReturnRepo returnRepo;

	@Autowired
	private ImeiRepository imeiRepo;

	@Override
	public Page<ReturnBill> findBillByImei(ImeiRequest imeiParam, Pageable pageable) {
		
		return null;
	}

	@Override
	public boolean saveReturnBill(ReturnBill returnBill) {
		ReturnBill returnBills = returnRepo.save(returnBill);
		return true;
	}

	@Override
	public void deleteReturnBill(Long id) {
		returnRepo.deleteById(id);
	}

	@Override
	public void cancelReturnBill(Long id) {
		
	}

	@Override
	public ReturnBill getByID(Long id) {
		ReturnBill returnBills = returnRepo.findById(id).get();
		return returnBills;
	}

}
