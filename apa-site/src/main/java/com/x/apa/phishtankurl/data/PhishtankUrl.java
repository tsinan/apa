package com.x.apa.phishtankurl.data;

import com.x.apa.common.data.BaseData;

/**
 * @author liumeng
 */
public class PhishtankUrl extends BaseData {

	private String phishId;
	private String url;
	private String phishDetailUrl;
	private String submissionTime;
	private String verified;
	private String verificationTime;
	private String online;
	private String target;
	private String recordJson;

	@Override
	public String toString() {
		return "PhishtankUrl [phishId=" + phishId + ", url=" + url + ", phishDetailUrl=" + phishDetailUrl
				+ ", submissionTime=" + submissionTime + ", verified=" + verified + ", verificationTime="
				+ verificationTime + ", online=" + online + ", target=" + target + ", recordJson=" + recordJson
				+ ", toString()=" + super.toString() + "]";
	}

	public String getPhishId() {
		return phishId;
	}

	public void setPhishId(String phishId) {
		this.phishId = phishId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPhishDetailUrl() {
		return phishDetailUrl;
	}

	public void setPhishDetailUrl(String phishDetailUrl) {
		this.phishDetailUrl = phishDetailUrl;
	}

	public String getSubmissionTime() {
		return submissionTime;
	}

	public void setSubmissionTime(String submissionTime) {
		this.submissionTime = submissionTime;
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public String getVerificationTime() {
		return verificationTime;
	}

	public void setVerificationTime(String verificationTime) {
		this.verificationTime = verificationTime;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getRecordJson() {
		return recordJson;
	}

	public void setRecordJson(String recordJson) {
		this.recordJson = recordJson;
	}

}
