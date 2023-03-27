package com.example.DaPhone.Repository;

import com.example.DaPhone.Entity.OperatingSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface OperatingSystemRepo extends JpaRepository<OperatingSystem, Long>, JpaSpecificationExecutor<OperatingSystem> {
}
