package com.example.DaPhone.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DaPhone.Entity.BillDetail;
import com.example.DaPhone.Model.Response;
import com.example.DaPhone.Service.BillDetailService;
@RestController
@RequestMapping(path = "/api/bill-detail")
public class BillDetailController {
	@Autowired
	private BillDetailService billDetailService;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<BillDetail>> getBillDetail(@PathVariable(name = "id") Long id) {
		List<BillDetail> billDetails = billDetailService.findByBill(id);
		
		billDetails.forEach(e->{
			e.getBill().setListBillDetail(null);
			e.getBill().setProducts(null);
			e.getBill().getUser().setListBill(null);
			e.getProductDetail().setListProductDetailValue(null);
			e.getProductDetail().setProduct(null);
			e.getProductDetail().setListImei(null);
		});
		
		Long count = (long) billDetails.size();
		return new ResponseEntity<Response<BillDetail>>(new Response<BillDetail>(count, billDetails), HttpStatus.OK);
	}
}
