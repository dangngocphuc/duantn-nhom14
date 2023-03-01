package com.fpoly.datn.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpoly.datn.entity.Category;
import com.fpoly.datn.request.CategoryRequest;
@Service
public interface CategoryService {
	public Page<Category> findCategory(CategoryRequest categoryParam, Pageable pageable);
	public List<Category> findAll();
	public Category saveCategory(Category category);
	public void deleteCategory(Long id);
}
