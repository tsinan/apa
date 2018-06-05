package com.x.apa.loader.dao;

import java.util.List;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.loader.data.SensitiveDomain;

/**
 * @author liumeng
 */
public interface SensitiveDomainDao {

	Page<SensitiveDomain> querySensitiveDomains(PageRequest page, String startTime, String endTime, String domainLike);

	List<SensitiveDomain> querySensitiveDomains(String startTime, String endTime, String domainLike);

	SensitiveDomain querySensitiveDomain(String id);

	SensitiveDomain querySensitiveDomainByDomainName(String domainName);

	SensitiveDomain saveSensitiveDomain(SensitiveDomain sensitiveDomain);

	int updateSensitiveDomain(SensitiveDomain sensitiveDomain);
	
	int updateSensitiveDomainWhois(SensitiveDomain sensitiveDomain);

	int deleteLearningSensitiveDomain();

	int deleteSensitiveDomain(String id);
}
