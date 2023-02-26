package com.example.DaPhone.Controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.DaPhone.Common.CommonUtils;
import com.example.DaPhone.Entity.Option;
import com.example.DaPhone.Entity.Product;
import com.example.DaPhone.Entity.ProductDetail;
import com.example.DaPhone.Entity.ProductOption;
import com.example.DaPhone.Model.OptionRequest;
import com.example.DaPhone.Model.Response;
import com.example.DaPhone.Request.ProductRequest;
import com.example.DaPhone.Service.ProductService;

@RestController
@RequestMapping(path = "/api/product")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable(name = "id") Long id) {
		Product pro = productService.findById(id);
		
		for(ProductDetail po:  pro.getListProductDetail()) {
			po.setProduct(null);
//			po.getOption().getListOptionValue().clear();
		}
		pro.setListProductDetail(null);
//		for(ProductOption po:  pro.getListProductOption()) {
//			po.setProduct(null);
//			if(po.getOption() != null) {
//				po.getOption().getListOptionValue().clear();
//			}
//			
//		}
		return new ResponseEntity<Product>(pro, HttpStatus.OK);
	}
	
	@GetMapping(value = "/ngselect")
	public ResponseEntity<List<Product>> ngSelect(Pageable pageable, ProductRequest productRequest) {
		List<Product> lstProduct = productService.ngSelect(pageable,productRequest);
		return new ResponseEntity<List<Product>>(lstProduct, HttpStatus.OK);
	}

	@GetMapping(value = "/export")
	public ResponseEntity<InputStreamResource> exportProduct() {
		ByteArrayInputStream in;
		try {
			ProductRequest ProductRequest = new ProductRequest();
			in = productService.exportExcelProduct(ProductRequest);
			HttpHeaders headers = new HttpHeaders();
			String date = CommonUtils.StringFormatDate(new Date(), "dd/MM/yyyy");
			headers.add("Content-Disposition", "attachment; filename=BaoCao" + date + ".xlsx");
			return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping(value = "/statistic-brand")
	public ResponseEntity<Response<List<Long>>> getSatisticBrand() {
		List<Long> statics = productService.getSatisticBrand();
		
		return new ResponseEntity<Response<List<Long>>>(new Response<List<Long>>(statics), HttpStatus.OK);
	}

	@GetMapping(value = "/statistic-category")
	public ResponseEntity<Response<List<Long>>> getSatisticCategory() {
		List<Long> statics = productService.getSatisticCategory();
		return new ResponseEntity<Response<List<Long>>>(new Response<List<Long>>(statics), HttpStatus.OK);
	}

	@GetMapping(value = "")
//	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Page<Product>> getProducts(Pageable pageable,ProductRequest productRequest) {
		Page<Product> pageBrandPage = productService.findProduct(productRequest, pageable);
		return new ResponseEntity<Page<Product>>(pageBrandPage, HttpStatus.OK);
	}
	@GetMapping(value = "/search")
	public ResponseEntity<List<Product>> getProductSeach(ProductRequest productRequest) {
		int page = productRequest.getPageIndex() - 1;
		int size = productRequest.getPageSize();
		Sort sortable = null;
		if (productRequest.getSortField() != null && !productRequest.getSortField().equalsIgnoreCase("null")) {
			if (productRequest.getSortOrder().equals("ascend")) {
				sortable = Sort.by(productRequest.getSortField()).ascending();
			}
			if (productRequest.getSortOrder().equals("descend")) {
				sortable = Sort.by(productRequest.getSortField()).descending();
			}
		} else {
			sortable = Sort.by("id").descending();
		}
		Pageable pageable = PageRequest.of(page, size, sortable);
		Page<Product> pageBrandPage = productService.findProduct(productRequest, pageable);
		List<Product> lists = pageBrandPage.toList();
		return new ResponseEntity<List<Product>>(lists, HttpStatus.OK);
	}

	@PostMapping(value = "/save")
//	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Boolean> saveProduct(@RequestBody Product product) {
		if (product != null) {
			return new ResponseEntity<Boolean>(productService.saveProduct(product), HttpStatus.OK);
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}
	
	// update image
		@RequestMapping(path = "/upload_image/{id}", method = RequestMethod.POST, consumes = { "multipart/form-data" })
		public ResponseEntity<Response<String>> fileUpload(@PathVariable(name = "id") Long id,
				@RequestParam("file") MultipartFile multipartFile) {
			String rootFileUpload = CommonUtils.ROOT_IMAGES_BACKEND;
			String rootFileUpload1 = CommonUtils.ROOT_IMAGES_FRONTEND;
//			String rootFileUpload = "/home/app/bidv_run";
			Product product = productService.findById(id);

			if (product != null) {
				String originalFilename = multipartFile.getOriginalFilename();
//				product.setProductImage("images/" + originalFilename);
				File file = new File(rootFileUpload + originalFilename);
				if (file.getParentFile().exists() == false) {
					file.getParentFile().mkdirs();
				}
				File file1 = new File(rootFileUpload1 + originalFilename);
				if (file1.getParentFile().exists() == false) {
					file1.getParentFile().mkdirs();
				}
				try {
					try (InputStream is = multipartFile.getInputStream()) {
						try (OutputStream os = new FileOutputStream(file)) {
							byte[] b = new byte[10240];
							int size = 0;
							while ((size = is.read(b)) != -1) {
								os.write(b, 0, size);
							}
						}

						is.close();
					}
					try (InputStream is1 = multipartFile.getInputStream()) {
						try (OutputStream os = new FileOutputStream(file1)) {
							byte[] b = new byte[10240];
							int size = 0;
							while ((size = is1.read(b)) != -1) {
								os.write(b, 0, size);
							}
						}
						is1.close();
					}
					// save product
					productService.saveProduct(product);
					return new ResponseEntity<Response<String>>(new Response<String>("Update thành công"), HttpStatus.OK);
				} catch (IOException e) {
					e.printStackTrace();
					return new ResponseEntity<Response<String>>(new Response<String>("Có lỗi"), HttpStatus.BAD_REQUEST);
				}
			}
			return ResponseEntity.notFound().build();
		}

	@DeleteMapping(value = "/delete/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response<Product>> deleteProduct(@PathVariable(name = "id") Long id) {
		productService.deleteProduct(id);
		return new ResponseEntity<Response<Product>>(new Response<Product>("xoa thanh cong", "200"), HttpStatus.OK);
	}
	


}
