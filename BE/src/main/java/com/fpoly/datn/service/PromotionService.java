package com.fpoly.datn.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fpoly.datn.entity.Promotion;

public interface PromotionService {

	public Page<Promotion> getPagePromotion(Pageable pageable);

	public Promotion findById(Long id);
	
	public Promotion findByCode(String ma);

	public boolean savePromotion(Promotion promotion);

	public boolean deletePromotion(Long id);

	public List<Promotion> getListPromotion();
}
