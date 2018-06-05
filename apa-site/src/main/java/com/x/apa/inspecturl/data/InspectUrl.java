package com.x.apa.inspecturl.data;

import java.util.Date;

import com.x.apa.common.data.BaseData;

/**
 * @author liumeng
 */
public class InspectUrl extends BaseData {

	private int category;
	private String url;
	private String ipAddress;

	private int inspectLevel;
	private String inspectKeyword;
	private float inspectKeywordScore;
	private int inspectStatus;
	private String inspectMessage;

	private Date inspectTime;
	private Date inspectNextTime;
	private int inspectTimes;

	private int activeDuration;
	private String brandId;
	private String incidentNo;

	private String whoisRegName;
	private String whoisRegEmail;
	private String whoisWebhost;

	private String creatorId;

	private String brandName;

	private int pageOutlinkNum; // seed url的外链数量
	private int pageImageNum; // seed image的数量
	private int redirectNum; // 发生的重定向次数

	@Override
	public String toString() {
		return "InspectUrl [category=" + category + ", url=" + url + ", ipAddress=" + ipAddress + ", inspectLevel="
				+ inspectLevel + ", inspectKeyword=" + inspectKeyword + ", inspectKeywordScore=" + inspectKeywordScore
				+ ", inspectStatus=" + inspectStatus + ", inspectMessage=" + inspectMessage + ", inspectTime="
				+ inspectTime + ", inspectNextTime=" + inspectNextTime + ", inspectTimes=" + inspectTimes
				+ ", activeDuration=" + activeDuration + ", brandId=" + brandId + ", incidentNo=" + incidentNo
				+ ", whoisRegName=" + whoisRegName + ", whoisRegEmail=" + whoisRegEmail + ", whoisWebhost="
				+ whoisWebhost + ", creatorId=" + creatorId + ", brandName=" + brandName + ", toString()="
				+ super.toString() + "]";
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

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
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

	public int getInspectStatus() {
		return inspectStatus;
	}

	public void setInspectStatus(int inspectStatus) {
		this.inspectStatus = inspectStatus;
	}

	public String getInspectMessage() {
		return inspectMessage;
	}

	public void setInspectMessage(String inspectMessage) {
		this.inspectMessage = inspectMessage;
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

	public String getWhoisRegName() {
		return whoisRegName;
	}

	public void setWhoisRegName(String whoisRegName) {
		this.whoisRegName = whoisRegName;
	}

	public String getWhoisRegEmail() {
		return whoisRegEmail;
	}

	public void setWhoisRegEmail(String whoisRegEmail) {
		this.whoisRegEmail = whoisRegEmail;
	}

	public String getWhoisWebhost() {
		return whoisWebhost;
	}

	public void setWhoisWebhost(String whoisWebhost) {
		this.whoisWebhost = whoisWebhost;
	}

	public String getIncidentNo() {
		return incidentNo;
	}

	public void setIncidentNo(String incidentNo) {
		this.incidentNo = incidentNo;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public int getPageOutlinkNum() {
		return pageOutlinkNum;
	}

	public void setPageOutlinkNum(int pageOutlinkNum) {
		this.pageOutlinkNum = pageOutlinkNum;
	}

	public int getPageImageNum() {
		return pageImageNum;
	}

	public void setPageImageNum(int pageImageNum) {
		this.pageImageNum = pageImageNum;
	}

	public int getRedirectNum() {
		return redirectNum;
	}

	public void setRedirectNum(int redirectNum) {
		this.redirectNum = redirectNum;
	}

}
