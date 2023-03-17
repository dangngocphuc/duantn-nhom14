package com.fpoly.datn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.datn.entity.Cpu;
import com.fpoly.datn.service.CpuService;

@RestController
@RequestMapping(path = "/api/cpu")
public class CpuController {
	
	@Autowired
	private CpuService cpuService;

	@GetMapping(value = "")
	public ResponseEntity<Page<Cpu>> getPageImei(Pageable pageable) {
		Page<Cpu> pageCpu = cpuService.getPageCpu(pageable);
		return new ResponseEntity<Page<Cpu>>(pageCpu, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Cpu> getCpuById(@PathVariable(name = "id") Long id) {
		return new ResponseEntity<Cpu>(cpuService.findById(id), HttpStatus.OK);
	}
	
	@PostMapping(value="")
	public ResponseEntity<Boolean> saveCpu(@RequestBody Cpu cpu) {
		return new ResponseEntity<Boolean>(cpuService.saveCpu(cpu), HttpStatus.OK);
	}
	
	@GetMapping(value = "/list")
	public ResponseEntity<List<Cpu>> getListCpu() {
		List<Cpu> listCpu = cpuService.getListCpu();
		return new ResponseEntity<List<Cpu>>(listCpu, HttpStatus.OK);
	}

}