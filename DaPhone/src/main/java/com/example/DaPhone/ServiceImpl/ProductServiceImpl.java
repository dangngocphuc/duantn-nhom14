package com.example.DaPhone.ServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.DaPhone.Entity.Brand;
import com.example.DaPhone.Entity.Category;
import com.example.DaPhone.Entity.Option;
import com.example.DaPhone.Entity.OptionValue;
import com.example.DaPhone.Entity.Product;
import com.example.DaPhone.Entity.ProductOption;
import com.example.DaPhone.Model.OptionRequest;
import com.example.DaPhone.Repository.BrandRepo;
import com.example.DaPhone.Repository.CategoryRepo;
import com.example.DaPhone.Repository.ProductRepo;
import com.example.DaPhone.Request.ProductRequest;
import com.example.DaPhone.Service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
	private BrandRepo brandRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Override
	public Page<Product> findProduct(ProductRequest productParam, Pageable pageable) {

		Page<Product> listPage = productRepo.findAll(new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//				Join<Product, Category> cateJoin = root.join("category", JoinType.LEFT);
//				Join<Product, Brand> brandJoin = root.join("brand", JoinType.LEFT);
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				if (productParam.getProductID() > 0) {
					predicates.add(
							cb.and(cb.equal(root.get("productID"), productParam.getProductID())));
				}
//				if (productParam.getCategoryID() > 0) {
//					predicates.add(
//							cb.and(cb.equal(root.get("category").get("categoryID"), productParam.getCategoryID())));
//				}
				if (productParam.getBrandID() > 0) {
					predicates.add(cb.and(cb.equal(root.get("brand").get("brandID"), productParam.getBrandID())));
				}
				if (productParam.getProductName() != null && !"".equals(productParam.getProductName())) {
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
				if (productParam.isInventory()) {
					predicates.add(cb.and(cb.greaterThan(root.get("productQuantily"), 0)));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		for (Product pro : listPage) {
		    pro.setListProductDetail(null);
			pro.setListProductOption(null);
		}
		
		return listPage;
	}
	@Override
	public boolean saveProduct(Product product) {
		if (product.getId() != null) {
			Product products = productRepo.findById(product.getId()).orElse(null);
			products.setTenSanPham(product.getTenSanPham());
			products.getListProductOption().clear();
			products.getListProductOption().addAll(product.getListProductOption());
			products.setBrand(product.getBrand());
			for(ProductOption po : product.getListProductOption()) {
				po.setProduct(products);
			}
			Product pro = productRepo.save(products);			
			return true;
		} else { // new
			for(ProductOption po : product.getListProductOption()) {
				po.setProduct(product);
			}
			Product pro = productRepo.save(product);
			return true;
		}
		
		
	}
	@Override
	public void deleteProduct(Long id) {
		productRepo.deleteById(id);
	}
	@Override
	public Product findById(Long id) {
		Product product = productRepo.findById(id).orElse(null);
		return product;
	}
	
	@Override
	public List<Long> getSatisticBrand() {
		List<Long> statics = new ArrayList<Long>();
		List<Brand> lists = brandRepo.findAll();
		for (Brand brand : lists) {
			if ((long) productRepo.findByBrand(brand).size() > 0) {
				statics.add((long) productRepo.findByBrand(brand).size());
			} else {
				statics.add(0L);
			}
		}
		return statics;
	}
	
	@Override
	public List<Long> getSatisticCategory() {
		List<Long> statics = new ArrayList<Long>();
		List<Category> lists = categoryRepo.findAll();
		Long count = (long) productRepo.findAll().size();
//		for (Category category : lists) {
//			if ((long) productRepo.findByCategory(category).size() > 0) {
//				statics.add((long) ((productRepo.findByCategory(category).size() / (double) count) * 100));
//			} else {
//				statics.add(0L);
//			}
//		}
		return statics;
	}
	
	@Override
	public ByteArrayInputStream exportExcelProduct(ProductRequest productParam) throws IOException {
		String[] COLUMNs = { "STT", "Mã SP", "Tên", "Thương hiệu","Thể loại", "Giá bán", "Số lượng" };
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Sheet sheet = workbook.createSheet("Data");
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.BLUE.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			// Row for Header
			Row headerRow = sheet.createRow(0);

			// Header
			for (int col = 0; col < COLUMNs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(COLUMNs[col]);
				cell.setCellStyle(headerCellStyle);
			}

			int rowIdx = 1;
			int page = 0;
			int size = 100;
			productParam.setInventory(true);
			Sort sortable = null;
			if (productParam.getSortField() != null && !productParam.getSortField().equalsIgnoreCase("null")) {
				if (productParam.getSortOrder().equals("ascend")) {
					sortable = Sort.by(productParam.getSortField()).ascending();
				}
				if (productParam.getSortOrder().equals("descend")) {
					sortable = Sort.by(productParam.getSortField()).descending();
				}
			} else {
				sortable = Sort.by("productID").descending();
			}
			Pageable pageable = PageRequest.of(page, size, sortable);
			List<Product> lists = findProduct(productParam, pageable).toList();
			int count = 0;
			for (Product product : lists) {
				Row row = sheet.createRow(rowIdx++);
				++count;
				row.createCell(0).setCellValue(count);
//				row.createCell(1).setCellValue(product.getProductID() > 0 ? Long.toString(product.getProductID()) : "-");
//				row.createCell(2)
//						.setCellValue(product.getProductName() != null ? product.getProductName() : " ");
//				row.createCell(3).setCellValue(product.getBrand().getBrandName() != null ? product.getBrand().getBrandName() : " ");
//				row.createCell(4).setCellValue(product.getCategory().getCategoryName() != null ? product.getCategory().getCategoryName() : " ");
//				row.createCell(5)
//						.setCellValue(product.getProductPrice() > 0 ? NumberFormat.getInstance().format(product.getProductPrice()) : "-");
//				row.createCell(6).setCellValue(
//						product.getProductQuantily() > 0 ? NumberFormat.getInstance().format(product.getProductQuantily()) : "-");
				
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}
	
	
	@Override
	public List<Product> ngSelect(Pageable pageable, ProductRequest request) {
		Page<Product> page = productRepo.findAll(new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (request != null) {
					if (request.getProductName() != null && !request.getProductName().equals("")) {
						predicates.add(criteriaBuilder.or(
								criteriaBuilder.like(criteriaBuilder.upper(root.<String>get("tenSanPham")),
												"%" + request.getProductName().trim().toUpperCase() + "%"),
								criteriaBuilder.like(root.<String>get("maSanPham"),
												"%" + request.getProductName()+ "%")
						));
					}
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		for (Product pro : page) {
		    pro.setListProductDetail(null);
			pro.setListProductOption(null);
		}
		return page.getContent();
	}

}
