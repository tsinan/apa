package com.x.apa.webportal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.x.apa.common.Constant;
import com.x.apa.common.util.DateUtils;

/**
 * @author liumeng
 */
@Component
public class DashboardDaoImpl implements DashboardDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.webportal.dao.DashboardDao#queryDataCountSummary()
	 */
	@Override
	public List<Map<String, String>> queryDataCountSummary() {
		StringBuilder sql = new StringBuilder();
		sql.append("select 'ld_raw_domain' as table_name, count(*) as data_count from v_ld_raw_domain");
		sql.append(" UNION ");
		sql.append("select 'sd_suspect_domain' as table_name, count(*) as data_count from sd_suspect_domain");
		sql.append(" UNION ");
		sql.append("select 'su_clue_url' as table_name, count(*) as data_count from su_clue_url");
		sql.append(" UNION ");
		sql.append("select 'su_suspect_url' as table_name, count(*) as data_count from su_suspect_url");

		final List<Map<String, String>> result = Lists.newArrayList();
		namedParamJdbcTemplate.query(sql.toString(), new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {

				Map<String, String> entry = Maps.newLinkedHashMap();
				entry.put("tableName", rs.getString("table_name"));
				entry.put("dataCount", rs.getString("data_count"));
				result.add(entry);
				return 0;
			}
		});
		return result;
	}

	/**
	 * @see com.x.apa.webportal.dao.DashboardDao#queryCrawlerRunStatus()
	 */
	@Override
	public List<Map<String, String>> queryCrawlerRunStatus() {
		StringBuilder sql = new StringBuilder();
		sql.append("select progress,count(*) as data_count from su_clue_url group by progress;");

		final List<Map<String, String>> result = Lists.newArrayList();
		namedParamJdbcTemplate.query(sql.toString(), new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {

				Map<String, String> entry = Maps.newLinkedHashMap();
				entry.put("progress", rs.getString("progress"));
				entry.put("dataCount", rs.getString("data_count"));
				result.add(entry);
				return 0;
			}
		});
		return result;
	}

	/**
	 * @see com.x.apa.webportal.dao.DashboardDao#queryRawDomainCountSummary()
	 */
	@Override
	public List<Map<String, String>> queryRawDomainCountSummary() {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) as data_count, registration_date ");
		sql.append(" from v_ld_raw_domain ");
		sql.append(" group by registration_date ");
		sql.append(" order by registration_date DESC limit 0,4 ");

		final List<Map<String, String>> result = Lists.newArrayList();
		namedParamJdbcTemplate.query(sql.toString(), new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {

				Map<String, String> entry = Maps.newLinkedHashMap();
				entry.put("registrationDate", rs.getString("registration_date"));
				entry.put("dataCount", rs.getString("data_count"));
				result.add(entry);
				return 0;
			}
		});
		return result;
	}

	/**
	 * @see com.x.apa.webportal.dao.DashboardDao#queryCrawlerLatestRunStatus(java.util.Date)
	 */
	@Override
	public Map<String, String> queryCrawlerLatestRunStatus(Date date) {

		String startDay8 = DateUtils.toString_YYYY_MM_DD(date) + " 08:00:00";
		String endDay8 = DateUtils.toString_YYYY_MM_DD(DateUtils.afterDayDate(date, 1)) + " 08:00:00";

		Date startDay12Date = DateUtils.parseYYYY_MM_DD_HH_MM_SSDate(startDay8);
		Date endDay12Date = DateUtils.parseYYYY_MM_DD_HH_MM_SSDate(endDay8);

		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) as data_count, sum(page_outlink_num) as page_outlink_num, ");
		sql.append(
				" sum(page_outsitelink_num) as page_outsitelink_num, sum(page_trustlink_num) as page_trustlink_num, ");
		sql.append(" sum(page_image_num) as page_image_num, sum(page_should_visit_num) as page_should_visit_num, ");
		sql.append(" sum(page_visit_num) as page_visit_num, sum(page_visit_size) as page_visit_size, ");
		sql.append(" sum(nohtml_visit_num) as nohtml_visit_num, sum(nohtml_visit_size) as nohtml_visit_size, ");
		sql.append(" min(create_time) as min_create_time, max(create_time) as max_create_time ");
		sql.append(" from su_clue_url ");
		sql.append(" where create_time > :startDay and create_time < :endDay ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("startDay", startDay12Date);
		params.put("endDay", endDay12Date);

		Map<String, String> result = Maps.newLinkedHashMap();
		namedParamJdbcTemplate.query(sql.toString(), params, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {

				result.put("date", DateUtils.toString_YYYY_MM_DD(startDay12Date));
				result.put("dateCount", rs.getString("data_count"));
				result.put("pageOutlinkNum", rs.getString("page_outlink_num"));
				result.put("pageOutsitelinkNum", rs.getString("page_outsitelink_num"));
				result.put("pageTrustlinkNum", rs.getString("page_trustlink_num"));
				result.put("pageImageNum", rs.getString("page_image_num"));
				result.put("pageShouldVisitNum", rs.getString("page_should_visit_num"));
				result.put("pageVisitNum", rs.getString("page_visit_num"));
				result.put("pageVisitSize", rs.getString("page_visit_size"));
				result.put("nohtmlVisitNum", rs.getString("nohtml_visit_num"));
				result.put("nohtmlVisitSize", rs.getString("nohtml_visit_size"));
				result.put("minCreateTime", rs.getString("min_create_time"));
				result.put("maxCreateTime", rs.getString("max_create_time"));
				return 0;
			}
		});
		return result;
	}

	/**
	 * @see com.x.apa.webportal.dao.DashboardDao#queryRecentFoundSuspectUrl(int)
	 */
	@Override
	public List<Map<String, String>> queryRecentFoundSuspectUrl(int fetchNum) {
		StringBuilder sql = new StringBuilder();
		sql.append("select url,verify,create_time from su_suspect_url order by create_time DESC limit 0," + fetchNum);

		final List<Map<String, String>> result = Lists.newArrayList();
		namedParamJdbcTemplate.query(sql.toString(), new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {

				Map<String, String> entry = Maps.newLinkedHashMap();
				entry.put("url", rs.getString("url"));
				entry.put("verify", rs.getString("verify"));
				entry.put("createTime", rs.getString("create_time"));
				result.add(entry);
				return 0;
			}
		});
		return result;
	}

	/**
	 * @see com.x.apa.webportal.dao.DashboardDao#queryRecentFoundSuspectDomain(int)
	 */
	@Override
	public List<Map<String, String>> queryRecentFoundSuspectDomain(int fetchNum) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"select domain_name,create_time from sd_suspect_domain order by create_time DESC limit 0," + fetchNum);

		final List<Map<String, String>> result = Lists.newArrayList();
		namedParamJdbcTemplate.query(sql.toString(), new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {

				Map<String, String> entry = Maps.newLinkedHashMap();
				entry.put("domainName", rs.getString("domain_name"));
				entry.put("createTime", rs.getString("create_time"));
				result.add(entry);
				return 0;
			}
		});
		return result;
	}

	/**
	 * @see com.x.apa.webportal.dao.DashboardDao#queryPhishtankRunStatus()
	 */
	@Override
	public List<Map<String, String>> queryPhishtankRunStatus() {
		StringBuilder sql = new StringBuilder();
		sql.append("select 'latest_sync_time' as data_name, MAX(create_time) as data_value from phishtank_valid_url ");
		sql.append(" UNION ");
		sql.append(
				"select 'latest_submission_time' as data_name, MAX(submission_time) as data_value from phishtank_valid_url ");
		sql.append(" UNION ");
		sql.append("select 'phishtank_url_count' as data_name, COUNT(*) as data_value from phishtank_valid_url ");

		final List<Map<String, String>> result = Lists.newArrayList();
		namedParamJdbcTemplate.query(sql.toString(), new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {

				Map<String, String> entry = Maps.newLinkedHashMap();
				entry.put("dataName", rs.getString("data_name"));
				entry.put("dataValue", rs.getString("data_value"));
				result.add(entry);
				return 0;
			}
		});
		return result;
	}
}
