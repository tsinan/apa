package com.x.apa.inspecturl.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.x.apa.common.Constant;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageImpl;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.common.util.PageUtils;
import com.x.apa.common.util.UUIDGenerator;
import com.x.apa.inspecturl.data.InspectTrace;

/**
 * @author liumeng
 */
@Component
public class InspectTraceDaoImpl implements InspectTraceDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.inspecturl.dao.InspectTraceDao#queryInspectTraces(com.x.apa.common.pageable.PageRequest,
	 *      java.lang.String)
	 */
	@Override
	public Page<InspectTrace> queryInspectTraces(PageRequest page, String inspectUrlId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, url_id, url, ip_address, inspect_level, inspect_keyword, inspect_keyword_score, ");
		sql.append(" inspect_message, inspect_status, inspect_time, inspect_next_time, inspect_times, ");
		sql.append(" active_duration, ");
		sql.append(" create_time, update_time ");
		sql.append(" from ");
		sql.append(" pu_inspect_trace ");

		StringBuilder where = new StringBuilder();
		where.append(" where deleted = :deleted ");
		where.append(" and url_id = :inspectUrlId ");
		sql.append(where);

		PageUtils.appendPage(sql, page);

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("inspectUrlId", inspectUrlId);

		List<InspectTrace> inspectTraceList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(InspectTrace.class));

		// total
		StringBuilder totalSql = new StringBuilder();
		totalSql.append("select count(id) from ");
		totalSql.append(" pu_inspect_trace ");
		totalSql.append(where);

		Integer total = namedParamJdbcTemplate.queryForObject(totalSql.toString(), params, Integer.class);
		return new PageImpl<InspectTrace>(inspectTraceList, page, total);
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectTraceDao#queryInspectTrace(java.lang.String)
	 */
	@Override
	public InspectTrace queryInspectTrace(String inspectTraceId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, http_status_code, http_headers, text_tokens, text_other,");
		sql.append(" create_time, update_time ");
		sql.append(" from ");
		sql.append(" pu_inspect_trace ");

		sql.append(" where deleted = :deleted ");
		sql.append(" and id = :inspectTraceId ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("inspectTraceId", inspectTraceId);

		List<InspectTrace> inspectTraceList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(InspectTrace.class));
		return CollectionUtils.isEmpty(inspectTraceList) ? new InspectTrace() : inspectTraceList.get(0);
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectTraceDao#saveInspectTrace(com.x.apa.inspecturl.data.InspectTrace)
	 */
	@Override
	public InspectTrace saveInspectTrace(InspectTrace inspectTrace) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		sql.append(" pu_inspect_trace ");
		sql.append(" (id, url_id, url, ip_address, inspect_level, inspect_keyword, inspect_keyword_score, ");
		sql.append(" inspect_message, inspect_status, inspect_time, inspect_next_time, inspect_times, ");
		sql.append(" active_duration, http_status_code, http_headers, html, text, text_tokens, text_other,");
		sql.append("  create_time, update_time, deleted) ");
		sql.append(" values (:id, :urlId, :url, :ipAddress, :inspectLevel, :inspectKeyword, :inspectKeywordScore, ");
		sql.append(" :inspectMessage, :inspectStatus, :inspectTime, :inspectNextTime, :inspectTimes, ");
		sql.append(" :activeDuration, :httpStatusCode, :httpHeaders, :html, :text, :textTokens, :textOther, ");
		sql.append("  :createTime, :updateTime, :deleted) ");

		inspectTrace.setId(UUIDGenerator.generateUniqueID(""));
		inspectTrace.setCreateTime(new Date());
		inspectTrace.setUpdateTime(inspectTrace.getCreateTime());
		inspectTrace.setDeleted(DELETED_NO);

		SqlParameterSource ps = new BeanPropertySqlParameterSource(inspectTrace);
		namedParamJdbcTemplate.update(sql.toString(), ps);
		return inspectTrace;
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectTraceDao#deleteInspectTraceBeforeDate(java.util.Date)
	 */
	@Override
	public int deleteInspectTraceBeforeDate(Date date) {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from pu_inspect_trace ");
		sql.append(" where create_time < :date  ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("date", date);

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

}
