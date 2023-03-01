package com.fpoly.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.fpoly.datn.entity.Colr;

public interface ColrRepo extends JpaRepository<Colr, Long>, JpaSpecificationExecutor<Colr> {
}
