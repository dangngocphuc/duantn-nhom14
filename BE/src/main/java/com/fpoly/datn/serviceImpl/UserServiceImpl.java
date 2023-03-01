package com.fpoly.datn.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fpoly.datn.common.CommonUtils;
import com.fpoly.datn.entity.Role;
import com.fpoly.datn.entity.User;
import com.fpoly.datn.model.LoginRequest;
import com.fpoly.datn.model.RoleName;
import com.fpoly.datn.repository.RoleRepository;
import com.fpoly.datn.repository.UserRepo;
import com.fpoly.datn.request.UserRequest;
import com.fpoly.datn.service.UserService;

@Service
public class UserServiceImpl implements UserService,UserDetailsService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	private CommonUtils commonUtils;

	@Override
	public Page<User> findUser(UserRequest userParam, Pageable pageable) {

		Page<User> listPage = userRepo.findAll(new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				if (userParam.getUserName() != null) {
					predicates.add(cb.and(cb.like(cb.upper(root.get("userName")),
							"%" + userParam.getUserName().trim().toUpperCase() + "%")));
				}
				if (userParam.getEmail() != null) {
					predicates.add(cb.and(cb.like(cb.upper(root.get("userEmail")),
							"%" + userParam.getEmail().trim().toUpperCase() + "%")));
				}
				if (userParam.getPhone() > 0) {
					predicates.add(cb.and(cb.equal(root.get("userPhone"), userParam.getPhone())));
				}

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
		return listPage;
	}

	public User findUserById(Long id) {
		return userRepo.findById(id).get();
	}

	@Override
	public User saveUser(User user) {
		Role role = roleRepository.findById(RoleName.USER.getRole()).get();
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		user.setRoles(roles);
		user.setUserPass(encoder.encode(user.getPassword()));
		return userRepo.save(user);
	}

	@Override
	public void deleteUser(Long id) {
		userRepo.deleteById(id);
	}

	@Override
	public Long loginUser(LoginRequest loginRequest) {
		String sql = "";
		Long result = CommonUtils.LOGIN_FAIL;
		Map<String, Object> paramMaps = new HashMap<String, Object>();
		if (loginRequest.getUsername() != null && loginRequest.getPassword() != null) {
			String matKhau = commonUtils.Sha1EncryptText(loginRequest.getPassword());
			sql = "select user_id from users where user_name= :username and user_pass = :password;";
			paramMaps.put("username", loginRequest.getUsername());
			paramMaps.put("password", matKhau);
		}
		if (!sql.isEmpty()) {
			try {
				result = namedParameterJdbcTemplate.queryForObject(sql, paramMaps, Long.class);
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return CommonUtils.LOGIN_FAIL;
			}
			
		}
		return result;

	}

	@Override
	public void updatePassword(String password, Long id) {
		String sql = "";
		Map<String, Object> paramMaps = new HashMap<String, Object>();
		if (password != null && id != null) {
			sql = "update users set user_pass = :password where user_id = :id;";
			String matKhau = commonUtils.Sha1EncryptText(password);
			paramMaps.put("id", id);
			paramMaps.put("password", matKhau);
		}
		if (!sql.isEmpty()) {
			int status = namedParameterJdbcTemplate.update(sql, paramMaps);
			if (status != 0) {
				System.out.println("Change pass successful");
			} else {
				System.out.println("Change pass failure");
			}
		}
	}

	@Override
	public Long checkUser(String username) {
		String sql = "";
		Map<String, Object> paramMaps = new HashMap<String, Object>();
		if (username != null) {
			sql = "select * from users where user_name= :username ;";
			paramMaps.put("username", username);

		}
		if (!sql.isEmpty()) {
			List<User> result = namedParameterJdbcTemplate.query(sql, paramMaps,
					BeanPropertyRowMapper.newInstance(User.class));
			if (result.size() > 0)
				return CommonUtils.LOGIN_SUCCESS;
			return CommonUtils.LOGIN_FAIL;
		}
		return CommonUtils.LOGIN_FAIL;
	}

	@Override
	public List<User> getByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepo.findByUserEmail(email);
	}

	/*
	 * @Override public boolean existsEmail(String email) { // TODO Auto-generated
	 * method stub return userRepo.existsByEmail(email); }
	 */

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> user = userRepo.findByUserName(username);
		if (!user.isPresent()) {
			throw new UsernameNotFoundException("Could not find user");
		}
		User newUser = new User();
		newUser.setUserEmail(user.get().getUserEmail());
		newUser.setUserID(user.get().getUserID());
		newUser.setUserName(user.get().getUsername());
		newUser.setUserPhone(user.get().getUserPhone());
		newUser.setRoles(user.get().getRoles());
		for(Role role: user.get().getRoles()) {
			role.setUsers(null);
		}
		return user.get();
	}

}
