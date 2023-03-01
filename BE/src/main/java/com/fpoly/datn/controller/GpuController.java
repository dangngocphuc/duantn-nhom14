package com.fpoly.datn.controller;

import java.util.List;

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

import com.fpoly.datn.entity.Gpu;
import com.fpoly.datn.service.GpuService;

@RestController
@RequestMapping(path = "/api/gpu")
public class GpuController {

	@Autowired
	private GpuService gpuService;

	@GetMapping(value = "")
	public ResponseEntity<Page<Gpu>> getPageGpu(Pageable pageable) {
		Page<Gpu> pageGpu = gpuService.getPageGpu(pageable);
		return new ResponseEntity<Page<Gpu>>(pageGpu, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Gpu> getRamById(@PathVariable(name = "id") Long id) {
		return new ResponseEntity<Gpu>(gpuService.findById(id), HttpStatus.OK);
	}

	@PostMapping(value = "")
	public ResponseEntity<Boolean> saveRam(@RequestBody Gpu gpu) {
		return new ResponseEntity<Boolean>(gpuService.saveGpu(gpu), HttpStatus.OK);
	}

	@GetMapping(value = "/list")
	public ResponseEntity<List<Gpu>> getListGpu() {
		List<Gpu> listGpu = gpuService.getListGpu();
		return new ResponseEntity<List<Gpu>>(listGpu, HttpStatus.OK);
	}

}
