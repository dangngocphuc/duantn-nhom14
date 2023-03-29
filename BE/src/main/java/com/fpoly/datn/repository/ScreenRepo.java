package com.fpoly.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.fpoly.datn.entity.Screen;

public interface ScreenRepo extends JpaRepository<Screen, Long>, JpaSpecificationExecutor<Screen> {
}
