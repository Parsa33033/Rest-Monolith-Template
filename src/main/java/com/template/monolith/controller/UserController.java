package com.template.monolith.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.template.monolith.auth.AuthenticationRequest;
import com.template.monolith.auth.AuthenticationResponse;
import com.template.monolith.auth.JwtUtils;
import com.template.monolith.auth.LogoutRequest;
import com.template.monolith.auth.LogoutResponse;
import com.template.monolith.auth.UserRegistrationResponse;
import com.template.monolith.model.User;
import com.template.monolith.repository.UserRepository;

@RestController
public class UserController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepo;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	RedisTemplate<Object, Object> redisTemplate;

	/**
	 * registering user
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/register", consumes = { "application/json" })
	public ResponseEntity<UserRegistrationResponse> register(@RequestBody User user) throws Exception {

		String username = user.getUsername();
		String email = user.getEmail();
		List<User> temp = userRepo.getUserByUsernameAndEmail(username, email);
		if (temp.iterator().hasNext()) {
			return ResponseEntity.ok(registrationResponse(username, false, "user has been already registered!"));
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setEnabled(true);
		userRepo.saveAndFlush(user);
		return ResponseEntity.ok(registrationResponse(username, true, "user registered!"));

	}

	/**
	 * creating a user registration response object
	 * 
	 * @param username
	 * @param registered
	 * @param message
	 * @return
	 */
	private UserRegistrationResponse registrationResponse(String username, Boolean registered, String message) {
		UserRegistrationResponse response = new UserRegistrationResponse();
		response.setUsername(username);
		response.setRegistered(registered);
		response.setMessage(message);
		return response;
	}

	/**
	 * authenticating user for a new jwt token
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/authenticate", consumes = { "application/json" })
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)
			throws Exception {

		String username = request.getUsername();
		String password = request.getPassword();

		// authenticate user
		try {
			UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(username, password);
			authenticationManager.authenticate(upat);
		} catch (Exception e) {
			throw new Exception("Authentication Failure!");
		}

		// generate new jwt for the user
		String jwt = jwtUtils.generateToken(username);

		AuthenticationResponse response = new AuthenticationResponse();
		response.setJwt(jwt);

		return ResponseEntity.ok(response);
	}

	/**
	 * loggout user by adding jwt token to blacklist which will be checked in
	 * jwtFilter class while logging in
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/log-out", consumes = { "application/json" })
	public ResponseEntity<LogoutResponse> logout(@RequestBody LogoutRequest request, @RequestHeader("Authorization") String header) throws Exception {
		String username = request.getUsername();
		User user = userRepo.findUserByUsername(username);
		if (user == null) {
			return ResponseEntity.ok(logoutResponse(username, false, "user does not exist for logout!"));
		}
		String headerJwt = header.substring(7);
		String jwt = request.getJwt();
		if (!jwt.equals(headerJwt)) {
			throw new Exception("jwt failure!");
		}
		redisTemplate.opsForSet().add(jwt, username);
		return ResponseEntity.ok(logoutResponse(username, true, "logged out!"));
	}

	/**
	 * logout response
	 * @param username
	 * @param loggedOut
	 * @param message
	 * @return
	 */
	private LogoutResponse logoutResponse(String username, Boolean loggedOut, String message) {
		LogoutResponse response = new LogoutResponse();
		response.setUsername(username);
		response.setLoggedOut(loggedOut);
		response.setMessage(message);
		return response;
	}

}
