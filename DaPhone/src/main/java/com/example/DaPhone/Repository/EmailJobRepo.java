package com.example.DaPhone.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.DaPhone.Entity.EmailJob;

@Repository
public interface EmailJobRepo extends JpaRepository<EmailJob, Long>{
	List<EmailJob> findByStatus(int status);
}
