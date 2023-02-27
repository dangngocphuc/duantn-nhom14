package com.example.DaPhone.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DaPhone.Entity.ProductDetail;
import com.example.DaPhone.Entity.ProductDetailValue;
import com.example.DaPhone.Model.CompareResponse;
import com.example.DaPhone.Model.Response;
import com.example.DaPhone.Request.ProductDetailRequest;
import com.example.DaPhone.Service.ProductDetailService;

@RestController
@RequestMapping(path = "/api/product/detail")
public class ProductDetailController {

	@Autowired
	private ProductDetailService productService;

	@GetMapping(value = "/search")
	public ResponseEntity<Page<ProductDetail>> getProductSeach(ProductDetailRequest request) {
//		int page = 0;
//		int size = 5;
		Sort sortable = null;
		if (request.getSortField() != null && !request.getSortField().equalsIgnoreCase("null")) {
			if (request.getSortOrder().equals("ascend")) {
				sortable = Sort.by(request.getSortField()).ascending();
			}
			if (request.getSortOrder().equals("descend")) {
				sortable = Sort.by(request.getSortField()).descending();
			}
		} else {
			sortable = Sort.by("id").descending();
		}
		Pageable pageable = PageRequest.of(request.getPageIndex(), request.getPageSize(), sortable);

		Page<ProductDetail> pageBrandPage = productService.findProduct(request, pageable);
//		List<Product> lists = pageBrandPage.toList();
		return new ResponseEntity<Page<ProductDetail>>(pageBrandPage, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<ProductDetail>> getProduct(@PathVariable(name = "id") Long id) {
		ProductDetail productDetail = productService.findById(id);
		productDetail.getProduct().setListProductDetail(null);
		return new ResponseEntity<Response<ProductDetail>>(new Response<ProductDetail>(productDetail), HttpStatus.OK);
	}

	@PostMapping(value = "/save")
	public ResponseEntity<Boolean> saveProduct(@RequestBody ProductDetail productDetail) {
		return new ResponseEntity<Boolean>(productService.saveOrUpdate(productDetail), HttpStatus.OK);
	}
	
	@GetMapping(value = "/list")
	public ResponseEntity<List<ProductDetail>> getListProductDetail() {
		List<ProductDetail> pageBrandPage = productService.getListProduct();
		return new ResponseEntity<List<ProductDetail>>(pageBrandPage, HttpStatus.OK);
	}
	
	@PostMapping(value = "/compare")
	public ResponseEntity<CompareResponse> compareLaptops(@RequestBody List<ProductDetail> lstProductDetails) {
		CompareResponse compareResponse = new CompareResponse();
		compareResponse.setMessage(productService.compareLaptops(lstProductDetails));
		return new ResponseEntity<CompareResponse>(compareResponse, HttpStatus.OK);
	}
}
