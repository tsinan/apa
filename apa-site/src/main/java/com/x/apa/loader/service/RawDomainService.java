package com.x.apa.loader.service;

import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.x.apa.common.data.RawDomain;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;

/**
 * @author liumeng
 */
public interface RawDomainService {

	Date queryLatestRegistrationDate();

	Page<RawDomain> queryRawDomains(PageRequest page, String startTime, String endTime, String domainLike);

	List<RawDomain> queryRawDomains(int startPos, int size, Date day);

	HSSFWorkbook exportRawDomain(String startTime, String endTime, String domainLike);

	RawDomain createRawDomain(RawDomain rawDomain);

	int changeRawDomainSensitive(String domainName, int isSensitive);
}
