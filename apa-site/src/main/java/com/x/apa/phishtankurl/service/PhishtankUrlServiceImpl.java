package com.x.apa.phishtankurl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.phishtankurl.dao.PhishtankUrlDao;
import com.x.apa.phishtankurl.data.PhishtankUrl;

/**
 * @author liumeng
 */
@Component
public class PhishtankUrlServiceImpl implements PhishtankUrlService, Constant {

	@Autowired
	private PhishtankUrlDao phishtankUrlDao;

	/**
	 * @see com.x.apa.phishtankurl.service.PhishtankUrlService#queryPhishtankUrls(com.x.apa.common.pageable.PageRequest)
	 */
	@Override
	public Page<PhishtankUrl> queryPhishtankUrls(PageRequest page) {
		return phishtankUrlDao.queryPhishtankUrls(page);
	}

	/**
	 * @see com.x.apa.phishtankurl.service.PhishtankUrlService#queryLatestVerificationTime()
	 */
	@Override
	public String queryLatestVerificationTime() {
		String latestVerificationTime = phishtankUrlDao.queryLatestVerifiedPhishtankUrl().getVerificationTime();
		return latestVerificationTime == null ? "" : latestVerificationTime;
	}

	/**
	 * @see com.x.apa.phishtankurl.service.PhishtankUrlService#createPhishtankUrl(com.x.apa.phishtankurl.data.PhishtankUrl)
	 */
	@Override
	public PhishtankUrl createPhishtankUrl(PhishtankUrl phishtankUrl) {

		return phishtankUrlDao.savePhishtankUrl(phishtankUrl);
	}

}
