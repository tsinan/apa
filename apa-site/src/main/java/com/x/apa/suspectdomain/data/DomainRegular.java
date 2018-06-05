package com.x.apa.suspectdomain.data;

import com.x.apa.common.data.BaseData;

/**
 * @author liumeng
 */
public class DomainRegular extends BaseData {

	private String name;
	private String description;
	private String expression;

	private String creatorId;
	private int status;

	@Override
	public String toString() {
		return "DomainRegular [name=" + name + ", description=" + description + ", expression=" + expression
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

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
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
