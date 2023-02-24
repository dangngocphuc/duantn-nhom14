package com.example.DaPhone.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.DaPhone.Entity.Product;
import com.example.DaPhone.Request.ProductRequest;
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
}
