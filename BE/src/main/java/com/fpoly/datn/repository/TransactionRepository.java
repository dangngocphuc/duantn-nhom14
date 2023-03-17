package com.fpoly.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpoly.datn.entity.Transactions;

public interface TransactionRepository extends JpaRepository<Transactions, Long>{

}
