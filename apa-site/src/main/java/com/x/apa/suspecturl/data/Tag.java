package com.x.apa.suspecturl.data;

import com.x.apa.common.data.BaseData;

/**
 * @author liumeng
 */
public class Tag extends BaseData {

	private String name;
	private String creatorId;
	private int status;

	private String rules;

	@Override
	public String toString() {
		return "Tag [name=" + name + ", creatorId=" + creatorId + ", status=" + status + ", toString()="
				+ super.toString() + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

}
