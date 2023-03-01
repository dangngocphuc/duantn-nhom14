package com.fpoly.datn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpoly.datn.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

	Page<User> findAll(Specification<User> specification, Pageable pageable);

//	Optional<User> findByEmail(String email);

	List<User> findByUserEmail(String userEmail);

	Optional<User> findByUserName(String username);

	Boolean existsByUserName(String username);

	Boolean existsByUserEmail(String email);

}
