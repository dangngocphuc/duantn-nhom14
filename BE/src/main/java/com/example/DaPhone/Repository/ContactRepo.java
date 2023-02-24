package com.example.DaPhone.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.DaPhone.Entity.Category;
import com.example.DaPhone.Entity.Contact;
@Repository
public interface ContactRepo  extends JpaRepository<Contact, Long>{

}
