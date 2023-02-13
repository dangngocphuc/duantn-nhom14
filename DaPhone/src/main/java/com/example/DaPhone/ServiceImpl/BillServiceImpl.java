package com.example.DaPhone.ServiceImpl;

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
import org.springframework.stereotype.Service;

import com.example.DaPhone.Common.CommonUtils;
import com.example.DaPhone.Entity.Bill;
import com.example.DaPhone.Entity.BillDetail;
import com.example.DaPhone.Entity.Config;
import com.example.DaPhone.Entity.EmailJob;
import com.example.DaPhone.Entity.Product;
import com.example.DaPhone.Entity.User;
import com.example.DaPhone.Repository.BillDetailRepo;
import com.example.DaPhone.Repository.BillRepo;
import com.example.DaPhone.Repository.ConfigRepo;
import com.example.DaPhone.Repository.EmailJobRepo;
import com.example.DaPhone.Repository.ProductRepo;
import com.example.DaPhone.Request.BillRequest;
import com.example.DaPhone.Service.BillService;
import com.google.gson.Gson;

@Service
public class BillServiceImpl implements BillService{

	@Autowired
	private BillRepo billRepo;
	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private BillDetailRepo billDetailRepo;
	@Autowired
	private ConfigRepo configRepo;
	@Autowired
	private EmailJobRepo emailJobRepo;
	
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
					predicates.add(cb.and(cb.equal(root.get("user").<String>get("userID"),
							billParam.getUserId())));
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
	@Override
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
	
	@Override
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
	
	public Bill paymentBill(Bill bill) {
		Gson g = new Gson();
		bill.setDate(new Date());
		bill.setStatus(CommonUtils.PROCESS);
		Bill billSave = billRepo.save(bill);
		StringBuilder content = new StringBuilder();
		
		content.append("<table width=\"98%\" border=\"1\" cellpadding=\"3\" cellspacing=\"0\">"
				+ "	<thead>"
				+ "	<tr align=\"center\" style=\"font-weight: bold; background-color: #D6DBE9; \">"
				+ "	<td nowrap=\"nowrap\" width=\"30\">STT</td>"
				+ "	<td width=\"50%\">Tên tài liệu</td>"
				+ "	<td width=\"45%\">Ghi chú</td>"
				+ "	</tr>"
				+ "</thead>");

		Product[] products = g.fromJson(bill.getProducts(), Product[].class);

		for (Product product : products) {
			int i = 1;
			BillDetail billDetail = new BillDetail();
			billDetail.setBill(billSave);
//			billDetail.setPrice(product.getProductPrice());
			billDetail.setProduct(product);
//			billDetail.setQuantity(product.getQuanlityBuy());
			content.append("<tr>"
					+ "<td nowrap=\"nowrap\" width=\"30\">");
			content.append(i);
			content.append( "</td>"
					+ "	<td width=\"50%\">");
//			content.append(product.getProductName());
			content.append("</td>"
					+ "	<td width=\"45%\">");
//			content.append(product.getQuanlityBuy());
			content.append("</td>"
					+ "</tr>");
			// save bill
			billDetailRepo.save(billDetail);
			// save product
//			product.setProductQuantily(product.getProductQuantily() - product.getQuanlityBuy());
			productRepo.save(product);
			i++;
		}
		content.append("</table>");
		// save to email job
		EmailJob emailJob = new EmailJob();
		String subject = configRepo.getByName("subject").getValue().replace("__idBill__",
				String.valueOf(billSave.getBillID()));
		emailJob.setSubject(subject);
		emailJob.setUser(bill.getUser());
		
		Config config = configRepo.getByName("content");
		
		emailJob.setContent(config.getValue().replace("__name__", billSave.getName())
						.replace("__total__",NumberFormat.getInstance().format(bill.getTotal())).replace("__content__", content.toString()));
		emailJobRepo.save(emailJob);
		
		return billSave;
	}
	public void cancelBill(Bill bill) {
		bill.setStatus(CommonUtils.CANCEL);
		billRepo.save(bill);
		List<BillDetail> billDetails = billDetailRepo.findByBill(bill);
		for (BillDetail billDetail : billDetails) {
//			Product product = productRepo.findByProductID(billDetail.getProduct().getProductID());
//			product.setProductQuantily(product.getProductQuantily() + billDetail.getQuantity());
//			productRepo.save(product);
		}
	}
	public Bill saveBill(Bill bill) {
		return billRepo.save(bill);
	}
	public void deleteBill(Long id) {
		billRepo.deleteById(id);
	}
}
