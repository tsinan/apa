package com.x.apa.suspecturl.data;

import java.util.List;

import com.x.apa.common.data.BaseData;

import edu.uci.ics.crawler4j.parser.HtmlParseData;

/**
 * @author liumeng
 */
public class ClueUrl extends BaseData {

	private String url;
	private int progress;

	private String rawDomainId;
	private String suspectDomainId;

	private int pageOutlinkNum; // seed url的外链数量
	private int pageOutsitelinkNum; // seed url链接到外站的外链数量
	private int pageTrustlinkNum; // seed url链接到可信站点的数量
	private int pageImageNum; // seed image的数量
	private int pageShouldVisitNum = 1; // seed 通过检查/需要抓取的页面数量，起始值为1因为首页也需要访问

	private int pageVisitNum;
	private int pageVisitSize;
	private int nohtmlVisitNum;
	private int nohtmlVisitSize;

	private int redirectNum; // 发生的重定向次数

	private List<HtmlParseData> htmlDataList;

	@Override
	public String toString() {
		return "ClueUrl [url=" + url + ", progress=" + progress + ", rawDomainId=" + rawDomainId + ", suspectDomainId="
				+ suspectDomainId + ", pageOutlinkNum=" + pageOutlinkNum + ", pageOutsitelinkNum=" + pageOutsitelinkNum
				+ ", pageTrustlinkNum=" + pageTrustlinkNum + ", pageImageNum=" + pageImageNum + ", pageShouldVisitNum="
				+ pageShouldVisitNum + ", pageVisitNum=" + pageVisitNum + ", pageVisitSize=" + pageVisitSize
				+ ", nohtmlVisitNum=" + nohtmlVisitNum + ", nohtmlVisitSize=" + nohtmlVisitSize + ", redirectNum="
				+ redirectNum + ", toString()=" + super.toString() + "]";
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
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

	public int getPageOutlinkNum() {
		return pageOutlinkNum;
	}

	public void setPageOutlinkNum(int pageOutlinkNum) {
		this.pageOutlinkNum = pageOutlinkNum;
	}

	public int getPageOutsitelinkNum() {
		return pageOutsitelinkNum;
	}

	public void setPageOutsitelinkNum(int pageOutsitelinkNum) {
		this.pageOutsitelinkNum = pageOutsitelinkNum;
	}

	public int getPageTrustlinkNum() {
		return pageTrustlinkNum;
	}

	public void setPageTrustlinkNum(int pageTrustlinkNum) {
		this.pageTrustlinkNum = pageTrustlinkNum;
	}

	public int getPageImageNum() {
		return pageImageNum;
	}

	public void setPageImageNum(int pageImageNum) {
		this.pageImageNum = pageImageNum;
	}

	public int getPageShouldVisitNum() {
		return pageShouldVisitNum;
	}

	public void setPageShouldVisitNum(int pageShouldVisitNum) {
		this.pageShouldVisitNum = pageShouldVisitNum;
	}

	public int getPageVisitNum() {
		return pageVisitNum;
	}

	public void setPageVisitNum(int pageVisitNum) {
		this.pageVisitNum = pageVisitNum;
	}

	public int getPageVisitSize() {
		return pageVisitSize;
	}

	public void setPageVisitSize(int pageVisitSize) {
		this.pageVisitSize = pageVisitSize;
	}

	public int getNohtmlVisitNum() {
		return nohtmlVisitNum;
	}

	public void setNohtmlVisitNum(int nohtmlVisitNum) {
		this.nohtmlVisitNum = nohtmlVisitNum;
	}

	public int getNohtmlVisitSize() {
		return nohtmlVisitSize;
	}

	public void setNohtmlVisitSize(int nohtmlVisitSize) {
		this.nohtmlVisitSize = nohtmlVisitSize;
	}

	public int getRedirectNum() {
		return redirectNum;
	}

	public void setRedirectNum(int redirectNum) {
		this.redirectNum = redirectNum;
	}

	public List<HtmlParseData> getHtmlDataList() {
		return htmlDataList;
	}

	public void setHtmlDataList(List<HtmlParseData> htmlDataList) {
		this.htmlDataList = htmlDataList;
	}

}
