package com.fpoly.datn.service;

import org.springframework.stereotype.Service;

import com.fpoly.datn.entity.Contact;
@Service
public interface ContactService {
	public Contact saveContact(Contact contact);
}
