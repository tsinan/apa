package com.x.apa.common.data;

import java.util.Date;

/**
 * @author liumeng
 */
public class RawDomain extends BaseData {

	private String domainName;
	private Date registrationDate;
	private int isSensitive;

	private String registrantName;
	private String registrantEmail;
	private String registrantPhone;
	private String domainRegistrarId;

	private String recordJson;

	@Override
	public String toString() {
		return "RawDomain [domainName=" + domainName + ", registrationDate=" + registrationDate + ", isSensitive="
				+ isSensitive + ", registrantName=" + registrantName + ", registrantEmail=" + registrantEmail
				+ ", registrantPhone=" + registrantPhone + ", domainRegistrarId=" + domainRegistrarId + ", recordJson="
				+ recordJson + ", toString()=" + super.toString() + "]";
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public int getIsSensitive() {
		return isSensitive;
	}

	public void setIsSensitive(int isSensitive) {
		this.isSensitive = isSensitive;
	}

	public String getRegistrantName() {
		return registrantName;
	}

	public void setRegistrantName(String registrantName) {
		this.registrantName = registrantName;
	}

	public String getRegistrantEmail() {
		return registrantEmail;
	}

	public void setRegistrantEmail(String registrantEmail) {
		this.registrantEmail = registrantEmail;
	}

	public String getRegistrantPhone() {
		return registrantPhone;
	}

	public void setRegistrantPhone(String registrantPhone) {
		this.registrantPhone = registrantPhone;
	}

	public String getDomainRegistrarId() {
		return domainRegistrarId;
	}

	public void setDomainRegistrarId(String domainRegistrarId) {
		this.domainRegistrarId = domainRegistrarId;
	}

	public String getRecordJson() {
		return recordJson;
	}

	public void setRecordJson(String recordJson) {
		this.recordJson = recordJson;
	}

}
