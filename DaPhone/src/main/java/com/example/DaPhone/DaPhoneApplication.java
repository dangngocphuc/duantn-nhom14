package com.example.DaPhone;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.DaPhone.Entity.Role;
import com.example.DaPhone.Entity.User;
import com.example.DaPhone.Model.RoleName;
import com.example.DaPhone.Repository.RoleRepository;
import com.example.DaPhone.Service.UserService;
import com.example.DaPhone.ServiceImpl.UserServiceImpl;

@SpringBootApplication
@EnableScheduling
public class DaPhoneApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(DaPhoneApplication.class, args);
	
	}

}
