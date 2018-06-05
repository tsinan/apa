package com.x.apa.suspecturl.data;

import com.x.apa.common.data.BaseData;

/**
 * @author liumeng
 */
public class SuspectUrl extends BaseData {

	private String url;
	private int verify;

	private String tagId;
	private String tagRule;
	private float tagRuleScore;

	private String rawDomainId;
	private String suspectDomainId;
	private String clueUrlId;

	private String tagName;

	@Override
	public String toString() {
		return "SuspectUrl [url=" + url + ", verify=" + verify + ", tagId=" + tagId + ", tagRule=" + tagRule
				+ ", tagRuleScore=" + tagRuleScore + ", rawDomainId=" + rawDomainId + ", suspectDomainId="
				+ suspectDomainId + ", clueUrlId=" + clueUrlId + ", toString()=" + super.toString() + "]";
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getVerify() {
		return verify;
	}

	public void setVerify(int verify) {
		this.verify = verify;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTagRule() {
		return tagRule;
	}

	public void setTagRule(String tagRule) {
		this.tagRule = tagRule;
	}

	public float getTagRuleScore() {
		return tagRuleScore;
	}

	public void setTagRuleScore(float tagRuleScore) {
		this.tagRuleScore = tagRuleScore;
	}

	public String getRawDomainId() {
		return rawDomainId;
	}

	public void setRawDomainId(String rawDomainId) {
		this.rawDomainId = rawDomainId;
	}

	public String getSuspectDomainId() {
		return suspectDomainId;
	}

	public void setSuspectDomainId(String suspectDomainId) {
		this.suspectDomainId = suspectDomainId;
	}

	public String getClueUrlId() {
		return clueUrlId;
	}

	public void setClueUrlId(String clueUrlId) {
		this.clueUrlId = clueUrlId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

}
