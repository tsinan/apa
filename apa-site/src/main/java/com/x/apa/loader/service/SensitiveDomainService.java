package com.x.apa.loader.service;

import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.loader.data.SensitiveDomain;

/**
 * @author liumeng
 */
public interface SensitiveDomainService {
	
	List<SensitiveDomain> querySensitiveDomains();
	
	Set<String> querySensitiveDomainNames();

	Page<SensitiveDomain> querySensitiveDomains(PageRequest page, String startTime, String endTime, String domainLike);

	HSSFWorkbook exportSensitiveDomain(String startTime, String endTime, String domainLike);

	SensitiveDomain querySensitiveDomain(String id);

	SensitiveDomain createSensitiveDomain(SensitiveDomain sensitiveDomain);

	int updateSensitiveDomain(SensitiveDomain sensitiveDomain);
	
	int updateSensitiveDomainWhois(SensitiveDomain sensitiveDomain);

	int deleteLearningSensitiveDomain();

	int deleteSensitiveDomain(String id);
}
