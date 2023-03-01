package com.fpoly.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpoly.datn.entity.Config;

@Repository
public interface ConfigRepo extends JpaRepository<Config, Long>{
	public Config getByName(String name);
}
