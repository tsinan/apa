package com.x.apa.phishtankurl.service;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.phishtankurl.data.PhishtankUrl;

/**
 * @author liumeng
 */
public interface PhishtankUrlService {

	Page<PhishtankUrl> queryPhishtankUrls(PageRequest page);

	String queryLatestVerificationTime();

	PhishtankUrl createPhishtankUrl(PhishtankUrl phishtankUrl);
}
