package net.bamss.bamssauth.models;

public class BusinessAuth extends BaseAuth {
	private final String apiKey;

	public BusinessAuth(String apiKey) {
		super("business");
		this.apiKey = apiKey;
	}

	public String getApiKey() {
		return apiKey;
	}
}
