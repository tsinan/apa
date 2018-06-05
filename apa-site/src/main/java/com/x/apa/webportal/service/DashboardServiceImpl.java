package com.x.apa.webportal.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.common.util.DateUtils;
import com.x.apa.webportal.dao.DashboardDao;

/**
 * @author liumeng
 */
@Component
public class DashboardServiceImpl implements DashboardService, Constant {

	@Autowired
	private DashboardDao dashboardDao;

	/**
	 * @see com.x.apa.webportal.service.DashboardService#queryDataCountSummary()
	 */
	@Override
	public List<Map<String, String>> queryDataCountSummary() {
		return dashboardDao.queryDataCountSummary();
	}

	/**
	 * @see com.x.apa.webportal.service.DashboardService#queryCrawlerRunStatus()
	 */
	@Override
	public List<Map<String, String>> queryCrawlerRunStatus() {
		return dashboardDao.queryCrawlerRunStatus();
	}

	/**
	 * @see com.x.apa.webportal.service.DashboardService#queryRawDomainCountSummary()
	 */
	@Override
	public List<Map<String, String>> queryRawDomainCountSummary() {
		return dashboardDao.queryRawDomainCountSummary();
	}

	/**
	 * @see com.x.apa.webportal.service.DashboardService#queryCrawlerLatestRunStatus(java.lang.String)
	 */
	@Override
	public Map<String, String> queryCrawlerLatestRunStatus(String day) {
		Date date = new Date();
		if (StringUtils.equalsIgnoreCase(day, "yesterday")) {
			date = DateUtils.beforeDayDate(date, 1);
		}
		return dashboardDao.queryCrawlerLatestRunStatus(date);
	}

	/**
	 * @see com.x.apa.webportal.service.DashboardService#queryRecentFoundSuspectUrl(int)
	 */
	@Override
	public List<Map<String, String>> queryRecentFoundSuspectUrl(int fetchNum) {
		return dashboardDao.queryRecentFoundSuspectUrl(fetchNum);
	}

	/**
	 * @see com.x.apa.webportal.service.DashboardService#queryRecentFoundSuspectDomain(int)
	 */
	@Override
	public List<Map<String, String>> queryRecentFoundSuspectDomain(int fetchNum) {
		return dashboardDao.queryRecentFoundSuspectDomain(fetchNum);
	}

	/**
	 * @see com.x.apa.webportal.service.DashboardService#queryPhishtankRunStatus()
	 */
	@Override
	public List<Map<String, String>> queryPhishtankRunStatus() {
		return dashboardDao.queryPhishtankRunStatus();
	}
}
