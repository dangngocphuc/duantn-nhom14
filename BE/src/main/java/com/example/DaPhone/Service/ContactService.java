package com.example.DaPhone.Service;

import org.springframework.stereotype.Service;

import com.example.DaPhone.Entity.Contact;
@Service
public interface ContactService {
	public Contact saveContact(Contact contact);
}
