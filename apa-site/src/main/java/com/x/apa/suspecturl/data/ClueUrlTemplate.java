package com.x.apa.suspecturl.data;

import com.x.apa.common.data.BaseData;

/**
 * @author liumeng
 */
public class ClueUrlTemplate extends BaseData {

	private String content;

	@Override
	public String toString() {
		return "ClueUrlTemplate [content=" + content + ", toString()=" + super.toString() + "]";
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
