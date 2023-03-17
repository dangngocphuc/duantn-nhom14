package com.fpoly.datn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fpoly.datn.entity.Review;

@Repository
public interface ReviewRepo  extends CrudRepository<Review, Long> , JpaRepository<Review, Long>{
	Page<Review> findAll(Specification<Review> specification, Pageable pageable);
}