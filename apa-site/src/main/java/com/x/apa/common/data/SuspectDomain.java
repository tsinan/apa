package com.x.apa.common.data;

/**
 * @author liumeng
 */
public class SuspectDomain extends BaseData {

	private String domainName;
	private String suspectMatch;
	private String suspectMatchObjId;
	private String suspectMatchObjText;
	private String rawDomainId;

	@Override
	public String toString() {
		return "SuspectMatch [domainName=" + domainName + ", suspectMatch=" + suspectMatch + ", suspectMatchObjId="
				+ suspectMatchObjId + ", suspectMatchObjText=" + suspectMatchObjText + ", rawDomainId=" + rawDomainId
				+ ", toString()=" + super.toString() + "]";
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getSuspectMatch() {
		return suspectMatch;
	}

	public void setSuspectMatch(String suspectMatch) {
		this.suspectMatch = suspectMatch;
	}

	public String getSuspectMatchObjId() {
		return suspectMatchObjId;
	}

	public void setSuspectMatchObjId(String suspectMatchObjId) {
		this.suspectMatchObjId = suspectMatchObjId;
	}

	public String getSuspectMatchObjText() {
		return suspectMatchObjText;
	}

	public void setSuspectMatchObjText(String suspectMatchObjText) {
		this.suspectMatchObjText = suspectMatchObjText;
	}

	public String getRawDomainId() {
		return rawDomainId;
	}

	public void setRawDomainId(String rawDomainId) {
		this.rawDomainId = rawDomainId;
	}

}
