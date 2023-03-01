package com.fpoly.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpoly.datn.entity.Option;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long>, JpaSpecificationExecutor<Option> {
	@Query("Select option From Option option Where option.id = :id")
	Option getById(@Param("id") Long id);
}