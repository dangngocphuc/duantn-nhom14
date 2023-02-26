package com.example.DaPhone.Service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.example.DaPhone.Entity.Bill;
import com.example.DaPhone.Entity.Brand;
import com.example.DaPhone.Entity.Category;
import com.example.DaPhone.Entity.Product;
import com.example.DaPhone.Entity.Review;
import com.example.DaPhone.Entity.User;
import com.example.DaPhone.Repository.BillRepo;
import com.example.DaPhone.Repository.ProductRepo;
import com.example.DaPhone.Repository.ReviewRepo;
import com.example.DaPhone.Repository.UserRepo;
import com.example.DaPhone.Request.BillRequest;
import com.example.DaPhone.Request.ProductRequest;
import com.example.DaPhone.Request.ReviewParam;

@Component
public class ServiceUser {
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private BillRepo billRepo;

	@Autowired
	private ReviewRepo reviewRepo;

	@SuppressWarnings("serial")
	public Page<Product> findProduct(ProductRequest productParam, Pageable pageable) {

		Page<Product> listPage = productRepo.findAll(new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Join<Product, Category> cateJoin = root.join("category", JoinType.LEFT);
				Join<Product, Brand> brandJoin = root.join("brand", JoinType.LEFT);
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				if (productParam.getCategoryID() > 0) {
					predicates.add(
							cb.and(cb.equal(root.get("category").get("categoryID"), productParam.getCategoryID())));
				}
				if (productParam.getBrandID() > 0) {
					predicates.add(cb.and(cb.equal(root.get("brand").get("brandID"), productParam.getBrandID())));
				}
				if (productParam.getProductName() != null) {
					predicates.add(cb.and(cb.like(cb.upper(root.<String>get("productName")),
							"%" + productParam.getProductName().trim().toUpperCase() + "%")));
				}
				if (productParam.getPriceFrom() > 0) {
					predicates.add(
							cb.and(cb.greaterThanOrEqualTo(root.get("productPrice"), productParam.getPriceFrom())));
				}
				if (productParam.getPriceTo() > 0) {
					predicates.add(cb.and(cb.lessThanOrEqualTo(root.get("productPrice"), productParam.getPriceTo())));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return listPage;
	}

	@SuppressWarnings("serial")
	public Page<Bill> findBill(BillRequest billParam, Pageable pageable) {

		Page<Bill> listPage = billRepo.findAll(new Specification<Bill>() {
			@Override
			public Predicate toPredicate(Root<Bill> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Join<Bill, User> usJoin = root.join("user", JoinType.LEFT);

				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				if (billParam.getBillID() > 0) {
					predicates.add(cb.and(cb.equal(root.get("billID"), billParam.getBillID())));
				}
				if (billParam.getUserId() > 0) {
					predicates.add(cb.and(cb.equal(root.get("user").<String>get("userID"), billParam.getUserId())));
				}
				if (billParam.getUserName() != null) {
					predicates.add(cb.and(cb.like(cb.upper(root.get("user").<String>get("userName")),
							"%" + billParam.getUserName().trim().toUpperCase() + "%")));
				}
				if (billParam.getPriceFrom() > 0) {
					predicates.add(cb.and(cb.greaterThanOrEqualTo(root.get("total"), billParam.getPriceFrom())));
				}
				if (billParam.getPriceTo() > 0) {
					predicates.add(cb.and(cb.lessThanOrEqualTo(root.get("total"), billParam.getPriceTo())));
				}
				if (billParam.getFromDate() != null) {
					predicates.add(cb.and(cb.greaterThanOrEqualTo(root.get("date"), billParam.getFromDate())));
				}
				if (billParam.getToDate() != null) {
					predicates.add(cb.and(cb.lessThanOrEqualTo(root.get("date"), billParam.getToDate())));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return listPage;
	}

	@SuppressWarnings("serial")
	public Page<Review> findReview(ReviewParam reviewParam, Pageable pageable) {
		Page<Review> listPage = reviewRepo.findAll(new Specification<Review>() {
			@Override
			public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				if (reviewParam.getProductId() > 0) {
					predicates.add(
							cb.and(cb.equal(root.get("productDetail").<String>get("id"), reviewParam.getProductId())));
				}
				if (reviewParam.getStatus() != null) {
					predicates.add(cb.and(cb.equal(root.get("status"), reviewParam.getStatus())));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);

		for (Review e : listPage.getContent()) {
			if (e.getProductDetail() != null) {
				e.getProductDetail().setListImei(null);
//				e.getProductDetail().setListProductDetailValue(null);
				e.getProductDetail().setProduct(null);
			}
		}

		return listPage;
	}
}
