package net.bamss.bamss.models;

public class ShortenResult {
	private final String key;
	private final String url;
	private final String expireDate;


	public ShortenResult(String key, String url, String expireDate) {
		this.key = key;
		this.url = url;
		this.expireDate = expireDate;
	}

	public String getKey() {
		return key;
	}
	public String getUrl() {
		return url;
	}
	public String getExpireDate() {
		return expireDate;
	}

}
