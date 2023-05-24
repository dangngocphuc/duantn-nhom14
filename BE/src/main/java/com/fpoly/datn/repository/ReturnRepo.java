//package com.fpoly.datn.repository;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import com.fpoly.datn.entity.ReturnBill;
//
//@Repository
//public interface ReturnRepo extends JpaRepository<ReturnBill, Long> {
//	Page<ReturnBill> findAll(Specification<ReturnBill> spec, Pageable pageable);
//	
//	@Query(value = "SELECT r FROM ReturnBill r WHERE r.returnCode = :returnCode")
//	ReturnBill findReturnBillByCode(@Param("returnCode")String returnCode);
//	
//	@Query(value ="SELECT r FROM ReturnBill r WHERE r.imei = :imei")
//	ReturnBill findReturnBillByImei(@Param("imei")String imei);
//}
