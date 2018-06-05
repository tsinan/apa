package com.x.apa.inspecturl.data;

import java.util.Date;

import com.x.apa.common.data.BaseData;

/**
 * @author liumeng
 */
public class InspectEvent extends BaseData {

	private int category;
	private String urlId;
	private String url;

	private String keywordFrom;
	private String keywordTo;
	private float keywordScoreFrom;
	private float keywordScoreTo;
	private String messageFrom;
	private String messageTo;

	private int statusFrom;
	private int statusTo;
	private Date timeFrom;
	private Date timeTo;

	private int progress;

	@Override
	public String toString() {
		return "InpsectEvent [category=" + category + ", urlId=" + urlId + ", url=" + url + ", keywordFrom="
				+ keywordFrom + ", keywordTo=" + keywordTo + ", keywordScoreFrom=" + keywordScoreFrom
				+ ", keywordScoreTo=" + keywordScoreTo + ", messageFrom=" + messageFrom + ", messageTo=" + messageTo
				+ ", statusFrom=" + statusFrom + ", statusTo=" + statusTo + ", timeFrom=" + timeFrom + ", timeTo="
				+ timeTo + ", progress=" + progress + ", toString()=" + super.toString() + "]";
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKeywordFrom() {
		return keywordFrom;
	}

	public void setKeywordFrom(String keywordFrom) {
		this.keywordFrom = keywordFrom;
	}

	public String getKeywordTo() {
		return keywordTo;
	}

	public void setKeywordTo(String keywordTo) {
		this.keywordTo = keywordTo;
	}

	public float getKeywordScoreFrom() {
		return keywordScoreFrom;
	}

	public void setKeywordScoreFrom(float keywordScoreFrom) {
		this.keywordScoreFrom = keywordScoreFrom;
	}

	public float getKeywordScoreTo() {
		return keywordScoreTo;
	}

	public void setKeywordScoreTo(float keywordScoreTo) {
		this.keywordScoreTo = keywordScoreTo;
	}

	public String getMessageFrom() {
		return messageFrom;
	}

	public void setMessageFrom(String messageFrom) {
		this.messageFrom = messageFrom;
	}

	public String getMessageTo() {
		return messageTo;
	}

	public void setMessageTo(String messageTo) {
		this.messageTo = messageTo;
	}

	public int getStatusFrom() {
		return statusFrom;
	}

	public void setStatusFrom(int statusFrom) {
		this.statusFrom = statusFrom;
	}

	public int getStatusTo() {
		return statusTo;
	}

	public void setStatusTo(int statusTo) {
		this.statusTo = statusTo;
	}

	public Date getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(Date timeFrom) {
		this.timeFrom = timeFrom;
	}

	public Date getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(Date timeTo) {
		this.timeTo = timeTo;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

}
