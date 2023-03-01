package com.fpoly.datn.serviceImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.datn.entity.Contact;
import com.fpoly.datn.repository.ContactRepo;
import com.fpoly.datn.service.ContactService;
@Service
public class ContactServiceImpl implements ContactService{
	
	@Autowired
	private ContactRepo contactRepo;
	
	@Override
	public Contact saveContact(Contact contact) {
		contact.setDate(new Date());
		return contactRepo.save(contact);
	}

}
