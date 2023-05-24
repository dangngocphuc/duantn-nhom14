package com.fpoly.datn.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpoly.datn.entity.Bill;
import com.fpoly.datn.model.ThongKeUser;


@Repository
public interface BillRepo  extends JpaRepository<Bill, Long>{
	Page<Bill> findAll(Specification<Bill> specification, Pageable pageable);
	
	@Query(value = "SELECT t FROM Bill t WHERE t.billCode =:billCode")
	Bill findBillByCode(@Param("billCode") String billCode);
	
	@Query("SELECT new com.fpoly.datn.model.ThongKeUser(b.name, SUM(b.total)) FROM Bill b GROUP BY b.name ORDER BY SUM(b.total) DESC")
	List<ThongKeUser> getListUserBill();
}
