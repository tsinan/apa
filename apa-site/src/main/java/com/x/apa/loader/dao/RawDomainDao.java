package com.x.apa.loader.dao;

import java.util.Date;
import java.util.List;

import com.x.apa.common.data.RawDomain;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;

/**
 * @author liumeng
 */
public interface RawDomainDao {

	RawDomain queryLatestRawDomains();

	Page<RawDomain> queryRawDomains(PageRequest page, String startTime, String endTime, String domainLike);

	List<RawDomain> queryRawDomains(String startTime, String endTime, String domainLike);

	List<RawDomain> queryRawDomains(int startPos, int size, Date date);

	RawDomain saveRawDomain(RawDomain rawDomain);

	int changeRawDomainSensitive(String domainName, int isSensitive);
}
