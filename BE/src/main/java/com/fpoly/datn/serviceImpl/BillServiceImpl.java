package com.fpoly.datn.serviceImpl;

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
import org.springframework.transaction.annotation.Transactional;

import com.fpoly.datn.common.CommonUtils;
import com.fpoly.datn.entity.Bill;
import com.fpoly.datn.entity.BillDetail;
import com.fpoly.datn.entity.Config;
import com.fpoly.datn.entity.EmailJob;
import com.fpoly.datn.entity.Imei;
import com.fpoly.datn.entity.Promotion;
import com.fpoly.datn.entity.User;
import com.fpoly.datn.repository.BillDetailRepo;
import com.fpoly.datn.repository.BillRepo;
import com.fpoly.datn.repository.ConfigRepo;
import com.fpoly.datn.repository.EmailJobRepo;
import com.fpoly.datn.repository.ImeiRepository;
import com.fpoly.datn.repository.ProductRepo;
import com.fpoly.datn.repository.PromotionRepo;
import com.fpoly.datn.request.BillRequest;
import com.fpoly.datn.service.BillService;
import com.google.gson.Gson;

@Service
public class BillServiceImpl implements BillService {

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
	@Autowired
	private ImeiRepository imeiRepo;
	@Autowired
	private PromotionRepo promotionRepo;

	public Page<Bill> findBill(BillRequest billParam, Pageable pageable) {

		Page<Bill> listPage = billRepo.findAll(new Specification<Bill>() {
			@Override
			public Predicate toPredicate(Root<Bill> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Join<Bill, User> usJoin = root.join("user", JoinType.LEFT);
				query.distinct(true);
				List<Predicate> predicates = new ArrayList<>();
				if (billParam.getBillID() > 0) {
					predicates.add(cb.and(cb.equal(root.get("id"), billParam.getBillID())));
				}
				if (billParam.getUserId() > 0) {
					predicates.add(cb.and(cb.equal(root.get("user").<String>get("userID"), billParam.getUserId())));
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
				if (billParam.getMonth() > 0) {
					predicates.add(
							cb.and(cb.equal(cb.function("MONTH", Long.class, root.get("date")), billParam.getMonth())));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		for (Bill bill : listPage.getContent()) {
			if (bill.getUser() != null) {
				bill.getUser().setListBill(null);
			}
			bill.getListBillDetail().forEach(e -> {
				e.getListImei().forEach(i -> {
					i.setBillDetail(null);
				});
				e.setBill(null);
				e.getProductDetail().setListImei(null);
//				e.getProductDetail().setListProductDetailValue(null);
				e.getProductDetail().setProduct(null);
			});
		}

		return listPage;
	}

	@Override
	public List<Long> statisticBillByWeek() {
		List<Long> statistic = new ArrayList<Long>();
		Map<String, List<Date>> dateMap = CommonUtils.getWeeksOfMonth();
		int sizeMonth = dateMap.size();
		int page = 0;
		int size = 100;

		Sort sortable = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page, size, sortable);
		BillRequest billParam = new BillRequest();
		for (int i = 1; i <= sizeMonth; i++) {
			List<Date> dates = dateMap.get("Week" + i);
			billParam.setFromDate(dates.get(0));
			billParam.setToDate(dates.get(dates.size() - 1));
			List<Bill> lists = findBill(billParam, pageable).toList();
			long total = 0;
			for (Bill bill : lists) {
				if (bill.getTotal() != null) {
					total += bill.getTotal();
				}
			}
			statistic.add(total);
		}
		return statistic;
	}

	@Override
	public List<Long> statisticBillByMonth() {
		List<Long> statistic = new ArrayList<Long>();
		int page = 0;
		int size = 100;
		Sort sortable = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page, size, sortable);
		BillRequest billParam = new BillRequest();
		for (int i = 1; i <= 12; i++) {
			billParam.setMonth(i);
			List<Bill> lists = findBill(billParam, pageable).toList();
//			long total = 0;
//			for (Bill bill : lists) {
//				total += bill.getTotal();
//			}
			statistic.add(Long.valueOf(lists.size()));
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
				sortable = Sort.by("id").descending();
			}

			Pageable pageable = PageRequest.of(page, size, sortable);
			List<Bill> lists = findBill(billParam, pageable).toList();
			int count = 0;
			for (Bill bill : lists) {
				Row row = sheet.createRow(rowIdx++);
				++count;
				row.createCell(0).setCellValue(count);
				row.createCell(1).setCellValue(bill.getId() > 0 ? Long.toString(bill.getId()) : "-");
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

	@Transactional(rollbackFor = Exception.class)
	public boolean paymentBill(Bill bill) {
		try {
			if(bill.getId() != null) {
				Bill bills = billRepo.findById(bill.getId()).orElse(null);
				for (BillDetail billDetail : bills.getListBillDetail()) {
					billDetail.setBill(bill);
					for(Imei imei: billDetail.getListImei()) {
						imei.setBillDetail(null);
//						imei.setProductDetail(billDetail.getProductDetail());
						imei.setStatus(1);
					}
				}	
				bills.getListBillDetail().clear();
				bills.getListBillDetail().addAll(bill.getListBillDetail());
//				products.setTenSanPham(product.getTenSanPham());
//				products.getListProductOption().clear();
//				products.getListProductOption().addAll(product.getListProductOption());
//				bills.getListBillDetail().clear();
				for (BillDetail billDetail : bills.getListBillDetail()) {
					billDetail.setBill(bill);
					for(Imei imei: billDetail.getListImei()) {
						imei.setBillDetail(billDetail);
						imei.setProductDetail(billDetail.getProductDetail());
						imei.setStatus(0);
					}
				}		
				
				return true;	
			}else {
				Gson g = new Gson();
				bill.setDate(new Date());
				bill.setStatus(CommonUtils.WAITING);
				bill.setBillCode(CommonUtils.generateBillNumber());
				for (BillDetail billDetail : bill.getListBillDetail()) {
					billDetail.setBill(bill);
				}
				if(bill.getPromotion() != null) {
					Promotion promotion = promotionRepo.findByCode(bill.getPromotion().getCode());
					if(promotion.getQuantity()> (promotion.getCount()!=null?promotion.getCount():0)) {
						promotion.setCount((promotion.getCount()!=null?promotion.getCount():0)+1);
					}
					promotionRepo.save(promotion);
				}
				Bill billSave = billRepo.save(bill);
				StringBuilder content = new StringBuilder();
				content.append("<table width=\"98%\" border=\"1\" cellpadding=\"3\" cellspacing=\"0\">\r\n"
						+ "        <thead>\r\n"
						+ "            <tr align=\"center\" style=\"font-weight: bold; background-color: #D6DBE9; \">\r\n"
						+ "                <td nowrap=\"nowrap\" width=\"5%\">STT</td>\r\n"
						+ "                <td width=\"55%\">Tên Sản phẩm</td>\r\n"
						+ "                <td width=\"10%\">Số lượng</td>\r\n"
						+ "                <td width=\"30%\">Giá bán</td>\r\n" + "            </tr>\r\n"
						+ "        </thead>");

//				ProductDetail[] products = g.fromJson(bill.getProducts(), ProductDetail[].class);
				int i = 1;
				Double total = 0D;
				for (BillDetail billDetail : bill.getListBillDetail()) {
//					BillDetail billDetail = new BillDetail();
//					billDetail.setBill(billSave);
//					billDetail.setPrice(product.getProductPrice());
//					billDetail.setProduct(product);
//					billDetail.setQuantity(product.getQuanlityBuy());
					total = (billDetail.getPrice().doubleValue() * billDetail.getQuantity());

					content.append("<tr>" + "<td nowrap=\"nowrap\" width=\"5%\" style=\"text-align: center;\">");
					content.append(i);
					content.append("</td>" + "<td width=\"50%\">");
					content.append(billDetail.getProductDetail().getProductName());
					content.append("</td>" + "	<td width=\"10%\" style=\"text-align: center;\" >");
					content.append(billDetail.getQuantity());
					content.append("</td>" + "	<td width=\"30%\" style=\"text-align: right;\">");
					content.append(total.longValue());
					content.append("</td>" + "</tr>");
					// save bill
//					billDetailRepo.save(billDetail);
					// save product
//					product.setProductQuantily(product.getProductQuantily() - product.getQuanlityBuy());
//					productRepo.save(product);
					i++;
				}
				content.append("</table>");
				// save to email job
				EmailJob emailJob = new EmailJob();
				String subject = configRepo.getByName("subject").getValue().replace("__billCode__",
						String.valueOf(billSave.getBillCode()));
				emailJob.setSubject(subject);
				emailJob.setUser(bill.getUser());
				Config config = configRepo.getByName("content");
				emailJob.setContent(config.getValue().replace("__name__", billSave.getName())
						.replace("__total__", NumberFormat.getInstance().format(bill.getTotal()))
						.replace("__content__", content.toString()));
				if (bill.getUser() != null) {
					emailJobRepo.save(emailJob);
				}
			}
//			return bill;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void cancelBill(Long id) {
		Bill bills = billRepo.findById(id).orElse(null);
		bills.setStatus(CommonUtils.CANCEL);
		bills.setPaymentStatus(CommonUtils.PaymentStatus.ERROR.getValue());
		for (BillDetail billDetail : bills.getListBillDetail()) {
//			billDetail.setBill(bill);
			for(Imei imei: billDetail.getListImei()) {
				imei.setBillDetail(null);
//				imei.setProductDetail(billDetail.getProductDetail());
				imei.setStatus(1);
			}
		}	
		billRepo.save(bills);
//		List<BillDetail> billDetails = billDetailRepo.findByBill(bill);
//		for (BillDetail billDetail : billDetails) {
//			Product product = productRepo.findByProductID(billDetail.getProduct().getProductID());
//			product.setProductQuantily(product.getProductQuantily() + billDetail.getQuantity());
//			productRepo.save(product);
//		}
	}

	@Transactional
	public boolean saveBill(Bill bill) {
		Bill bills = billRepo.findById(bill.getId()).get();
		if (bills != null) {
			bills.setStatus(bill.getStatus());
		} else {
			return false;
		}
		return true;
	}

	public void deleteBill(Long id) {
		billRepo.deleteById(id);
	}

	@Override
	public Bill getById(Long id) {
		Bill bills = billRepo.findById(id).get();
		return bills;
	}
}
