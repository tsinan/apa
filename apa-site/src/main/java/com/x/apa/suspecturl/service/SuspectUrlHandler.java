package com.x.apa.suspecturl.service;

import com.x.apa.suspecturl.data.ClueUrl;

/**
 * @author liumeng
 */
public class SuspectUrlHandler {

	private ClueUrl clueUrl;
	private ClueUrlService clueUrlService;
	private SuspectUrlService suspectUrlService;
	private TrustDomainService trustDomainService;

	public SuspectUrlHandler(ClueUrl clueUrl, ClueUrlService clueUrlService, SuspectUrlService suspectUrlService,
			TrustDomainService trustDomainService) {
		super();
		this.clueUrl = clueUrl;
		this.clueUrlService = clueUrlService;
		this.suspectUrlService = suspectUrlService;
		this.trustDomainService = trustDomainService;
	}

	public ClueUrl getClueUrl() {
		return clueUrl;
	}

	public void setClueUrl(ClueUrl clueUrl) {
		this.clueUrl = clueUrl;
	}

	public ClueUrlService getClueUrlService() {
		return clueUrlService;
	}

	public void setClueUrlService(ClueUrlService clueUrlService) {
		this.clueUrlService = clueUrlService;
	}

	public SuspectUrlService getSuspectUrlService() {
		return suspectUrlService;
	}

	public void setSuspectUrlService(SuspectUrlService suspectUrlService) {
		this.suspectUrlService = suspectUrlService;
	}

	public TrustDomainService getTrustDomainService() {
		return trustDomainService;
	}

	public void setTrustDomainService(TrustDomainService trustDomainService) {
		this.trustDomainService = trustDomainService;
	}

}
