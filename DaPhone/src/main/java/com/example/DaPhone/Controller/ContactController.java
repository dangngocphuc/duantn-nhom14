package com.example.DaPhone.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DaPhone.Entity.Contact;
import com.example.DaPhone.Model.Response;
import com.example.DaPhone.Service.ContactService;

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
