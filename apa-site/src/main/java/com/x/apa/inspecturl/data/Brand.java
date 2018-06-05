package com.x.apa.inspecturl.data;

import com.x.apa.common.data.BaseData;

/**
 * @author liumeng
 */
public class Brand extends BaseData {

	private String name;
	private int category;
	private String description;

	private String creatorId;

	@Override
	public String toString() {
		return "Brand [name=" + name + ", category=" + category + ", description=" + description + ", creatorId="
				+ creatorId + ", toString()=" + super.toString() + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
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

}
