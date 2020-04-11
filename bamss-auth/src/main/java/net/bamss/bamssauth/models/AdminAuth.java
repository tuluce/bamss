package net.bamss.bamssauth.models;

public class AdminAuth extends BaseAuth {
	private final String adminKey;

	public AdminAuth(String adminKey) {
		super("admin");
		this.adminKey = adminKey;
	}

	public String getAdminKey() {
		return adminKey;
	}
}
