package com.fpoly.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.fpoly.datn.entity.Promotion;
@Repository
public interface PromotionRepo extends JpaRepository<Promotion, Long>, JpaSpecificationExecutor<Promotion>{
	
	Promotion findByCode(String code);
}
