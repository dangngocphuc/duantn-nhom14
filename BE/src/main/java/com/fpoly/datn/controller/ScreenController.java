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

import com.fpoly.datn.entity.Screen;
import com.fpoly.datn.service.ScreenService;

@RestController
@RequestMapping(path = "/api/Screen")
public class ScreenController {

	@Autowired
	private ScreenService screenService;

	@GetMapping(value = "")
	public ResponseEntity<Page<Screen>> getPageImei(Pageable pageable) {
		Page<Screen> pageCpu = screenService.getPageScreens(pageable);
		return new ResponseEntity<Page<Screen>>(pageCpu, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Screen> getScreenById(@PathVariable(name = "id") Long id) {
		return new ResponseEntity<Screen>(screenService.findById(id), HttpStatus.OK);
	}

	@PostMapping(value = "")
	public ResponseEntity<Boolean> saveScreen(@RequestBody Screen screen) {
		return new ResponseEntity<Boolean>(screenService.saveScreen(screen), HttpStatus.OK);
	}
}
