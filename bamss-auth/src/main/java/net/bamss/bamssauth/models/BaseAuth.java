package net.bamss.bamssauth.models;

public class BaseAuth {
	private final String authType;

	public BaseAuth(String authType) {
		this.authType = authType;
	}

	public String getAuthType() {
		return authType;
	}
}
