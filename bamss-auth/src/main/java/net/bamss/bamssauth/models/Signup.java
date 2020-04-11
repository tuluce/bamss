package net.bamss.bamssauth.models;

public class Signup {
	private final boolean success;
	private final String message;

	public Signup(String message) {
		this.message = message;
		this.success = false;
	}

	public Signup() {
		this.message = "";
		this.success = true;
	}

	public String getError() {
		return message;
	}

	public boolean getSuccess() {
		return success;
	}
}
