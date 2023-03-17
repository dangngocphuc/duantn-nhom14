package com.fpoly.datn.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fpoly.datn.entity.Imei;
import com.fpoly.datn.request.ImeiRequest;

public interface ImeiService {
	public Page<Imei> getPageImei(ImeiRequest req, Pageable pageable);
	public List<Imei> getListImeiByProductDetail(ImeiRequest req, Pageable pageable);
	public Imei findById(Long id);
	public boolean saveImei(Imei imei);
	public boolean deleteImei(Long id);
}
