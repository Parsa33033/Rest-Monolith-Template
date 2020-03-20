package com.template.monolith.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.template.monolith.auth.AuthenticationRequest;
import com.template.monolith.auth.LogoutRequest;
import com.template.monolith.model.User;

@Aspect
@Component
public class UserControllerAspect {

	Logger logger = LoggerFactory.getLogger(UserControllerAspect.class);

	@Before(value = "execution(* com.template.monolith.controller.UserController.register(..)) and args(user)")
	public void beforeRegistration(User user) {
		logger.info("-----> before registering user with username: " + user.getUsername());
	}

	@After(value = "execution(* com.template.monolith.controller.UserController.register(..)) and args(user)")
	public void afterRegistration(User user) {
		logger.info("-----> registered user with username: " + user.getUsername());
	}

	@Before(value = "execution(* com.template.monolith.controller.UserController.authenticate(..)) and args(request)")
	public void beforeRegistration(AuthenticationRequest request) {
		logger.info("-----> before authenticating user with username: " + request.getUsername());
	}

	@After(value = "execution(* com.template.monolith.controller.UserController.authenticate(..)) and args(request)")
	public void afterRegistration(AuthenticationRequest request) {
		logger.info("-----> auhtenicated user with username: " + request.getUsername());
	}

	@Before(value = "execution(* com.template.monolith.controller.UserController.logout(..)) and args(request)")
	public void beforeLogout(LogoutRequest request) {
		logger.info("-----> before loggin out user with username: " + request.getUsername());
	}

	@After(value = "execution(* com.template.monolith.controller.UserController.logout(..)) and args(request)")
	public void afterLogout(LogoutRequest request) {
		logger.info("-----> logged out user with username: " + request.getUsername());
	}

}
