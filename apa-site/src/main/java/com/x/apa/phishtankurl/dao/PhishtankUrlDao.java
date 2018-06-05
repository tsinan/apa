package com.x.apa.phishtankurl.dao;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.phishtankurl.data.PhishtankUrl;

/**
 * @author liumeng
 */
public interface PhishtankUrlDao {

	Page<PhishtankUrl> queryPhishtankUrls(PageRequest page);

	PhishtankUrl queryLatestVerifiedPhishtankUrl();

	PhishtankUrl savePhishtankUrl(PhishtankUrl phishtankValidUrl);
}
