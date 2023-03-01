package com.fpoly.datn.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.util.WebUtils;

import com.fpoly.datn.common.CommonUtils;
import com.fpoly.datn.model.UserDetail;
import com.fpoly.datn.service.CustomOAuth2UserService;
import com.fpoly.datn.serviceImpl.UserServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = false, jsr250Enabled = false)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	UserServiceImpl userDetailsService;

	@Autowired
	private CustomOAuth2UserService oauthUserService;

	@Bean
	public RestAuthenticationEntryPoint restServicesEntryPoint() {
		return new RestAuthenticationEntryPoint();
	}

	@Bean
	public CustomAccessDeniedHandler customAccessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
//		TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(commonUtils);
//		httpSecurity.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//		httpSecurity.antMatcher("/api/lchsdt/**").httpBasic().authenticationEntryPoint(restServicesEntryPoint()).and()
//				.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler());
//		
//		httpSecurity.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//				.authorizeRequests().antMatchers("/api/lchsdt/oauth2/**").permitAll()
//				.antMatchers(HttpMethod.POST, "/api/lchsdt/authenticate/login").permitAll()
//				.anyRequest().authenticated()
//				.anyRequest().permitAll().and().oauth2Login().userInfoEndpoint().userService(oauthUserService);

		TokenAuthenticationFilter tokenFilter = new TokenAuthenticationFilter(commonUtils);
		httpSecurity.addFilterBefore(tokenFilter, BasicAuthenticationFilter.class);
		httpSecurity.headers().frameOptions().disable();
		httpSecurity.cors().and().csrf().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/api/authenticate/**", "/api/authenticate/user/login", "/api/getsession",
						"/api/option/**", "/api/productVariant/**", "/api/imei/**", "/api/user/save", "/api/brand/**","/api/bill/**",
						"/api/bill-detail/**","/api/cpu/**","/api/ram/**","/api/rom/**","/api/gpu/**","/api/user/reviews",
						"/rest-service/*", "/api/product/**", "/api/category/**", "/api/oauth2/google")
				.permitAll().antMatchers("/admin").hasAuthority("ADMIN").anyRequest().authenticated();

	}

	public class TokenAuthenticationFilter implements Filter {
		private CommonUtils commonUtils;

		public TokenAuthenticationFilter(CommonUtils commonUtils) {
			super();
			this.commonUtils = commonUtils;
		}

		@Override
		public void init(FilterConfig filterConfig) throws ServletException {
		}

		@Override
		public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
				throws IOException, ServletException {
			final HttpServletRequest httpRequest = (HttpServletRequest) request;
			Cookie cookie = WebUtils.getCookie(httpRequest, "COOKIEID");
			String jwt = httpRequest.getHeader("Authorization");
			if (cookie != null) {
				String accessToken = cookie.getValue();
				if (accessToken != null && !accessToken.equals("false")) {
					try {
						UserDetail htnsd = this.commonUtils.getUserInfo(accessToken);
						UserDetails userDetails = userDetailsService.loadUserByUsername(htnsd.getUsername());
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
						SecurityContextHolder.getContext().setAuthentication(authentication);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				if (jwt != null) {
					try {
						UserDetail htnsd = this.commonUtils.getUserInfo(jwt);
						String username = htnsd.getUsername();
						UserDetails userDetails = userDetailsService.loadUserByUsername(username);
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
						SecurityContextHolder.getContext().setAuthentication(authentication);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
			/*
			 * if (accessToken != null && accessToken.isEmpty() == false) { try {
			 * UserDetailPrincipal userDetail = this.commonUtils.getUserInfo(accessToken);
			 * boolean authenticated = userDetail.isAuthenticated(); if (authenticated ==
			 * true) { List<GrantedAuthority> grantedAuthorityList = new
			 * ArrayList<GrantedAuthority>(); final UsernamePasswordAuthenticationToken
			 * authentication = new UsernamePasswordAuthenticationToken( userDetail, null,
			 * grantedAuthorityList);
			 * SecurityContextHolder.getContext().setAuthentication(authentication); } }
			 * catch (SignatureException e) { System.out.println("Convert JWT error:" +
			 * accessToken + ":" + httpRequest.getPathInfo()); } catch (Exception e) {
			 * e.printStackTrace(); } }
			 */

			chain.doFilter(request, response);
		}

		@Override
		public void destroy() {

		}

		@SuppressWarnings("unused")
		private String getJwt(HttpServletRequest request) {
			String accessToken = request.getHeader("Authorization");
//			if (authHeader != null && authHeader.startsWith("Bearer ")) {
//				return authHeader.replace("Bearer ", "");
//			}
			return accessToken;
		}
	}

}