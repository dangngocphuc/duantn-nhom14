package com.example.DaPhone.ServiceImpl;

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

import com.example.DaPhone.Entity.Category;
import com.example.DaPhone.Repository.CategoryRepo;
import com.example.DaPhone.Request.CategoryRequest;
import com.example.DaPhone.Service.CategoryService;
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
