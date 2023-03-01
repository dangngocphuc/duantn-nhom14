package com.fpoly.datn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.datn.common.CommonUtils;
import com.fpoly.datn.entity.Review;
import com.fpoly.datn.model.Response;
import com.fpoly.datn.repository.BillDetailRepo;
import com.fpoly.datn.repository.BillRepo;
import com.fpoly.datn.repository.BrandRepo;
import com.fpoly.datn.repository.CategoryRepo;
import com.fpoly.datn.repository.ContactRepo;
import com.fpoly.datn.repository.ProductRepo;
import com.fpoly.datn.repository.ReviewRepo;
import com.fpoly.datn.repository.UserAdminRepo;
import com.fpoly.datn.repository.UserRepo;
import com.fpoly.datn.request.ReviewParam;
import com.fpoly.datn.service.ServiceAdmin;

@RestController
@RequestMapping(path = "/api/admin/v1")
public class AdminController {
	@Autowired
	private UserAdminRepo userAdminRepo;
	@Autowired
	private CommonUtils commonUtils;
	@Autowired
	private BillDetailRepo billDetailRepo;
	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private BrandRepo brandRepo;
	@Autowired
	private CategoryRepo categoryRepo;
	@Autowired
	private ServiceAdmin serviceAdmin;
	@Autowired
	private BillRepo billRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ReviewRepo reviewRepo;
	@Autowired
	private ContactRepo contactRepo;

	// review service
	@GetMapping(value = "/reviews")
	@PreAuthorize("hasAuthority('ADMIN')")
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
			sortable = Sort.by("reviewID").descending();
		}
		Pageable pageable = PageRequest.of(page, size, sortable);
		Page<Review> pageBrandPage = serviceAdmin.findReview(reviewParam, pageable);
		List<Review> lists = pageBrandPage.toList();
		Long count = (long) pageBrandPage.getTotalElements();
		return new ResponseEntity<Response<Review>>(new Response<Review>(count, lists), HttpStatus.OK);
	}

	@PostMapping(value = "/review")
	public ResponseEntity<Response<Review>> saveReview(@RequestBody Review review) {
		if (review != null) {
			Review review2 = reviewRepo.save(review);
			return new ResponseEntity<Response<Review>>(new Response<Review>(review2), HttpStatus.OK);
		}
		return new ResponseEntity<Response<Review>>(new Response<Review>("loi", "10001"), HttpStatus.OK);
	}

	@DeleteMapping(value = "/review/{id}")
	public ResponseEntity<Response<Review>> deleteReview(@PathVariable(name = "id") Long id) {
		reviewRepo.deleteById(id);
		return new ResponseEntity<Response<Review>>(new Response<Review>("xoa thanh cong", "200"), HttpStatus.OK);
	}

	// contact service

}
