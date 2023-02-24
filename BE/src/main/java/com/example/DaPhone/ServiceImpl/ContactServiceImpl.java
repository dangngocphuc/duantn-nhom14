package com.example.DaPhone.ServiceImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DaPhone.Entity.Contact;
import com.example.DaPhone.Repository.ContactRepo;
import com.example.DaPhone.Service.ContactService;
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
