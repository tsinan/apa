package com.x.apa.suspecturl.data;

import com.x.apa.common.data.BaseData;

/**
 * @author liumeng
 */
public class TagRule extends BaseData {

	private String tagId;
	private String rule;
	private String description;
	private String creatorId;

	@Override
	public String toString() {
		return "TagRule [tagId=" + tagId + ", rule=" + rule + ", description=" + description + ", creatorId="
				+ creatorId + ", toString()=" + super.toString() + "]";
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
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
