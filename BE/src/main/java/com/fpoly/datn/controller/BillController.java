package com.fpoly.datn.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.datn.common.CommonUtils;
import com.fpoly.datn.entity.Bill;
import com.fpoly.datn.entity.BillDetail;
import com.fpoly.datn.entity.Transactions;
import com.fpoly.datn.model.Response;
import com.fpoly.datn.model.ResponseVnpay;
import com.fpoly.datn.model.TransactionRequest;
import com.fpoly.datn.repository.BillRepo;
import com.fpoly.datn.repository.TransactionRepository;
import com.fpoly.datn.request.BillRequest;
import com.fpoly.datn.service.BillService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@RestController
@RequestMapping(path = "/api/bill")
public class BillController {

	@Autowired
	private BillService billService;

	@Autowired
	private BillRepo billRepo;

	@Autowired
	private TransactionRepository transactionRepo;

	// search
	@GetMapping(value = "")
	public ResponseEntity<Response<Bill>> getBills(BillRequest billParam) {
		int page = billParam.getPageIndex() - 1;
		int size = billParam.getPageSize();
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
		Page<Bill> pageBrandPage = billService.findBill(billParam, pageable);
		List<Bill> lists = pageBrandPage.toList();
		Long count = (long) pageBrandPage.getTotalElements();
		return new ResponseEntity<Response<Bill>>(new Response<Bill>(count, lists), HttpStatus.OK);
	}

	// Save bill
	@PostMapping(value = "/save")
	public ResponseEntity<Boolean> saveBill(@RequestBody Bill bill) {
		if (bill != null) {
			Boolean billSave = billService.saveBill(bill);
			return new ResponseEntity<Boolean>(billSave, HttpStatus.OK);
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}

	// Delete bill
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<Response<Bill>> deleteBill(@PathVariable(name = "id") Long id) {
		billService.deleteBill(id);
		return new ResponseEntity<Response<Bill>>(new Response<Bill>("xoa thanh cong", "200"), HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Bill> getBillById(@PathVariable(name = "id") Long id) {
		Bill bill = billService.getById(id);
		return new ResponseEntity<Bill>(bill, HttpStatus.OK);
	}

	// payment service
	@PostMapping(value = "/payment")
	public ResponseEntity<Boolean> paymentBill(@RequestBody Bill bill) {
		if (bill != null) {
			return new ResponseEntity<Boolean>(billService.paymentBill(bill), HttpStatus.OK);
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}

	// Export excel
	@GetMapping(value = "/export")
	public ResponseEntity<InputStreamResource> exportBill(BillRequest billParam) {
		ByteArrayInputStream in;
		try {
			in = billService.exportExcel(billParam);
			HttpHeaders headers = new HttpHeaders();
			String date = CommonUtils.StringFormatDate(new Date(), "dd/MM/yyyy");
			headers.add("Content-Disposition", "attachment; filename=BaoCao" + date + ".xlsx");
			return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.notFound().build();
	}

	// cancel bill
	@GetMapping(value = "/cancel/{id}")
	public ResponseEntity<Response<BillDetail>> cancelBill(@PathVariable(name = "id") Long id) {
		if (id != null) {
			billService.cancelBill(id);
			return new ResponseEntity<Response<BillDetail>>(new Response<BillDetail>("1002", "oke"), HttpStatus.OK);
		}
		return new ResponseEntity<Response<BillDetail>>(new Response<BillDetail>("loi", "10001"), HttpStatus.OK);
	}

	@GetMapping(value = "/statistic/week")
	public ResponseEntity<Response<List<Long>>> getSatisticBillByWeek() {
		List<Long> countList = billService.statisticBillByWeek();
//		List<String> staticList = new ArrayList<String>();
		return new ResponseEntity<Response<List<Long>>>(new Response<List<Long>>(countList), HttpStatus.OK);
	}

	@GetMapping(value = "/statistic/month")
	public ResponseEntity<Response<List<Long>>> getSatisticBillByMonth() {
		List<Long> countList = billService.statisticBillByMonth();
//		List<String> staticList = new ArrayList<String>();
		return new ResponseEntity<Response<List<Long>>>(new Response<List<Long>>(countList), HttpStatus.OK);
	}

	@PostMapping(value = "/payment/vnpay")
	public ResponseEntity<Object> paymentBillByVnpay(@RequestBody Bill bill, HttpServletRequest req,
			HttpServletResponse resp) throws UnsupportedEncodingException, IOException {

		String vnp_Version = "2.1.0";
		String vnp_Command = "pay";

		String orderType = "other";
		String vnp_TxnRef = CommonUtils.generateBillNumber();
		String vnp_IpAddr = CommonUtils.getIpAddress(req);
		String vnp_TmnCode = CommonUtils.vnp_TmnCode;
		String vnp_OrderInfo = "Thanh toan cho don hang " + vnp_TxnRef;

		int amount = (int) (bill.getTotal().doubleValue() * 100);

		Map<String, String> vnp_Params = new HashMap<>();
		vnp_Params.put("vnp_Version", vnp_Version);
		vnp_Params.put("vnp_Command", vnp_Command);
		vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
		vnp_Params.put("vnp_Amount", String.valueOf(amount));
		vnp_Params.put("vnp_CurrCode", "VND");

		String bank_code = "";
		if (bank_code != null && !bank_code.isEmpty()) {
			vnp_Params.put("vnp_BankCode", bank_code);
		}

		vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
		vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
		vnp_Params.put("vnp_OrderType", orderType);

		String locate = req.getParameter("language");
		if (locate != null && !locate.isEmpty()) {
			vnp_Params.put("vnp_Locale", locate);
		} else {
			vnp_Params.put("vnp_Locale", "vn");
		}
		vnp_Params.put("vnp_ReturnUrl", CommonUtils.vnp_Returnurl);
		vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String vnp_CreateDate = formatter.format(cld.getTime());

		vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
		cld.add(Calendar.MINUTE, 15);

		String vnp_ExpireDate = formatter.format(cld.getTime());
		// Add Params of 2.1.0 Version
		vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

		bill.setDate(new Date());
		bill.setStatus(CommonUtils.WAITING);
		bill.setBillCode(CommonUtils.generateBillNumber());
		for (BillDetail billDetail : bill.getListBillDetail()) {
			billDetail.setBill(bill);
		}
		Bill billSave = billRepo.save(bill);

		// Billing
//		vnp_Params.put("vnp_Bill_Mobile", req.getParameter("txt_billing_mobile"));
//		vnp_Params.put("vnp_Bill_Email", req.getParameter("txt_billing_email"));
//		String fullName = (req.getParameter("txt_billing_fullname")).trim();

//		if (fullName != null && !fullName.isEmpty()) {
//			int idx = fullName.indexOf(' ');
//			String firstName = fullName.substring(0, idx);
//			String lastName = fullName.substring(fullName.lastIndexOf(' ') + 1);
//			vnp_Params.put("vnp_Bill_FirstName", firstName);
//			vnp_Params.put("vnp_Bill_LastName", lastName);
//
//		}
//		vnp_Params.put("vnp_Bill_Address", req.getParameter("txt_inv_addr1"));
//		vnp_Params.put("vnp_Bill_City", req.getParameter("txt_bill_city"));
//		vnp_Params.put("vnp_Bill_Country", req.getParameter("txt_bill_country"));
//		if (req.getParameter("txt_bill_state") != null && !req.getParameter("txt_bill_state").isEmpty()) {
//			vnp_Params.put("vnp_Bill_State", req.getParameter("txt_bill_state"));
//		}
		// Invoice
//		vnp_Params.put("vnp_Inv_Phone", req.getParameter("txt_inv_mobile"));
//		vnp_Params.put("vnp_Inv_Email", req.getParameter("txt_inv_email"));
//		vnp_Params.put("vnp_Inv_Customer", req.getParameter("txt_inv_customer"));
//		vnp_Params.put("vnp_Inv_Address", req.getParameter("txt_inv_addr1"));
//		vnp_Params.put("vnp_Inv_Company", req.getParameter("txt_inv_company"));
//		vnp_Params.put("vnp_Inv_Taxcode", req.getParameter("txt_inv_taxcode"));
//		vnp_Params.put("vnp_Inv_Type", req.getParameter("cbo_inv_type"));
		// Build data to hash and querystring
		List fieldNames = new ArrayList(vnp_Params.keySet());
		Collections.sort(fieldNames);
		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Iterator itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = (String) vnp_Params.get(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				// Build hash data
				hashData.append(fieldName);
				hashData.append('=');
				hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				// Build query
				query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
				query.append('=');
				query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				if (itr.hasNext()) {
					query.append('&');
					hashData.append('&');
				}
			}
		}
		String queryUrl = query.toString();
		String vnp_SecureHash = CommonUtils.hmacSHA512(CommonUtils.vnp_HashSecret, hashData.toString());
		queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
		String paymentUrl = CommonUtils.vnp_PayUrl + "?" + queryUrl;
		com.google.gson.JsonObject job = new JsonObject();
		job.addProperty("code", "00");
		job.addProperty("message", "success");
		job.addProperty("data", paymentUrl);
		Gson gson = new Gson();
//		resp.getWriter().write(gson.toJson(job));
		return new ResponseEntity<Object>(gson.toJson(job), HttpStatus.OK);
	}

	@GetMapping(value = "/payment/results")
	public ResponseEntity<ResponseVnpay> showRespond(TransactionRequest transactionRequest,
			HttpServletRequest request) {
		ResponseVnpay responseVnpay = new ResponseVnpay();
		try {

			Map<String, Object> fields = CommonUtils.objectToMap(transactionRequest);
			System.out.println(fields);
			String vnp_SecureHash = transactionRequest.getVnp_SecureHash();
			if (fields.containsKey("vnp_SecureHashType")) {
				fields.remove("vnp_SecureHashType");
			}
			if (fields.containsKey("vnp_SecureHash")) {
				fields.remove("vnp_SecureHash");
			}

			Transactions transactions = new Transactions();
			transactions.setVnp_Amount(transactionRequest.getVnp_Amount());
			transactions.setVnp_BankCode(transactionRequest.getVnp_BankCode());
			transactions.setVnp_CardType(transactionRequest.getVnp_CardType());
			transactions.setVnp_OrderInfo(transactionRequest.getVnp_OrderInfo());
			transactions.setVnp_PayDate(transactionRequest.getVnp_PayDate());
			transactions.setVnp_ResponseCode(transactionRequest.getVnp_ResponseCode());
			transactions.setVnp_SecureHash(transactionRequest.getVnp_SecureHash());
			transactions.setVnp_TmnCode(transactionRequest.getVnp_TmnCode());
			transactions.setVnp_TransactionNo(transactionRequest.getVnp_TransactionNo());
			transactions.setVnp_TransactionStatus(transactionRequest.getVnp_TransactionStatus());
			transactions.setVnp_TxnRef(transactionRequest.getVnp_TxnRef());

			transactionRepo.save(transactions);

			String signValue = CommonUtils.hashAllFields(fields);
//			if (signValue.equals(vnp_SecureHash)) {
			if ("00".equals(transactionRequest.getVnp_ResponseCode())) {
//				System.out.println("Giao dịch thành công");
				Bill bill = billRepo.findBillByCode(transactionRequest.getVnp_TxnRef());
				if (bill != null) {
					bill.setPaymentStatus(CommonUtils.PaymentStatus.SUCCESS.getValue());
					billService.saveBill(bill);
				} else {
					System.out.println("không tìm thấy thông tin hóa đơn");
				}
				responseVnpay.setErrorCode("00");
				responseVnpay.setErrorMessage("Giao dịch thành công");
			} else if ("07".equals(transactionRequest.getVnp_ResponseCode())) {

				Bill bill = billRepo.findBillByCode(transactionRequest.getVnp_TxnRef());
				billService.cancelBill(bill.getId());
				responseVnpay.setErrorCode("07");
				responseVnpay.setErrorMessage(
						"Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường).\")");
			} else if ("09".equals(transactionRequest.getVnp_ResponseCode())) {
				Bill bill = billRepo.findBillByCode(transactionRequest.getVnp_TxnRef());
				billService.cancelBill(bill.getId());
				responseVnpay.setErrorCode("09");
				responseVnpay.setErrorMessage(
						"Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng.");
			} else if ("10".equals(transactionRequest.getVnp_ResponseCode())) {
				Bill bill = billRepo.findBillByCode(transactionRequest.getVnp_TxnRef());
				billService.cancelBill(bill.getId());
				responseVnpay.setErrorCode("10");
				responseVnpay.setErrorMessage(
						"Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần.");
			} else if ("11".equals(transactionRequest.getVnp_ResponseCode())) {
				Bill bill = billRepo.findBillByCode(transactionRequest.getVnp_TxnRef());
				billService.cancelBill(bill.getId());
				responseVnpay.setErrorCode("11");
				responseVnpay.setErrorMessage(
						"Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.");
			} else if ("12".equals(transactionRequest.getVnp_ResponseCode())) {
				Bill bill = billRepo.findBillByCode(transactionRequest.getVnp_TxnRef());
				billService.cancelBill(bill.getId());
				responseVnpay.setErrorCode("12");
				responseVnpay.setErrorMessage("Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.");
			} else if ("13".equals(transactionRequest.getVnp_ResponseCode())) {
				Bill bill = billRepo.findBillByCode(transactionRequest.getVnp_TxnRef());
				billService.cancelBill(bill.getId());
				responseVnpay.setErrorCode("13");
				responseVnpay.setErrorMessage(
						"Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch.");
			} else if ("24".equals(transactionRequest.getVnp_ResponseCode())) {
				Bill bill = billRepo.findBillByCode(transactionRequest.getVnp_TxnRef());
				billService.cancelBill(bill.getId());
				responseVnpay.setErrorCode("07");
				responseVnpay.setErrorMessage("Giao dịch không thành công do: Khách hàng hủy giao dịch");
			} else if ("51".equals(transactionRequest.getVnp_ResponseCode())) {
				Bill bill = billRepo.findBillByCode(transactionRequest.getVnp_TxnRef());
				billService.cancelBill(bill.getId());
				responseVnpay.setErrorCode("51");
				responseVnpay.setErrorMessage("Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.");
			} else if ("65".equals(transactionRequest.getVnp_ResponseCode())) {
				Bill bill = billRepo.findBillByCode(transactionRequest.getVnp_TxnRef());
				billService.cancelBill(bill.getId());
				responseVnpay.setErrorCode("65");
				responseVnpay.setErrorMessage("Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.");
			} else if ("75".equals(transactionRequest.getVnp_ResponseCode())) {
				Bill bill = billRepo.findBillByCode(transactionRequest.getVnp_TxnRef());
				billService.cancelBill(bill.getId());
				responseVnpay.setErrorCode("75");
				responseVnpay.setErrorMessage("Ngân hàng thanh toán đang bảo trì.");
			} else if ("79".equals(transactionRequest.getVnp_ResponseCode())) {
				Bill bill = billRepo.findBillByCode(transactionRequest.getVnp_TxnRef());
				billService.cancelBill(bill.getId());
				responseVnpay.setErrorCode("79");
				responseVnpay.setErrorMessage("Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch");
			} else if ("99".equals(transactionRequest.getVnp_ResponseCode())) {
				Bill bill = billRepo.findBillByCode(transactionRequest.getVnp_TxnRef());
				billService.cancelBill(bill.getId());
				responseVnpay.setErrorCode("99");
				responseVnpay.setErrorMessage("Các lỗi khác (lỗi còn lại, không có trong danh sách mã lỗi đã liệt kê)");
			} else {
				Bill bill = billRepo.findBillByCode(transactionRequest.getVnp_TxnRef());
				billService.cancelBill(bill.getId());
				responseVnpay.setErrorCode("100");
				responseVnpay.setErrorMessage("Lỗi không xác định.");
			}
//			} else {
//				System.out.println("Chu ky khong hop le");
//			}

		} catch (

		IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Map fields = new HashMap();
//		for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
//			String fieldName = (String) params.nextElement();
//			String fieldValue = request.getParameter(fieldName);
//			if ((fieldValue != null) && (fieldValue.length() > 0)) {
//				fields.put(fieldName, fieldValue);
//			}
//		}
//		String vnp_SecureHash = request.getParameter("vnp_SecureHash");
//		if (fields.containsKey("vnp_SecureHashType")) {
//			fields.remove("vnp_SecureHashType");
//		}
//		if (fields.containsKey("vnp_SecureHash")) {
//			fields.remove("vnp_SecureHash");
//		}
//		String signValue = CommonUtils.hashAllFields(fields);
//		if (signValue.equals(vnp_SecureHash)) {
//			if ("00".equals(transactionRequest.getVnp_TransactionStatus())) {
//				System.out.println("GD Thanh cong");
//			} else {
//				System.out.println("GD Khong thanh cong");
//			}
//
//		} else {
//			System.out.println("Chu ky khong hop le");
//		}
//		return ResponseEntity.ok(transactionRequest);
		return new ResponseEntity<ResponseVnpay>(responseVnpay, HttpStatus.OK);
	}
}
