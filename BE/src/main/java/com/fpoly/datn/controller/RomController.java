package com.fpoly.datn.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.datn.entity.Rom;
import com.fpoly.datn.service.RomService;

@RestController
@RequestMapping(path = "/api/rom")
public class RomController {

	@Autowired
	private RomService romService;

	@GetMapping(value = "")
	public ResponseEntity<Page<Rom>> getPageRom(Pageable pageable) {
		Page<Rom> pageRom = romService.getPageRom(pageable);
		return new ResponseEntity<Page<Rom>>(pageRom, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Rom> getRomById(@PathVariable(name = "id") Long id) {
		return new ResponseEntity<Rom>(romService.findById(id), HttpStatus.OK);
	}

	@PostMapping(value = "")
	public ResponseEntity<Boolean> saveRom(@RequestBody Rom Rom) {
		return new ResponseEntity<Boolean>(romService.saveRom(Rom), HttpStatus.OK);
	}

	@GetMapping(value = "/list")
	public ResponseEntity<List<Rom>> getListRom() {
		List<Rom> listRom = romService.getListRom();
		return new ResponseEntity<List<Rom>>(listRom, HttpStatus.OK);
	}

}
