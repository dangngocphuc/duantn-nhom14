package com.example.DaPhone.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.DaPhone.Entity.Config;

@Repository
public interface ConfigRepo extends JpaRepository<Config, Long>{
	public Config getByName(String name);
}
