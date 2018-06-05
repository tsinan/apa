package com.x.apa.inspecturl.service;

import java.util.List;
import org.apache.http.Header;

import com.x.apa.inspecturl.data.InspectUrl;
import com.x.apa.suspecturl.service.TrustDomainService;

import edu.uci.ics.crawler4j.parser.HtmlParseData;

/**
 * @author liumeng
 */
public class InspectUrlHandler {

	private InspectUrl inspectUrl;
	private InspectUrlService inspectUrlService;
	private TrustDomainService trustDomainService;
	private int statusCode;
	private Header[] headers;
	private List<HtmlParseData> htmlDataList;

	public InspectUrlHandler(InspectUrl inspectUrl, InspectUrlService inspectUrlService,
			TrustDomainService trustDomainService) {
		super();
		this.inspectUrl = inspectUrl;
		this.inspectUrlService = inspectUrlService;
		this.trustDomainService = trustDomainService;
	}

	public InspectUrl getInspectUrl() {
		return inspectUrl;
	}

	public void setInspectUrl(InspectUrl inspectUrl) {
		this.inspectUrl = inspectUrl;
	}

	public InspectUrlService getInspectUrlService() {
		return inspectUrlService;
	}

	public void setInspectUrlService(InspectUrlService inspectUrlService) {
		this.inspectUrlService = inspectUrlService;
	}

	public TrustDomainService getTrustDomainService() {
		return trustDomainService;
	}

	public void setTrustDomainService(TrustDomainService trustDomainService) {
		this.trustDomainService = trustDomainService;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Header[] getHeaders() {
		return headers;
	}

	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}

	public List<HtmlParseData> getHtmlDataList() {
		return htmlDataList;
	}

	public void setHtmlDataList(List<HtmlParseData> htmlDataList) {
		this.htmlDataList = htmlDataList;
	}

}
