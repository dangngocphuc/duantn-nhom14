package com.fpoly.datn.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpoly.datn.entity.Brand;
import com.fpoly.datn.entity.Product;
import com.fpoly.datn.request.ProductRequest;
@Service
public interface ProductService {
	public Page<Product> findProduct(ProductRequest productParam, Pageable pageable);
	public Product findById(Long id);
	public boolean saveProduct(Product product);
	public void deleteProduct(Long id);
	public ByteArrayInputStream exportExcelProduct(ProductRequest productParam) throws IOException;
	public List<Long> getSatisticBrand();
	public List<Long> getSatisticCategory();
	public List<Product> ngSelect(Pageable pageable, ProductRequest request);
	public List<Product> findAll();
}
