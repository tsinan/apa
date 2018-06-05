package com.x.apa.inspecturl.data;

import java.util.Date;

import com.x.apa.common.data.BaseData;

/**
 * @author liumeng
 */
public class InspectTrace extends BaseData {

	private String urlId;
	private String url;
	private String ipAddress;

	private int inspectLevel;
	private String inspectKeyword;
	private float inspectKeywordScore;
	private String inspectMessage;

	private int inspectStatus;
	private Date inspectTime;
	private Date inspectNextTime;
	private int inspectTimes;

	private int activeDuration;

	private int httpStatusCode;
	private String httpHeaders;
	private String html;
	private String text;
	private String textTokens;
	private String textOther;

	@Override
	public String toString() {
		return "InspectTrace [urlId=" + urlId + ", url=" + url + ", ipAddress=" + ipAddress + ", inspectLevel="
				+ inspectLevel + ", inspectKeyword=" + inspectKeyword + ", inspectKeywordScore=" + inspectKeywordScore
				+ ", inspectMessage=" + inspectMessage + ", inspectStatus=" + inspectStatus + ", inspectTime="
				+ inspectTime + ", inspectNextTime=" + inspectNextTime + ", inspectTimes=" + inspectTimes
				+ ", activeDuration=" + activeDuration + ", httpStatusCode=" + httpStatusCode + ", httpHeaders="
				+ httpHeaders + ", html=" + html + ", text=" + text + ", textTokens=" + textTokens + ", textOther="
				+ textOther + ", toString()=" + super.toString() + "]";
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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getInspectKeyword() {
		return inspectKeyword;
	}

	public void setInspectKeyword(String inspectKeyword) {
		this.inspectKeyword = inspectKeyword;
	}

	public float getInspectKeywordScore() {
		return inspectKeywordScore;
	}

	public void setInspectKeywordScore(float inspectKeywordScore) {
		this.inspectKeywordScore = inspectKeywordScore;
	}

	public int getInspectLevel() {
		return inspectLevel;
	}

	public void setInspectLevel(int inspectLevel) {
		this.inspectLevel = inspectLevel;
	}

	public String getInspectMessage() {
		return inspectMessage;
	}

	public void setInspectMessage(String inspectMessage) {
		this.inspectMessage = inspectMessage;
	}

	public int getInspectStatus() {
		return inspectStatus;
	}

	public void setInspectStatus(int inspectStatus) {
		this.inspectStatus = inspectStatus;
	}

	public Date getInspectTime() {
		return inspectTime;
	}

	public void setInspectTime(Date inspectTime) {
		this.inspectTime = inspectTime;
	}

	public Date getInspectNextTime() {
		return inspectNextTime;
	}

	public void setInspectNextTime(Date inspectNextTime) {
		this.inspectNextTime = inspectNextTime;
	}

	public int getInspectTimes() {
		return inspectTimes;
	}

	public void setInspectTimes(int inspectTimes) {
		this.inspectTimes = inspectTimes;
	}

	public int getActiveDuration() {
		return activeDuration;
	}

	public void setActiveDuration(int activeDuration) {
		this.activeDuration = activeDuration;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTextTokens() {
		return textTokens;
	}

	public void setTextTokens(String textTokens) {
		this.textTokens = textTokens;
	}

	public String getHttpHeaders() {
		return httpHeaders;
	}

	public void setHttpHeaders(String httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

	public String getTextOther() {
		return textOther;
	}

	public void setTextOther(String textOther) {
		this.textOther = textOther;
	}

}
