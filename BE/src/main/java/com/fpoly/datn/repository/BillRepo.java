package com.fpoly.datn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpoly.datn.entity.Bill;


@Repository
public interface BillRepo  extends JpaRepository<Bill, Long>{
	Page<Bill> findAll(Specification<Bill> specification, Pageable pageable);
	
	@Query(value = "SELECT t FROM Bill t WHERE t.billCode =:billCode")
	Bill findBillByCode(@Param("billCode") String billCode);
}