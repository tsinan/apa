package com.x.apa.webportal.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author liumeng
 */
public interface DashboardDao {

	List<Map<String, String>> queryDataCountSummary();

	List<Map<String, String>> queryCrawlerRunStatus();

	List<Map<String, String>> queryRawDomainCountSummary();

	Map<String, String> queryCrawlerLatestRunStatus(Date date);

	List<Map<String, String>> queryRecentFoundSuspectUrl(int fetchNum);

	List<Map<String, String>> queryRecentFoundSuspectDomain(int fetchNum);

	List<Map<String, String>> queryPhishtankRunStatus();
}
