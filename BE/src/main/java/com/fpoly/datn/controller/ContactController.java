package com.fpoly.datn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.datn.entity.Contact;
import com.fpoly.datn.model.Response;
import com.fpoly.datn.service.ContactService;

@RestController
@RequestMapping(path = "/api/contact")
public class ContactController {
	
	@Autowired
	private ContactService contactService;
	
	@PostMapping(value = "/save")
	public ResponseEntity<Response<Contact>> saveContact(@RequestBody Contact contact) {
		if (contact != null) {
			return new ResponseEntity<Response<Contact>>(new Response<Contact>(contactService.saveContact(contact)), HttpStatus.OK);
		}
		return new ResponseEntity<Response<Contact>>(new Response<Contact>("loi", "10001"), HttpStatus.OK);
	}
}
