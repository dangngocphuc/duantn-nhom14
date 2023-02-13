package com.example.DaPhone.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.springframework.stereotype.Component;

import com.example.DaPhone.Common.CommonUtils;
import com.example.DaPhone.Entity.Bill;
import com.example.DaPhone.Entity.Brand;
import com.example.DaPhone.Entity.Category;
import com.example.DaPhone.Entity.Product;
import com.example.DaPhone.Entity.Review;
import com.example.DaPhone.Entity.User;
import com.example.DaPhone.Repository.BillRepo;
import com.example.DaPhone.Repository.BrandRepo;
import com.example.DaPhone.Repository.CategoryRepo;
import com.example.DaPhone.Repository.ProductRepo;
import com.example.DaPhone.Repository.ReviewRepo;
import com.example.DaPhone.Repository.UserRepo;
import com.example.DaPhone.Request.BillRequest;
import com.example.DaPhone.Request.BrandRequest;
import com.example.DaPhone.Request.CategoryRequest;
import com.example.DaPhone.Request.ProductRequest;
import com.example.DaPhone.Request.ReviewParam;
import com.example.DaPhone.Request.UserRequest;

@Component
public class ServiceAdmin {
	@Autowired
	private BrandRepo brandRepo;
	@Autowired
	private CategoryRepo categoryRepo;
	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private BillRepo billRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ReviewRepo reviewRepo;

	@SuppressWarnings("serial")
	public Page<Brand> findBrand(BrandRequest brandParam, Pageable pageable) {

		Page<Brand> listPage = brandRepo.findAll(new Specification<Brand>() {
			@Override
			public Predicate toPredicate(Root<Brand> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return listPage;
	}

	@SuppressWarnings("serial")
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
	public Page<User> findUser(UserRequest userParam, Pageable pageable) {

		Page<User> listPage = userRepo.findAll(new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				if (userParam.getUserName() != null) {
					predicates.add(cb.and(cb.like(cb.upper(root.get("userName")),
							"%" + userParam.getUserName().trim().toUpperCase() + "%")));
				}
				if (userParam.getEmail() != null) {
					predicates.add(cb.and(cb.like(cb.upper(root.get("userEmail")),
							"%" + userParam.getEmail().trim().toUpperCase() + "%")));
				}
				if (userParam.getPhone() > 0) {
					predicates.add(cb.and(cb.equal(root.get("userPhone"), userParam.getPhone())));
				}

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return listPage;
	}

	@SuppressWarnings("serial")
	public Page<Product> findProduct(ProductRequest productParam, Pageable pageable) {

		Page<Product> listPage = productRepo.findAll(new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Join<Product, Category> cateJoin = root.join("category", JoinType.LEFT);
				Join<Product, Brand> brandJoin = root.join("brand", JoinType.LEFT);
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				if (productParam.getProductID() > 0) {
					predicates.add(
							cb.and(cb.equal(root.get("productID"), productParam.getProductID())));
				}
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
				if (productParam.isInventory()) {
					predicates.add(cb.and(cb.greaterThan(root.get("productQuantily"), 0)));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return listPage;
	}

	public List<Long> statisticBillByWeek() {
		List<Long> statistic = new ArrayList<Long>();
		Map<String, List<Date>> dateMap = CommonUtils.getWeeksOfMonth();
		int sizeMonth = dateMap.size();
		int page = 0;
		int size = 100;

		Sort sortable = Sort.by("billID").descending();
		Pageable pageable = PageRequest.of(page, size, sortable);
		BillRequest billParam = new BillRequest();
		for (int i = 1; i <= sizeMonth; i++) {

			List<Date> dates = dateMap.get("Week" + i);
			billParam.setFromDate(dates.get(0));
			billParam.setToDate(dates.get(dates.size() - 1));
			List<Bill> lists = findBill(billParam, pageable).toList();
			long total = 0;
			for (Bill bill : lists) {
				total += bill.getTotal();
			}
			statistic.add(total);

		}
		return statistic;
	}

	public ByteArrayInputStream exportExcel(BillRequest billParam) throws IOException {
		String[] COLUMNs = { "STT", "Mã Bill", "Khách hàng", "Thanh toán", "Tổng đơn", "Địa chỉ", "Ngày",
				"Số điện thoại", "Tình trạng" };
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

			Sort sortable = null;
			if (billParam.getSortField() != null && !billParam.getSortField().equalsIgnoreCase("null")) {
				if (billParam.getSortOrder().equals("ascend")) {
					sortable = Sort.by(billParam.getSortField()).ascending();
				}
				if (billParam.getSortOrder().equals("descend")) {
					sortable = Sort.by(billParam.getSortField()).descending();
				}
			} else {
				sortable = Sort.by("billID").descending();
			}

			Pageable pageable = PageRequest.of(page, size, sortable);
			List<Bill> lists = findBill(billParam, pageable).toList();
			int count = 0;
			for (Bill bill : lists) {
				Row row = sheet.createRow(rowIdx++);
				++count;
				row.createCell(0).setCellValue(count);
				row.createCell(1).setCellValue(bill.getBillID() > 0 ? Long.toString(bill.getBillID()) : "-");
				row.createCell(2)
						.setCellValue(bill.getUser().getUsername() != null ? bill.getUser().getUsername() : " ");
				row.createCell(3).setCellValue(bill.getPayment() != null ? bill.getPayment() : " ");
				row.createCell(4)
						.setCellValue(bill.getTotal() > 0 ? NumberFormat.getInstance().format(bill.getTotal()) : "-");
				row.createCell(5).setCellValue(bill.getAddress() != null ? bill.getAddress() : " ");
				row.createCell(6).setCellValue(
						bill.getDate() != null ? CommonUtils.StringFormatDate(bill.getDate(), "dd/MM/yyyy") : " ");
				row.createCell(7).setCellValue(bill.getPhone() != null ? bill.getPhone() : " ");
				row.createCell(8).setCellValue(bill.getStatus() != null ? bill.getStatus() : " ");
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}
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
//				row.createCell(0).setCellValue(count);
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
	@SuppressWarnings("serial")
	public Page<Review> findReview(ReviewParam reviewParam, Pageable pageable) {

		Page<Review> listPage = reviewRepo.findAll(new Specification<Review>() {
			@Override
			public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				if (reviewParam.getProductName() != null) {
					predicates.add(cb.and(cb.like(cb.upper(root.get("product").<String>get("productName")),
							"%" + reviewParam.getProductName().trim().toUpperCase() + "%")));
				}
				if (reviewParam.getReviewStar() > 0) {
					predicates.add(
							cb.and(cb.equal(root.<String>get("reviewStar"), reviewParam.getReviewStar())));
				}
				if (reviewParam.getReviewName() != null) {
					predicates.add(cb.and(cb.like(cb.upper(root.<String>get("reviewName")),
							"%" + reviewParam.getReviewName().trim().toUpperCase() + "%")));
				}
				if (reviewParam.getStatus() != null) {
					predicates.add(cb.and(cb.equal(root.get("status"), reviewParam.getStatus())));
				}
				
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return listPage;
	}
}
