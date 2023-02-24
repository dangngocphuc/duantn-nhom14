package com.example.DaPhone.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.DaPhone.Entity.Brand;
import com.example.DaPhone.Entity.Category;
import com.example.DaPhone.Entity.Product;

@Repository
public interface ProductRepo  extends JpaRepository<Product, Long>,JpaSpecificationExecutor<Product>{

	Page<Product> findAll(Specification<Product> specification, Pageable pageable);
	List<Product> findByBrand(Brand brand);
//	List<Product> findByCategory(Category category);
	Optional<Product> findById(Long id);
}
