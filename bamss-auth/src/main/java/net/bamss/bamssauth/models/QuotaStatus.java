package net.bamss.bamssauth.models;

import java.util.Date;

import org.bson.Document;

public class QuotaStatus {
  private final int usageCount;
	private final Date lastUsage;

	public QuotaStatus(int usageCount, Date lastUsage) {
    this.usageCount = usageCount;
    this.lastUsage = lastUsage;
	}

	public int getUsageCount() {
		return usageCount;
  }
  
  public Date getLastUsage() {
		return lastUsage;
	}

	public Document getDocument() {
		return new Document()
			.append("usage_count", usageCount)
			.append("last_usage", lastUsage);
	}
}
