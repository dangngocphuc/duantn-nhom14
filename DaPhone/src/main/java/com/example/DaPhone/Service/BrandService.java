package com.example.DaPhone.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.DaPhone.Entity.Brand;
import com.example.DaPhone.Entity.Product;
import com.example.DaPhone.Request.BrandRequest;

@Service
public interface BrandService {
	public Page<Brand> findBrand(BrandRequest brandParam, Pageable pageable);
	public List<Brand> findAll();
	public Brand saveBrand(Brand brand);
	public void deleteBrand(Long id);
}
