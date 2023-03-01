package com.fpoly.datn.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fpoly.datn.entity.ProductDetail;

@Repository
public interface ProductDetailRepo  extends JpaRepository<ProductDetail, Long>,JpaSpecificationExecutor<ProductDetail>{

	Page<ProductDetail> findAll(Specification<ProductDetail> specification, Pageable pageable);
//	List<Product> findByBrand(Brand brand);
//	List<Product> findByCategory(Category category);
	
	@Query("Select t From ProductDetail t  Where t.id =:id ")
	ProductDetail getById(Long id);
}
