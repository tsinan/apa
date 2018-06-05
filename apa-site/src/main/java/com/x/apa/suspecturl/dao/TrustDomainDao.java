package com.x.apa.suspecturl.dao;

import java.util.List;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.suspecturl.data.TrustDomain;

/**
 * @author liumeng
 */
public interface TrustDomainDao {

	List<TrustDomain> queryTrustDomains(boolean openOnly);

	Page<TrustDomain> queryTrustDomains(PageRequest page);

	TrustDomain queryTrustDomain(String id);

	TrustDomain saveTrustDomain(TrustDomain trustDomain);

	int updateTrustDomain(TrustDomain trustDomain);

	int changeTrustDomainStatus(String trustDomainId, int status);

	int deleteTrustDomain(String id);

}
