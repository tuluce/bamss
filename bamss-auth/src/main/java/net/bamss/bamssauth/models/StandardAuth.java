package net.bamss.bamssauth.models;

public class StandardAuth extends BaseAuth {
	private final String token;

	public StandardAuth(String token) {
		super("standard");
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
