package com.fpoly.datn.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fpoly.datn.entity.Imei;
import com.fpoly.datn.model.Response;
import com.fpoly.datn.model.ResponseVnpay;
import com.fpoly.datn.request.ImeiRequest;
import com.fpoly.datn.service.ImeiService;

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

	@GetMapping(value = "/list")
	public ResponseEntity<List<Imei>> getListImei(Pageable pageable, ImeiRequest req) {
		List<Imei> pageImei = imeiService.getListImeiByProductDetail(req, pageable);
		return new ResponseEntity<List<Imei>>(pageImei, HttpStatus.OK);
	}

	public Imei toResponse(Imei entity) {
		if (entity == null) {
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

	@PostMapping(value = "/import", consumes = { "multipart/form-data" })
	public ResponseEntity<ResponseVnpay> importData(@RequestParam(value = "file", required = true) MultipartFile[] files)
			throws IOException {
		String jsonResp = null;
		ResponseVnpay response = new ResponseVnpay();
		response.setErrorCode("00");
		response.setErrorMessage("success");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		String msg = "success";
		try {
			try {
				Map<String, List> mResult = imeiService.readFileExcel(files);
				for (Map.Entry mapElement : mResult.entrySet()) {
					String key = (String) mapElement.getKey();
					List<Imei> lst = (ArrayList<Imei>) mapElement.getValue();
					if (key.equals("SUCCESS") && lst.size() > 0) {
//						msg = daoObj.insertList(lst);
						boolean hasDuplicateName = false;
						Set<String> imei = new HashSet<>();
						for (Imei i : lst) {
						    if (!imei.add(i.getImei())) {
						        // Trường name đã tồn tại trong Set, tức là đã có đối tượng khác có cùng giá trị
						        hasDuplicateName = true;
						        break;
						    }
						}
						if (hasDuplicateName) {
//						    System.out.println("Imei bị trùng lặp trong danh sách.");
						    msg="Imei bị trùng lặp trong danh sách.";
						    response.setErrorCode("01");
							response.setErrorMessage(msg);
							return new ResponseEntity<ResponseVnpay>(response, HttpStatus.OK);
						} else {
						    imeiService.saveListImei(lst);
						}
					}
					if(key.equals("ERROR") && lst.size() > 0) {
						msg = lst.toString();
						response.setErrorCode("01");
						response.setErrorMessage(msg);
					}
				}
//				System.out.println(mResult);
			} catch (Exception e) {
				e.printStackTrace();
				msg = e.getMessage();
				response.setErrorCode("01");
				response.setErrorMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonResp = msg;
		return new ResponseEntity<ResponseVnpay>(response, HttpStatus.OK);
	}
}
