package com.x.apa.suspectdomain.dao;

import java.util.List;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.suspectdomain.data.DomainRegular;

/**
 * @author liumeng
 */
public interface DomainRegularDao {

	List<DomainRegular> queryDomainRegulars(boolean openedOnly);
	
	Page<DomainRegular> queryDomainRequlars(PageRequest page);
	
	DomainRegular queryDomainReqular(String id);
	
	DomainRegular saveDomainRegular(DomainRegular regular);
	
	int updateDomainRegular(DomainRegular regular);
	
	int changeDomainRegularStatus(String id, int status);
	
	int deleteDomainRegular(String id);
	
}
