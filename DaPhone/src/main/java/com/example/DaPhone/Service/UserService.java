package com.example.DaPhone.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.DaPhone.Entity.User;
import com.example.DaPhone.Model.LoginRequest;
import com.example.DaPhone.Request.UserRequest;

@Service
public interface UserService {
	public Page<User> findUser(UserRequest userParam, Pageable pageable);
	public User saveUser(User user);
	public User findUserById(Long id);
	public void deleteUser(Long id);
	public Long loginUser(LoginRequest loginRequest);
	public void updatePassword(String password, Long id);
	public Long checkUser(String username);
	public List<User> getByEmail(String email);
//	public boolean existsEmail(String email);
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
