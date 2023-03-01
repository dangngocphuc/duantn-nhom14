package com.example.DaPhone.Common;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

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

	public static enum Demand {
		OFFICE("1", "Văn phòng, học tập"), GAMING("2", "Gaming"), DESIGN("3", "");

		private String value;
		private String name;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private Demand(String value, String name) {
			this.value = value;
			this.name = name;
		}

	}

	public static String generateProductCode() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder code = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			int index = (int) (Math.random() * characters.length());
			code.append(characters.charAt(index));
		}
		return code.toString();
	}
	
//	public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
//    public static String vnp_Returnurl = "http://localhost:8080/vnpay_jsp/vnpay_return.jsp";
//    public static String vnp_TmnCode = "";
//    public static String vnp_HashSecret = "";
//    public static String vnp_apiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";
    
    public static final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
//	public static final String vnp_Returnurl = "http://localhost:8081/datnapi/api/bill/payment/results";
	public static final String vnp_Returnurl = "http://localhost:4200/pay-success";
	public static final String vnp_TmnCode = "24F3A5FL";
	public static final String vnp_HashSecret = "BWNFDYXHVSDLZKRXZYNEHUVQGHFFCIZA";
	public static final String vnp_apiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/merchant.html";

    public static String md5(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException ex) {
            digest = "";
        } catch (NoSuchAlgorithmException ex) {
            digest = "";
        }
        return digest;
    }

    public static String Sha256(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException ex) {
            digest = "";
        } catch (NoSuchAlgorithmException ex) {
            digest = "";
        }
        return digest;
    }

    //Util for VNPAY
    public static String hashAllFields(Map fields) {
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                sb.append(fieldName);
                sb.append("=");
                sb.append(fieldValue);
            }
            if (itr.hasNext()) {
                sb.append("&");
            }
        }
        return hmacSHA512(vnp_HashSecret,sb.toString());
    }
    
    public static String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getLocalAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
