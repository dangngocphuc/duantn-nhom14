package com.example.DaPhone.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DaPhone.Entity.Imei;
import com.example.DaPhone.Request.ImeiRequest;
import com.example.DaPhone.Service.ImeiService;

@RestController
@RequestMapping(path = "/api/imei")
public class ImeiController {

	@Autowired
	private ImeiService imeiService;

	@GetMapping(value = "")
	public ResponseEntity<Page<Imei>> getPageImei(Pageable pageable, ImeiRequest req) {
		Page<Imei> pageImei = imeiService.getPageImei(req, pageable);
		return new ResponseEntity<Page<Imei>>(pageImei, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Imei> getOption(@PathVariable(name = "id") Long id) {
		return new ResponseEntity<Imei>(toResponse(imeiService.findById(id)), HttpStatus.OK);
	}
	
	public Imei toResponse(Imei entity) {
		if(entity==null) {
			return null;
		}
		Imei item = new Imei();
		item.setId(entity.getId());
		item.setImei(entity.getImei());
		item.setProductDetail(entity.getProductDetail());
		item.setStatus(entity.getStatus());
		item.getProductDetail().setListImei(null);
		item.getProductDetail().setProduct(null);
//		item.getProductDetail().setListProductDetailValue(null);
		return item;
	}
}
