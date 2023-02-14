package com.example.DaPhone.Controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DaPhone.Entity.Brand;
import com.example.DaPhone.Model.Response;
import com.example.DaPhone.Request.BrandRequest;
import com.example.DaPhone.Service.BrandService;

@RestController
@RequestMapping(path = "/api/brand")
public class BrandController {
	@Autowired
	private BrandService brandService;
	
	@GetMapping(value = "")
	public ResponseEntity<Page<Brand>> getBrands(Pageable pageable, BrandRequest brandParam) {
//		int page = brandParam.getPageIndex() - 1;
//		int size = brandParam.getPageSize();
//		Sort sortable = null;
//		if (brandParam.getSortField() != null && ! brandParam.getSortField().equalsIgnoreCase("null")) {
//			if (brandParam.getSortOrder().equals("ascend")) {
//				sortable = Sort.by(brandParam.getSortField()).ascending();
//			}
//			if (brandParam.getSortOrder().equals("descend")) {
//				sortable = Sort.by(brandParam.getSortField()).descending();
//			}
//		} else {
//			sortable = Sort.by("brandID").descending();
//		}
//		Pageable pageable = PageRequest.of(page, size, sortable);
		Page<Brand> pageBrandPage = brandService.findBrand(brandParam, pageable);
		return new ResponseEntity<Page<Brand>>(pageBrandPage, HttpStatus.OK);
	}
	
	@GetMapping(value = "/list")
	public ResponseEntity<List<Brand>> getBrandAll() {
		List<Brand> list = brandService.findAll();
//		Long count = (long) list.size();
		return new ResponseEntity<List<Brand>>(list, HttpStatus.OK);
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<Brand> getBrandById(@PathVariable(name = "id") Long id) {
	    Brand brand = brandService.findById(id);
		return new ResponseEntity<Brand>(brand, HttpStatus.OK);
	}
	
	@PostMapping(value = "/save")
	public ResponseEntity<Response<Brand>> saveBrand(@RequestBody Brand brand) {
		if (brand != null) {
			Brand brandSave = brandService.saveBrand(brand);
			return new ResponseEntity<Response<Brand>>(new Response<Brand>(brandSave), HttpStatus.OK);
		}
		return new ResponseEntity<Response<Brand>>(new Response<Brand>("loi", "10001"), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<Response<Brand>> deleteBrand(@PathVariable(name = "id") Long id) {
		brandService.deleteBrand(id);
		return new ResponseEntity<Response<Brand>>(new Response<Brand>("xoa thanh cong", "200"), HttpStatus.OK);
	}
}
