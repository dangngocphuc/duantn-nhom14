package com.fpoly.datn.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fpoly.datn.entity.Category;
import com.fpoly.datn.repository.CategoryRepo;
import com.fpoly.datn.request.CategoryRequest;
import com.fpoly.datn.service.CategoryService;
@Service
public class CategoryServiceImpl implements CategoryService{
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Override
	public Page<Category> findCategory(CategoryRequest categoryParam, Pageable pageable) {

		Page<Category> listPage = categoryRepo.findAll(new Specification<Category>() {
			@Override
			public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return listPage;
	}
	@Override
	public List<Category> findAll(){
		return categoryRepo.findAll();
	}
	@Override
	public Category saveCategory(Category category) {
		return categoryRepo.save(category);
	}
	@Override
	public void deleteCategory(Long id) {
		categoryRepo.deleteById(id);
	}
	
}
