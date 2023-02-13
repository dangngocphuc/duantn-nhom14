package com.example.DaPhone.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.DaPhone.Entity.Admin;

@Repository
public interface UserAdminRepo extends JpaRepository<Admin, Long> {

}
