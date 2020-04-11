package net.bamss.bamss.models;

public class Validation {
	private final String username;
	private final int quota;

	public Validation(String username, int quota) {
		this.username = username;
		this.quota = quota;
	}

	public String getUsername() {
		return username;
	}

	public int getQuota() {
		return quota;
	}
}
