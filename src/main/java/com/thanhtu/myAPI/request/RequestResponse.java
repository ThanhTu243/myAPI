package com.thanhtu.myAPI.request;

import java.sql.Timestamp;

public class RequestResponse {
	private String customerId;
	private Double value;
	private Timestamp recentDate;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Timestamp getRecentDate() {
		return recentDate;
	}

	public void setRecentDate(Timestamp recentDate) {
		this.recentDate = recentDate;
	}

}
