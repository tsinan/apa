package com.x.apa.inspecturl.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import com.x.apa.inspecturl.data.InspectEvent;

/**
 * @author liumeng
 */
@Component
public class inspectEventDaoImpl implements InspectEventDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.inspecturl.dao.InspectEventDao#queryInspectEvents(com.x.apa.common.pageable.PageRequest,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public Page<InspectEvent> queryInspectEvents(PageRequest page, String progress, String urlLike) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, category, url_id, url, keyword_from, keyword_to,  ");
		sql.append(" keyword_score_from, keyword_score_to, message_from, message_to, ");
		sql.append("  status_from, status_to, time_from, time_to, progress, ");
		sql.append("  create_time, update_time ");
		sql.append(" from ");
		sql.append(" pu_inspect_event ");

		StringBuilder where = new StringBuilder();
		where.append(" where deleted = :deleted ");

		if (StringUtils.isNotBlank(progress)) {
			where.append(" and progress = :progress ");
		}
		if (StringUtils.isNotBlank(urlLike)) {
			where.append(" and url like :urlLike ");
		}
		sql.append(where);

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("progress", progress);
		params.put("urlLike", "%" + urlLike + "%");

		PageUtils.appendPage(sql, page);

		List<InspectEvent> inspectEventList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(InspectEvent.class));

		// total
		StringBuilder totalSql = new StringBuilder();
		totalSql.append("select count(id) from ");
		totalSql.append(" pu_inspect_event ");
		totalSql.append(where);

		Integer total = namedParamJdbcTemplate.queryForObject(totalSql.toString(), params, Integer.class);
		return new PageImpl<InspectEvent>(inspectEventList, page, total);
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectEventDao#saveInspectEvent(com.x.apa.inspecturl.data.InspectEvent)
	 */
	@Override
	public InspectEvent saveInspectEvent(InspectEvent inspectEvent) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		sql.append(" pu_inspect_event ");
		sql.append(" (id, category, url_id, url, keyword_from, keyword_to, ");
		sql.append(" keyword_score_from, keyword_score_to, message_from, message_to, ");
		sql.append(" status_from, status_to, time_from, time_to,  ");
		sql.append("  create_time, update_time, deleted) ");
		sql.append(" values (:id, :category, :urlId, :url, :keywordFrom, :keywordTo, ");
		sql.append(" :keywordScoreFrom, :keywordScoreTo, :messageFrom, :messageTo, ");
		sql.append(" :statusFrom, :statusTo, :timeFrom, :timeTo, ");
		sql.append("  :createTime, :updateTime, :deleted) ");

		inspectEvent.setId(UUIDGenerator.generateUniqueID(""));
		inspectEvent.setCreateTime(new Date());
		inspectEvent.setUpdateTime(inspectEvent.getCreateTime());
		inspectEvent.setDeleted(DELETED_NO);

		SqlParameterSource ps = new BeanPropertySqlParameterSource(inspectEvent);
		namedParamJdbcTemplate.update(sql.toString(), ps);
		return inspectEvent;
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectEventDao#changeInspectEventProgress(java.lang.String,
	 *      int)
	 */
	@Override
	public int changeInspectEventProgress(String id, int progress) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" pu_inspect_event ");
		sql.append(" set progress = :progress, update_time = :updateTime ");
		sql.append(" where id = :id");

		Map<String, Object> params = Maps.newHashMap();
		params.put("id", id);
		params.put("progress", progress);
		params.put("updateTime", new Date());

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectEventDao#deleteInspectEvent(java.lang.String)
	 */
	@Override
	public int deleteInspectEvent(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" pu_inspect_event ");
		sql.append(" set deleted = :deleted ,");
		sql.append(" delete_time = :deleteTime");
		sql.append(" where id = :id");

		Map<String, Object> params = Maps.newHashMap();
		params.put("id", id);
		params.put("deleteTime", new Date());
		params.put("deleted", DELETED_YES);

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectEventDao#deleteInspectEventBeforeDate(java.util.Date)
	 */
	@Override
	public int deleteInspectEventBeforeDate(Date date) {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from pu_inspect_event ");
		sql.append(" where create_time < :date  ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("date", date);

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

}
