package com.example.DaPhone.Controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.DaPhone.Common.CommonUtils;
import com.example.DaPhone.Entity.Bill;
import com.example.DaPhone.Entity.BillDetail;
import com.example.DaPhone.Model.Response;
import com.example.DaPhone.Request.BillRequest;
import com.example.DaPhone.Service.BillService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@RestController
@RequestMapping(path = "/api/bill")
public class BillController {

	@Autowired
	private BillService billService;

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
	@PostMapping(value = "/cancel")
	public ResponseEntity<Response<BillDetail>> cancelBill(@RequestBody Bill bill) {
		if (bill != null) {
			billService.cancelBill(bill);
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
		String vnp_OrderInfo = "Thanh toan cho don hang";
		String orderType = "other";
		String vnp_TxnRef = CommonUtils.getRandomNumber(8);
		String vnp_IpAddr = CommonUtils.getIpAddress(req);
		String vnp_TmnCode = CommonUtils.vnp_TmnCode;

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
	public ResponseEntity<Map<String, String>> showRespond(@RequestParam Map<String, String> allParam) {
		return ResponseEntity.ok(allParam);
	}
}
