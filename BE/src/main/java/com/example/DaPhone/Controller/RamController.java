package com.example.DaPhone.Controller;

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

import com.example.DaPhone.Entity.Ram;
import com.example.DaPhone.Service.RamService;

@RestController
@RequestMapping(path = "/api/ram")
public class RamController {

	@Autowired
	private RamService ramService;

	@GetMapping(value = "")
	public ResponseEntity<Page<Ram>> getPageRam(Pageable pageable) {
		Page<Ram> pageRam = ramService.getPageRam(pageable);
		return new ResponseEntity<Page<Ram>>(pageRam, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Ram> getRamById(@PathVariable(name = "id") Long id) {
		return new ResponseEntity<Ram>(ramService.findById(id), HttpStatus.OK);
	}

	@PostMapping(value = "")
	public ResponseEntity<Boolean> saveRam(@RequestBody Ram Ram) {
		return new ResponseEntity<Boolean>(ramService.saveRam(Ram), HttpStatus.OK);
	}

	@GetMapping(value = "/list")
	public ResponseEntity<List<Ram>> getListRam() {
		List<Ram> listRam = ramService.getListRam();
		return new ResponseEntity<List<Ram>>(listRam, HttpStatus.OK);
	}

}
