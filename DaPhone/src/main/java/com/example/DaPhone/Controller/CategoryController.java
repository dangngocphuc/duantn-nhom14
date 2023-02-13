package com.example.DaPhone.Controller;

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

import com.example.DaPhone.Entity.Category;
import com.example.DaPhone.Model.Response;
import com.example.DaPhone.Request.CategoryRequest;
import com.example.DaPhone.Service.CategoryService;
@RestController
@RequestMapping(path = "/api/category")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping(value = "")
	public ResponseEntity<Response<Category>> getCategories(CategoryRequest categoryParam) {
		int page = categoryParam.getPageIndex() - 1;
		int size = categoryParam.getPageSize();
		Sort sortable = null;
		if (categoryParam.getSortField() != null && !categoryParam.getSortField().equalsIgnoreCase("null")) {
			if (categoryParam.getSortOrder().equals("ascend")) {
				sortable = Sort.by(categoryParam.getSortField()).ascending();
			}
			if (categoryParam.getSortOrder().equals("descend")) {
				sortable = Sort.by(categoryParam.getSortField()).descending();
			}
		} else {
			sortable = Sort.by("categoryID").descending();
		}
		Pageable pageable = PageRequest.of(page, size, sortable);
		Page<Category> pageBrandPage = categoryService.findCategory(null, pageable);
		List<Category> lists = pageBrandPage.toList();
		Long count = (long) pageBrandPage.getTotalElements();
		return new ResponseEntity<Response<Category>>(new Response<Category>(count, lists), HttpStatus.OK);
	}

	
	@GetMapping(value = "/all")
	public ResponseEntity<Response<Category>> getCategoryAll() {
		List<Category> lists = categoryService.findAll();
		Long count = (long) lists.size();
		return new ResponseEntity<Response<Category>>(new Response<Category>(count, lists), HttpStatus.OK);
	}

	@PostMapping(value = "/save")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response<Category>> saveCategory(@RequestBody Category category) {
		if (category != null) {
			Category category2 = categoryService.saveCategory(category);
			return new ResponseEntity<Response<Category>>(new Response<Category>(category2), HttpStatus.OK);
		}
		return new ResponseEntity<Response<Category>>(new Response<Category>("loi", "10001"), HttpStatus.OK);
	}

	@DeleteMapping(value = "/delete/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response<Category>> deleteCategory(@PathVariable(name = "id") Long id) {
		categoryService.deleteCategory(id);
		return new ResponseEntity<Response<Category>>(new Response<Category>("xoa thanh cong", "200"), HttpStatus.OK);
	}
}
