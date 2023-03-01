package com.fpoly.datn.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpoly.datn.entity.Brand;
import com.fpoly.datn.entity.Product;
import com.fpoly.datn.request.BrandRequest;

@Service
public interface BrandService {
	public Page<Brand> findBrand(BrandRequest brandParam, Pageable pageable);
	public List<Brand> findAll();
	public Brand saveBrand(Brand brand);
	public Brand findById(Long id);
	public void deleteBrand(Long id);
}
