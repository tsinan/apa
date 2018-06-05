package com.x.apa.suspecturl.service;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.suspecturl.data.PoisonDomain;

/**
 * @author liumeng
 */
public interface PoisonDomainService {

	Page<PoisonDomain> queryPoisonDomains(PageRequest page);

	PoisonDomain queryPoisonDomain(String id);

	PoisonDomain createPoisonDomain(PoisonDomain poisonDomain);

	int updatePoisonDomain(PoisonDomain poisonDomain);

	int changePoisonDomainStatus(String id, int status);

	int deletePoisonDomain(String id);
}
