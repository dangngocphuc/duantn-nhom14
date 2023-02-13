package com.example.DaPhone.Auth;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import com.example.DaPhone.Common.CommonUtils;
import com.example.DaPhone.Model.UserDetail;
import com.example.DaPhone.ServiceImpl.UserServiceImpl;


@RestController
@RequestMapping(path = "/api/getsession")
public class FakeSessionController {
	
	@Autowired
	private CommonUtils commonUtils;
	
	@Autowired
	UserServiceImpl userDetailsService;
	
	@GetMapping
	public ResponseEntity<Boolean> getTEST(final ServletRequest request) {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		Cookie cookie = WebUtils.getCookie(httpRequest, "COOKIEID");
		if (cookie != null) {
			String accessToken = cookie.getValue();
			if (accessToken != null) {
				try {
//					final HttpServletResponse httpResponse = (HttpServletResponse) response;

					UserDetail htnsd = commonUtils.getUserInfo(accessToken);
			
					Collection<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
//
//					final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//							htnsd, null, grantedAuthorityList);
//					UserDetail htnsd = this.commonUtils.getUserInfo(accessToken);
					UserDetails userDetails = userDetailsService.loadUserByUsername(htnsd.getUsername());
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
//					SecurityContextHolder.getContext().setAuthentication(authentication);
					
					if(authentication!= null) {
//						UserDetails userDetails =  (UserDetails) authentication.getPrincipal();
						if(userDetails != null) {
							return new ResponseEntity<Boolean>(true, HttpStatus.OK);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					return new ResponseEntity<Boolean>(false, HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.OK);
	}
}
