package com.fpoly.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpoly.datn.entity.Admin;

@Repository
public interface UserAdminRepo extends JpaRepository<Admin, Long> {

}
