//package com.fpoly.datn.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.fpoly.datn.entity.ReturnBill;
//import com.fpoly.datn.model.Response;
//import com.fpoly.datn.repository.ReturnRepo;
//import com.fpoly.datn.service.ReturnService;
//
//@RestController
//@RequestMapping(path = "/api/return")
//public class ReturnController {
//
//	@Autowired
//	private ReturnService returnService;
//
//	@Autowired
//	private ReturnRepo returnRepo;
//
//	// save return bill
//	@PostMapping(value = "/save")
//	public ResponseEntity<Boolean> saveReturn(@RequestBody ReturnBill returnBill) {
//		if (returnBill != null) {
//			Boolean returnSave = returnService.saveReturnBill(returnBill);
//			return new ResponseEntity<Boolean>(returnSave, HttpStatus.OK);
//		}
//		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
//	}
//
//	// delete return bill
//	@DeleteMapping(value = "/{id}")
//	public ResponseEntity<Response<ReturnBill>> deleteReturn(@PathVariable(name = "id") Long id) {
//		returnService.deleteReturnBill(id);
//		return new ResponseEntity<Response<ReturnBill>>(new Response<ReturnBill>("Xoa thanh cong", "200"),
//				HttpStatus.OK);
//	}
//
//	@GetMapping(value = "/{id}")
//	public ResponseEntity<ReturnBill> getReturnById(@PathVariable(name = "id") Long id) {
//		ReturnBill returnBill = returnService.getByID(id);
//		return new ResponseEntity<ReturnBill>(returnBill, HttpStatus.OK);
//	}
//}
