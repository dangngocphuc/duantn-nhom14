package com.example.DaPhone.Common;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

//import com.example.DaPhone.Auth.UserDetail;

import com.example.DaPhone.Model.UserDetail;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class CommonUtils {
	@Value("${privatekey_string}")
	private String PRIVATEKEY_STRING;
	@Value("${publickey_string}")
	private String PUBLICKEY_STRING;

	public String createToken(String username, String password, String authRequestID) throws Exception {
		byte[] privateKeyBytes = Base64.getDecoder().decode(PRIVATEKEY_STRING);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PrivateKey privKey = kf.generatePrivate(keySpec);
		if (authRequestID != null) {
			JwtBuilder jwtBuilder = Jwts.builder().setSubject(username).setId(authRequestID)
					.claim("tokenType", "register")
					.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(1440).toInstant()));
			return jwtBuilder.signWith(privKey).compact();
		}
		return null;
	}

//	public UserDetailPrincipal getUserInfo(String jwtToken) throws Exception {
//		byte[] publicKeyBytes = Base64.getDecoder().decode(PUBLICKEY_STRING);
//		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
//		KeyFactory kf = KeyFactory.getInstance("RSA");
//		PublicKey publicKey = kf.generatePublic(keySpec);
//		Jws<Claims> jws = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(jwtToken);
//		UserDetailPrincipal userDetail = new UserDetailPrincipal();
//		Claims claims = jws.getBody();
//		userDetail.setId(claims.getId());
//		userDetail.setUsername(claims.getSubject());
//		String tokenType = claims.get("tokenType", String.class);
//		if (tokenType.equalsIgnoreCase("register")) {
//			userDetail.setAuthenticated(true);
//		} else {
//			userDetail.setAuthenticated(false);
//		}
//		return userDetail;	
//	}

	public UserDetail getUserInfo(String jwtToken) throws Exception {
		byte[] publicKeyBytes = Base64.getDecoder().decode(PUBLICKEY_STRING);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PublicKey publicKey = kf.generatePublic(keySpec);
		Jws<Claims> jws = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(jwtToken);

		Claims claims = jws.getBody();
		Long date = claims.get("date", Long.class);
		/*
		 * if (date != null && date > new Date().getTime() + 4 * 60 * 60 * 1000) { throw
		 * new ApiException(ErrorCode.EXPIRED); }
		 */
		UserDetail userDetail = new UserDetail();
		userDetail.setId(Long.parseLong(claims.getId()));
		userDetail.setUsername(claims.getSubject());
		userDetail.setTokenId(claims.get("tokenId", String.class));
//		userDetail.setChucVu(claims.get("chucVu", String.class));
//		userDetail.setAuthorities((Collection<? extends GrantedAuthority>) claims.get("Em", UserDetailPrincipal.class));
//		userDetail.setMaPhongBan(claims.get("maPhongBan", String.class));
		return userDetail;
	}

//	public UserDetail getCurrentUserDetail() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication != null) {
//			Object principal = authentication.getPrincipal();
//			if (principal instanceof UserDetail) {
//				return (UserDetail) principal;
//			}
//		}
//		return null;
//	}

	public KeyPair createNewKeyPair() {
		return Keys.keyPairFor(SignatureAlgorithm.RS256);
	}

	public class UserDetailPrincipal {
		private String id;
		private String username;
		private boolean authenticated;

		public boolean isAuthenticated() {
			return authenticated;
		}

		public void setAuthenticated(boolean authenticated) {
			this.authenticated = authenticated;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
	}

	public static String StringFormatDate(Date sdate, String toDate) {
		Date date = sdate;
		SimpleDateFormat formatter = new SimpleDateFormat(toDate);
		return formatter.format(date);
	}

	public static Map<String, List<Date>> getWeeksOfMonth() {
		List<Date> listDateOfMonth = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int myMonth = cal.get(Calendar.MONTH);
		while (myMonth == cal.get(Calendar.MONTH)) {
			listDateOfMonth.add(cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		int from = 1;
		int to = 7;
		Map<String, List<Date>> weekOfMonth = new HashMap<String, List<Date>>();
		for (int i = 1; i <= (listDateOfMonth.size() / 7); i++) {
			List<Date> dates = new ArrayList<Date>();
			dates.add(listDateOfMonth.get(from - 1));
			dates.add(listDateOfMonth.get(to - 1));
			weekOfMonth.put("Week" + i, dates);
			from += 7;
			to = 7 * (i + 1);
		}
		List<Date> otherDates = new ArrayList<Date>();
		for (int j = from; j <= listDateOfMonth.size(); j++) {
			otherDates.add(listDateOfMonth.get(j - 1));
			weekOfMonth.put("Week" + ((listDateOfMonth.size() / 7) + 1), otherDates);
		}
		return weekOfMonth;
	}

	// response exception
	public static final String GIUPVIEC_HVT = "GIUPVIEC_HVT";
	public static final Long LOGIN_FAIL = 10L;
	public static final String PROCESS = "Process";
	public static final String DELIVERY = "Delivery";
	public static final String DELIVERED = "Delivered";
	public static final String CANCEL = "Cancel";
	public static final Long LOGIN_SUCCESS = 0L;
	public static final String ROOT_IMAGES_BACKEND = "D:/Linh_tinh/work-to-do/ki 2 nam 4/DoAnTotNghiep/do_an_code/be/src/assets/images/";
	public static final String ROOT_IMAGES_FRONTEND = "D:/Linh_tinh/work-to-do/ki 2 nam 4/DoAnTotNghiep/do_an_code/fe/src/assets/images/";

	public static String Sha1EncryptText(String sInputText) {
		try {
			return DigestUtils.sha1Hex(sInputText);
		} catch (Exception e) {
			return null;
		}
	}

	public UserDetail getCurrentUserDetail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetail) {
				return (UserDetail) principal;
			}
		}
		return null;
	}

	public static enum TrangThai {

		CON(1, ""), HET_HANG(0, "");

		private Integer value;
		private String name;

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private TrangThai(Integer value, String name) {
			this.value = value;
			this.name = name;
		}
	}

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

	public static String generateBillNumber() {
		Date now = new Date();
		return DATE_FORMAT.format(now);
	}
}
