package com.fpoly.datn.repository;

import com.fpoly.datn.entity.OperatingSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface OperatingSystemRepo extends JpaRepository<OperatingSystem, Long>, JpaSpecificationExecutor<OperatingSystem> {
}
