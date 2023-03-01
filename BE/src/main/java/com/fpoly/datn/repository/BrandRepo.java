package com.fpoly.datn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpoly.datn.entity.Brand;

@Repository
public interface BrandRepo  extends JpaRepository<Brand, Long>{
	Page<Brand> findAll(Specification<Brand> specification, Pageable pageable);
}
