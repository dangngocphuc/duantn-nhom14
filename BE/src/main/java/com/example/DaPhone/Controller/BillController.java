package com.example.DaPhone.Controller;

import java.io.ByteArrayInputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DaPhone.Common.CommonUtils;
import com.example.DaPhone.Entity.Bill;
import com.example.DaPhone.Entity.BillDetail;
import com.example.DaPhone.Model.Response;
import com.example.DaPhone.Request.BillRequest;
import com.example.DaPhone.Service.BillService;

@RestController
@RequestMapping(path = "/api/bill")
public class BillController {
	
	@Autowired
	private BillService billService;
	//search
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
	
	//Save bill
	@PostMapping(value = "/save")
	public ResponseEntity<Boolean> saveBill(@RequestBody Bill bill) {
		if (bill != null) {
			Boolean billSave = billService.saveBill(bill);
			return new ResponseEntity<Boolean>(billSave, HttpStatus.OK);
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}
	
	
	//Delete bill
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
	
	//Export excel
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
	
	//cancel bill
	@PostMapping(value = "/cancel")
	public ResponseEntity<Response<BillDetail>> cancelBill(@RequestBody Bill bill) {
		if(bill!=null) {
			billService.cancelBill(bill);
			return new ResponseEntity<Response<BillDetail>>(new Response<BillDetail>("1002","oke"), HttpStatus.OK);
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
}
