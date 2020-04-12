package net.bamss.bamss.models;

public class ShortenResult {
	private final String key;
	private final String url;

	public ShortenResult(String key, String url) {
		this.key = key;
		this.url = url;
	}

	public String getKey() {
		return key;
	}
	public String getUrl() {
		return url;
	}

}
