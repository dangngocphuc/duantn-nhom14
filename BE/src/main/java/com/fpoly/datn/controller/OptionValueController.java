package com.fpoly.datn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.datn.entity.Option;
import com.fpoly.datn.entity.OptionValue;
import com.fpoly.datn.model.OptionRequest;
import com.fpoly.datn.model.OptionValueRequest;
import com.fpoly.datn.model.Response;
import com.fpoly.datn.service.OptionValueService;

@RestController
@RequestMapping(path = "/api/option/value")
public class OptionValueController {
	
	@Autowired
	private OptionValueService optionService;
	
	@GetMapping(value = "")
	public ResponseEntity<Page<OptionValue>> getOptions(Pageable pageable,OptionValueRequest optionRequest) {
		Page<OptionValue> pageOption = optionService.findOptionValues(optionRequest, pageable);
		return new ResponseEntity<Page<OptionValue>>(pageOption, HttpStatus.OK);
	}
	
	@PostMapping(value = "/save")
//	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response<OptionValue>> saveOption(@RequestBody OptionValue option) {
		if (option != null) {
			return new ResponseEntity<Response<OptionValue>>(new Response<OptionValue>(optionService.saveOptionValue(option)), HttpStatus.OK);
		}
		return new ResponseEntity<Response<OptionValue>>(new Response<OptionValue>("loi", "10001"), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<OptionValue> getOption(@PathVariable(name = "id") Long id) {
		return new ResponseEntity<OptionValue>(optionService.findById(id), HttpStatus.OK);
	}
}
