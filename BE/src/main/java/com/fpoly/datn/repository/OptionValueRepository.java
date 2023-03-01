package com.fpoly.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.fpoly.datn.entity.OptionValue;

@Repository
public interface OptionValueRepository extends JpaRepository<OptionValue, Long>, JpaSpecificationExecutor<OptionValue> {

}