package com.x.apa.suspecturl.service;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.suspecturl.data.TrustDomain;

/**
 * @author liumeng
 */
public interface TrustDomainService {

	boolean isTrustDomain(String domain);

	Page<TrustDomain> queryTrustDomains(PageRequest page);

	TrustDomain queryTrustDomain(String id);

	TrustDomain createTrustDomain(TrustDomain trustDomain);

	int updateTrustDomain(TrustDomain trustDomain);

	int changeTrustDomainStatus(String id, int status);

	int deleteTrustDomain(String id);
}
