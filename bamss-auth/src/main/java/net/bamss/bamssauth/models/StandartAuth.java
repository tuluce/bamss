package net.bamss.bamssauth.models;

public class StandartAuth extends BaseAuth {
	private final String token;

	public StandartAuth(String token) {
		super("standart");
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
