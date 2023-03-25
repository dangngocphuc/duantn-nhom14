package com.fpoly.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fpoly.datn.entity.Imei;
import com.fpoly.datn.entity.ProductDetail;

@Repository
public interface ImeiRepository extends JpaRepository<Imei, Long>, JpaSpecificationExecutor<Imei>{
	
	@Query("Select t From Imei t  Where t.imei =:imei")
	Imei getByImei(String imei);
}
