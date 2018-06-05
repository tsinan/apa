package com.x.apa.suspectdomain.service;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.suspectdomain.data.DomainRegular;

/**
 * @author liumeng
 */
public interface DomainRegularService {

	Page<DomainRegular> queryDomainRegulars(PageRequest page);

	void createDomainRegular(DomainRegular domainRegular);
	
	int updateDomainRegular(DomainRegular domainRegular);

	int changeDomainRegularStatus(String id, int status);

	int deleteDomainRegular(String id);
	
}
