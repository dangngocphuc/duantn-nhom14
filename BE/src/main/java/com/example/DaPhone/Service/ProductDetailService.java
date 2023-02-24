package com.example.DaPhone.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.DaPhone.Entity.ProductDetail;
import com.example.DaPhone.Request.ProductDetailRequest;
@Service
public interface ProductDetailService {
	public Page<ProductDetail> findProduct(ProductDetailRequest productParam, Pageable pageable);
	public ProductDetail findById(Long id);
	public boolean saveOrUpdate(ProductDetail productDetail);
	public List<ProductDetail> getListProduct();
	public String compareLaptops (List<ProductDetail> listProductDetail);
}
