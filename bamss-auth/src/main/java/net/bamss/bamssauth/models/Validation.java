package net.bamss.bamssauth.models;

public class Validation {
	private final String username;
	private final boolean hasQuota;

	public Validation(String username, boolean hasQuota) {
		this.username = username;
		this.hasQuota = hasQuota;
	}

	public String getUsername() {
		return username;
	}

	public boolean getHasQuota() {
		return hasQuota;
	}
}
