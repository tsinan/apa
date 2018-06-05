package com.x.apa.suspecturl.dao;

import java.util.List;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.suspecturl.data.PoisonDomain;

/**
 * @author liumeng
 */
public interface PoisonDomainDao {

	List<PoisonDomain> queryPoisonDomains(boolean openOnly);

	Page<PoisonDomain> queryPoisonDomains(PageRequest page);

	PoisonDomain queryPoisonDomain(String id);

	PoisonDomain savePoisonDomain(PoisonDomain poisonDomain);

	int updatePoisonDomain(PoisonDomain poisonDomain);

	int changePoisonDomainStatus(String poisonDomainId, int status);

	int deletePoisonDomain(String id);

}
