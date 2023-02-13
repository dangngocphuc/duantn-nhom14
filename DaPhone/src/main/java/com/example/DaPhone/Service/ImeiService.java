package com.example.DaPhone.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.DaPhone.Entity.Imei;
import com.example.DaPhone.Request.ImeiRequest;

public interface ImeiService {
	public Page<Imei> getPageImei(ImeiRequest req, Pageable pageable);
	public Imei findById(Long id);
	public boolean saveImei(Imei imei);
	public boolean deleteImei(Long id);
}
