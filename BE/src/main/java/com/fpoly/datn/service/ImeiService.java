package com.fpoly.datn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fpoly.datn.entity.Imei;
import com.fpoly.datn.request.ImeiRequest;

public interface ImeiService {
	public Page<Imei> getPageImei(ImeiRequest req, Pageable pageable);
	public Imei findById(Long id);
	public boolean saveImei(Imei imei);
	public boolean deleteImei(Long id);
}
