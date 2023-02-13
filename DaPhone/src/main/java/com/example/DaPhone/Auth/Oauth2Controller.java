package com.example.DaPhone.Auth;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DaPhone.Common.CommonUtils;
import com.example.DaPhone.Entity.User;
import com.example.DaPhone.Model.LoginResponse;
import com.example.DaPhone.Model.TokenDto;
import com.example.DaPhone.Service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@RestController
@RequestMapping("/api/oauth2")
public class Oauth2Controller {

	@Value("${google.clientId}")
	String googleClientId;

//	@Value("${secretPsw}")
//    String secretPsw;

	@Autowired
	private UserService userService;

	@Autowired
	private CommonUtils commonUtils;

	@PostMapping("/google")
	public ResponseEntity<LoginResponse> google(@RequestBody TokenDto tokenDto) throws IOException {
		String secretPsw = "kasdjhfkadhsY776ggTyUU65khaskdjfhYuHAwjñlji";
		String auth;
		final NetHttpTransport transport = new NetHttpTransport();
		final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
		GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
				.setAudience(Collections.singletonList(googleClientId));
		final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), tokenDto.getValue());
		final GoogleIdToken.Payload payload = googleIdToken.getPayload();
		User usuario = new User();
		if (!userService.getByEmail(payload.getEmail()).isEmpty())
			usuario = userService.getByEmail(payload.getEmail()).get(0);
		else {
			usuario.setUserEmail(payload.getEmail());
//			usuario.setUserRole(false);
			usuario.setUserName(payload.getEmail());
			usuario.setUserPass(commonUtils.Sha1EncryptText(secretPsw));
			usuario = userService.saveUser(usuario);
		}
		try {
			auth = commonUtils.createToken(payload.getEmail(), secretPsw, "0");
		} catch (Exception e) {
			return new ResponseEntity<LoginResponse>(new LoginResponse("false", ""), HttpStatus.OK);
		}

		return new ResponseEntity<LoginResponse>(
				new LoginResponse(auth, usuario.getUsername(), usuario.getUserID()),
				HttpStatus.OK);
	}

//	private TokenDto login(User usuario) {
//		Authentication authentication = authenticationManager
//				.authenticate(new UsernamePasswordAuthenticationToken(usuario.getEmail(), secretPsw));
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//		String jwt = jwtProvider.generateToken(authentication);
//		TokenDto tokenDto = new TokenDto();
//		tokenDto.setValue(jwt);
//		return tokenDto;
//	}

}
