package com.example.DaPhone.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.DaPhone.Entity.Rom;

@Repository
public interface RomRepository extends JpaRepository<Rom, Long>, JpaSpecificationExecutor<Rom>{
	
}
