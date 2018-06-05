package com.x.apa.loader.data;

import java.util.Date;

import com.x.apa.common.data.BaseData;

/**
 * @author liumeng
 */
public class SensitiveDomain extends BaseData {

	private int category;
	private String domainName;

	private String sensitiveWordName;
	private String sensitiveWord;
	private int lcsLength;

	private Date registrationDate;
	private String whoisRegName;
	private String whoisRegEmail;

	@Override
	public String toString() {
		return "SensitiveDomain [category=" + category + ", domainName=" + domainName + ", sensitiveWordName="
				+ sensitiveWordName + ", sensitiveWord=" + sensitiveWord + ", lcsLength=" + lcsLength
				+ ", registrationDate=" + registrationDate + ", whoisRegName=" + whoisRegName + ", whoisRegEmail="
				+ whoisRegEmail + ", toString()=" + super.toString() + "]";
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
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

	public String getSensitiveWordName() {
		return sensitiveWordName;
	}

	public void setSensitiveWordName(String sensitiveWordName) {
		this.sensitiveWordName = sensitiveWordName;
	}

	public String getSensitiveWord() {
		return sensitiveWord;
	}

	public void setSensitiveWord(String sensitiveWord) {
		this.sensitiveWord = sensitiveWord;
	}

	public int getLcsLength() {
		return lcsLength;
	}

	public void setLcsLength(int lcsLength) {
		this.lcsLength = lcsLength;
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

}
