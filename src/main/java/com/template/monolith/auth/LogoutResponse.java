package com.template.monolith.auth;

public class LogoutResponse {

	private String username;
	private Boolean loggedOut;
	private String message;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getLoggedOut() {
		return loggedOut;
	}

	public void setLoggedOut(Boolean loggedOut) {
		this.loggedOut = loggedOut;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
