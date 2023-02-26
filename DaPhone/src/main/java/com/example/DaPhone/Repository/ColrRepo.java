package com.example.DaPhone.Repository;

import com.example.DaPhone.Entity.Colr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ColrRepo extends JpaRepository<Colr, Long>, JpaSpecificationExecutor<Colr> {
}
