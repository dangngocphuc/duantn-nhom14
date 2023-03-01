package com.fpoly.datn.auth;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.datn.common.CommonUtils;
import com.fpoly.datn.entity.User;
import com.fpoly.datn.model.LoginRequest;
import com.fpoly.datn.service.UserAdminService;
import com.fpoly.datn.service.UserService;



@RestController
@RequestMapping(path = "/api/authenticate")
public class AutheticateController {
	@Autowired
	private UserAdminService userAdminService;
	@Autowired
	private UserService userService;
	@Autowired
	private CommonUtils commonUtils;
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	PasswordEncoder encoder;

	@PostMapping(value = "/admin/login")
	public ResponseEntity<LoginResponse> loginAdmin(@RequestBody LoginRequest loginRequest,
			HttpServletResponse response) {
//		if (userAdminService.loginAdmin(loginRequest) == CommonUtils.LOGIN_SUCCESS) {
		String auth;
//			LoginResponse loginResponse = new LoginResponse();
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setErrorCode("00");
		
//		encoder.encode(loginRequest.getPassword());
//		
//		System.out.println(encoder.encode(loginRequest.getPassword()));
		
		try {
			auth = commonUtils.createToken(loginRequest.getUsername(), loginRequest.getPassword(), "1");
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			User user = (User) authentication.getPrincipal();
			List<String> permissions = new ArrayList<>();
			for (GrantedAuthority role : user.getAuthorities()) {
				permissions.add(role.getAuthority());
			}
			UserDetail userDetail = new UserDetail();
			userDetail.setTokenId(auth);
			userDetail.setUsername(user.getUsername());
			userDetail.setUserID(user.getUserID());
			userDetail.setPermissions(permissions);

			loginResponse.setUserDetail(userDetail);
			loginResponse.setAuthorization(auth);
			loginResponse.setAuthenticated(true);
			Cookie myCookie = new Cookie("COOKIEID", auth);
			response.addCookie(myCookie);
			if (loginRequest.getRemember() != null && loginRequest.getRemember() == true) {
				userDetail.setAdmin(true);
			}
		} catch (Exception e) {
			return new ResponseEntity<LoginResponse>(new LoginResponse("00", "Error"), HttpStatus.OK);
		}
		return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
//		}
//		return new ResponseEntity<LoginResponse>(new LoginResponse("false", ""), HttpStatus.OK);

	}

	@PostMapping(value = "/user/login")
	public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
//		if (userService.loginUser(loginRequest) != CommonUtils.LOGIN_FAIL) {
		String auth;
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setErrorCode("00");
		try {
			auth = commonUtils.createToken(loginRequest.getUsername(), loginRequest.getPassword(), "0");
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			User user = (User) authentication.getPrincipal();

			List<String> permissions = new ArrayList<>();
			for (GrantedAuthority role : user.getAuthorities()) {
				permissions.add(role.getAuthority());
			}
			UserDetail userDetail = new UserDetail();
			userDetail.setTokenId(auth);
			userDetail.setUsername(user.getUsername());
			userDetail.setUserID(user.getUserID());
			userDetail.setPermissions(permissions);
			
			loginResponse.setUserDetail(userDetail);
			loginResponse.setAuthorization(auth);
			loginResponse.setAuthenticated(true);
		} catch (Exception e) {
			return new ResponseEntity<LoginResponse>(new LoginResponse("false", ""), HttpStatus.OK);
		}
		return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
//		}
//		return new ResponseEntity<LoginResponse>(new LoginResponse("false", ""), HttpStatus.OK);
	}
	
	@RequestMapping(path = "/userdetail", method = RequestMethod.GET)
	public ResponseEntity<UserDetail> userDetail(@CookieValue(value = "COOKIEID") String COOKIEID)
			throws Exception {
		com.fpoly.datn.model.UserDetail userDetail = this.commonUtils.getCurrentUserDetail();
		UserDetail userDetails =  new UserDetail();;
		if (userDetails != null) {
			userDetails.setTokenId(COOKIEID);
			UserDetails us = userService.loadUserByUsername(userDetail.getUsername());
			if (us != null) {
				List<String> permissions = new ArrayList<>();
				for (GrantedAuthority role : userDetail.getAuthorities()) {
					permissions.add(role.getAuthority());
				}
				userDetails.setTokenId(COOKIEID);
				userDetails.setUsername(us.getUsername());
				userDetails.setPermissions(permissions);
			} else {
//				throw new ApiException(ErrorCode.GTGC_LOGIN_1002);
			}

			return new ResponseEntity<UserDetail>(userDetails, HttpStatus.OK);
		}
		return new ResponseEntity<UserDetail>(HttpStatus.FORBIDDEN);
	}
}
