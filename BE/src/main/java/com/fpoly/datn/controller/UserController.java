package com.fpoly.datn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.datn.auth.UserDetail;
import com.fpoly.datn.dto.UserDTO;
import com.fpoly.datn.entity.Review;
import com.fpoly.datn.entity.User;
import com.fpoly.datn.model.Response;
import com.fpoly.datn.repository.ReviewRepo;
import com.fpoly.datn.repository.UserRepo;
import com.fpoly.datn.request.ReviewParam;
import com.fpoly.datn.request.UserRequest;
import com.fpoly.datn.service.ServiceUser;
import com.fpoly.datn.service.UserService;
@RestController
@RequestMapping(path = "/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ReviewRepo reviewRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ServiceUser serviceUser;
	
	
	@GetMapping(value = "")
	public ResponseEntity<Response<User>> getUsers(UserRequest userParam) {
		int page = userParam.getPageIndex() - 1;
		int size = userParam.getPageSize();
		Sort sortable = null;
		if (userParam.getSortField() != null && !userParam.getSortField().equalsIgnoreCase("null")) {
			if (userParam.getSortOrder().equals("ascend")) {
				sortable = Sort.by(userParam.getSortField()).ascending();
			}
			if (userParam.getSortOrder().equals("descend")) {
				sortable = Sort.by(userParam.getSortField()).descending();
			}
		} else {
			sortable = Sort.by("userID").descending();
		}
		Pageable pageable = PageRequest.of(page, size, sortable);
		Page<User> pageBrandPage = userService.findUser(userParam, pageable);
		List<User> lists = pageBrandPage.toList();
		Long count = (long) pageBrandPage.getTotalElements();
		return new ResponseEntity<Response<User>>(new Response<User>(count, lists), HttpStatus.OK);
	}

	@PutMapping(value = "")
	public ResponseEntity<Boolean> updateUser(@RequestBody UserDTO user) {
		Boolean res = userService.updateUser(user);
		return new ResponseEntity<Boolean>(res, HttpStatus.OK);
	}
	
	@PostMapping(value = "")
	public ResponseEntity<Response<User>> saveUser(@RequestBody User user) {
		if (user != null) {
			if (userRepo.existsByUserName(user.getUsername())) {
				return new ResponseEntity<Response<User>>(new Response<User>("10002","Username is already taken!"),HttpStatus.BAD_REQUEST);
			}
			if (userRepo.existsByUserEmail(user.getUserEmail())) {
				return new ResponseEntity<Response<User>>(new Response<User>("10003","Email is already in use!"),HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<Response<User>>(new Response<User>(userService.saveUser(user)), HttpStatus.OK);
		}
		return new ResponseEntity<Response<User>>(new Response<User>("loi", "10001"), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<UserDetail>> getUser(@PathVariable(name = "id") Long id) {
		User user = userService.findUserById(id);
		UserDetail userDetail = new UserDetail();
		userDetail.setUsername(user.getUsername());
		userDetail.setUserID(user.getUserID());
		userDetail.setPhoneNumber(user.getUserPhone().toString());
		userDetail.setListAddress(user.getListAddress());
		return new ResponseEntity<Response<UserDetail>>(new Response<UserDetail>(userDetail), HttpStatus.OK);
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<Response<User>> deleteUser(@PathVariable(name = "id") Long id) {
		userService.deleteUser(id);
		return new ResponseEntity<Response<User>>(new Response<User>("xoa thanh cong", "200"), HttpStatus.OK);
	}
	
	@PostMapping(value = "/review")
	public ResponseEntity<Response<Review>> saveReview(@RequestBody Review review) {
		if (review != null) {
			review.setStatus(1L);
			Review review2 = reviewRepo.save(review);
			return new ResponseEntity<Response<Review>>(new Response<Review>(review2), HttpStatus.OK);
		}
		return new ResponseEntity<Response<Review>>(new Response<Review>("loi", "10001"), HttpStatus.OK);
	}
	
	@GetMapping(value = "/reviews")
	public ResponseEntity<Response<Review>> getReviews(ReviewParam reviewParam) {
		int page = reviewParam.getPageIndex() - 1;
		int size = reviewParam.getPageSize();
		Sort sortable = null;
		if (reviewParam.getSortField() != null && !reviewParam.getSortField().equalsIgnoreCase("null")) {
			if (reviewParam.getSortOrder().equals("ascend")) {
				sortable = Sort.by(reviewParam.getSortField()).ascending();
			}
			if (reviewParam.getSortOrder().equals("descend")) {
				sortable = Sort.by(reviewParam.getSortField()).descending();
			}
		} else {
			sortable = Sort.by("id").descending();
		}
		Pageable pageable = PageRequest.of(page, size, sortable);
		Page<Review> pageBrandPage = serviceUser.findReview(reviewParam, pageable);
		List<Review> lists = pageBrandPage.toList();
		Long count = (long) pageBrandPage.getTotalElements();
		return new ResponseEntity<Response<Review>>(new Response<Review>(count, lists), HttpStatus.OK);
	}
	
	
	
	
	
}
