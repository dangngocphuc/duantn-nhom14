package com.fpoly.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpoly.datn.entity.Category;
import com.fpoly.datn.entity.Contact;
@Repository
public interface ContactRepo  extends JpaRepository<Contact, Long>{

}
