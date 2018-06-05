package com.x.apa.loader.data;

import com.x.apa.common.data.BaseData;

/**
 * @author liumeng
 */
public class ThreatActor extends BaseData {

	private String registrantEmail;
	private String ipAddress;
	private String description;

	private String creatorId;
	private int status;

	@Override
	public String toString() {
		return "ThreatActor [registrantEmail=" + registrantEmail + ", ipAddress=" + ipAddress + ", description="
				+ description + ", creatorId=" + creatorId + ", status=" + status + ", toString()=" + super.toString()
				+ "]";
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getRegistrantEmail() {
		return registrantEmail;
	}

	public void setRegistrantEmail(String registrantEmail) {
		this.registrantEmail = registrantEmail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
