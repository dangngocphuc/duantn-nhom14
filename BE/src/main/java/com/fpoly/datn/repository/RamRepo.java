package com.fpoly.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.fpoly.datn.entity.Ram;
@Repository
public interface RamRepo extends JpaRepository<Ram, Long>, JpaSpecificationExecutor<Ram>{

}
