package com.template.monolith.auth;

/**
 * a registration response to the register method in UserController class
 * @author PARSA
 *
 */
public class UserRegistrationResponse {
	private String username;
	private Boolean registered;
	private String message;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getRegistered() {
		return registered;
	}

	public void setRegistered(Boolean registered) {
		this.registered = registered;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
