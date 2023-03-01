package com.fpoly.datn.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.fpoly.datn.common.CommonUtils;
import com.fpoly.datn.entity.Admin;
import com.fpoly.datn.model.LoginRequest;
import com.fpoly.datn.service.UserAdminService;
@Service
public class UserAdminServiceImpl implements UserAdminService{
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
    @Autowired
    private CommonUtils commonUtils;
    
	@Override
	public Long loginAdmin(LoginRequest loginRequest) {
		String sql = "";
		Map<String, Object> paramMaps = new HashMap<String, Object>();
		if (loginRequest.getUsername() != null && loginRequest.getPassword() != null) {
			sql = "select * from admin where user_name= :username and user_pass = :password;";
	        String matKhau = commonUtils.Sha1EncryptText(loginRequest.getPassword());
			paramMaps.put("username", loginRequest.getUsername());
			paramMaps.put("password", matKhau);
			System.out.print(sql);
		}
		if (!sql.isEmpty()) {
			List<Admin> result = namedParameterJdbcTemplate.query(sql, paramMaps, BeanPropertyRowMapper.newInstance(Admin.class));
			if (result.size()>0)
				return CommonUtils.LOGIN_SUCCESS;
			return CommonUtils.LOGIN_FAIL;
		}
		return CommonUtils.LOGIN_FAIL;
	}
	
	
	
}
