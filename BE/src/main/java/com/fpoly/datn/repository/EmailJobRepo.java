package com.fpoly.datn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpoly.datn.entity.EmailJob;

@Repository
public interface EmailJobRepo extends JpaRepository<EmailJob, Long>{
	List<EmailJob> findByStatus(int status);
}
