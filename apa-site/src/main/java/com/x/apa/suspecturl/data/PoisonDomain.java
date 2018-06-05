package com.x.apa.suspecturl.data;

import com.x.apa.common.data.BaseData;

/**
 * @author liumeng
 */
public class PoisonDomain extends BaseData {

	private String name;
	private String description;
	private String domainName;

	private String creatorId;
	private int status;

	@Override
	public String toString() {
		return "PoisonDomain [name=" + name + ", description=" + description + ", domainName=" + domainName
				+ ", creatorId=" + creatorId + ", status=" + status + ", toString()=" + super.toString() + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
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
