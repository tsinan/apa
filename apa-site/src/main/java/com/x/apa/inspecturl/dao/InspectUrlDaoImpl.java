package com.x.apa.inspecturl.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
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
import com.x.apa.inspecturl.data.InspectUrl;
import com.x.apa.inspecturl.vo.InspectUrlQuery;

/**
 * @author liumeng
 */
@Component
public class InspectUrlDaoImpl implements InspectUrlDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.inspecturl.dao.InspectUrlDao#queryInspectUrlsByNextTime(java.util.Date)
	 */
	@Override
	public List<InspectUrl> queryInspectUrlsByNextTime(Date datetime) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, category, url, ip_address, inspect_level, inspect_keyword, inspect_keyword_score, ");
		sql.append(" inspect_status, inspect_message, inspect_time, inspect_next_time, ");
		sql.append(" inspect_times, active_duration, brand_id, incident_no, ");
		sql.append(" whois_reg_name, whois_reg_email, whois_webhost, ");
		sql.append(" create_time, update_time ");
		sql.append(" from ");
		sql.append(" pu_inspect_url ");
		sql.append(" where deleted = :deleted ");
		sql.append(" and inspect_level = :inspectLevel ");
		sql.append(" and inspect_next_time <= :datetime ");
		sql.append(" order by inspect_next_time ASC ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("inspectLevel", INSPECT_URL_INSPECT_LEVEL_ROUTINE);
		params.put("datetime", datetime);

		return namedParamJdbcTemplate.query(sql.toString(), params, new BeanPropertyRowMapper<>(InspectUrl.class));
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectUrlDao#queryInspectUrls(boolean)
	 */
	@Override
	public List<InspectUrl> queryInspectUrls(boolean onlyLocked) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, category, url, ip_address, inspect_level, inspect_keyword, inspect_keyword_score, ");
		sql.append(" inspect_status, inspect_message, inspect_time, inspect_next_time, ");
		sql.append(" inspect_times, active_duration, brand_id, incident_no, ");
		sql.append(" whois_reg_name, whois_reg_email, whois_webhost, ");
		sql.append(" create_time, update_time ");
		sql.append(" from ");
		sql.append(" pu_inspect_url ");
		sql.append(" where deleted = :deleted ");
		if (onlyLocked) {
			sql.append(" and inspect_next_time is null ");
		}

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);

		return namedParamJdbcTemplate.query(sql.toString(), params, new BeanPropertyRowMapper<>(InspectUrl.class));
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectUrlDao#queryInspectUrls(com.x.apa.common.pageable.PageRequest,
	 *      com.x.apa.inspecturl.vo.InspectUrlQuery)
	 */
	@Override
	public Page<InspectUrl> queryInspectUrls(PageRequest page, InspectUrlQuery inspectUrlQuery) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, category, url, ip_address, inspect_level, inspect_keyword, inspect_keyword_score, ");
		sql.append(" inspect_status, inspect_message, inspect_time, inspect_next_time, ");
		sql.append(" inspect_times, active_duration, brand_id, incident_no, ");
		sql.append(" whois_reg_name, whois_reg_email, whois_webhost, ");
		sql.append(" create_time, update_time ");
		sql.append(" from ");
		sql.append(" pu_inspect_url ");

		StringBuilder where = new StringBuilder();
		where.append(" where deleted = :deleted ");
		if (StringUtils.isNotBlank(inspectUrlQuery.getStartTime())) {
			where.append(" and create_time >= :startTime ");
		}
		if (StringUtils.isNotBlank(inspectUrlQuery.getEndTime())) {
			where.append(" and create_time < :endTime ");
		}
		if (StringUtils.isNotBlank(inspectUrlQuery.getInspectLevel())) {
			where.append(" and inspect_level = :inspectLevel ");
		}
		if (StringUtils.isNotBlank(inspectUrlQuery.getInspectStatus())) {
			where.append(" and inspect_status = :inspectStatus ");
		}
		if (StringUtils.isNotBlank(inspectUrlQuery.getCategory())) {
			where.append(" and category = :category ");
		}
		if (StringUtils.isNotBlank(inspectUrlQuery.getBrandId())) {
			where.append(" and brand_id = :brandId ");
		}
		if (StringUtils.isNotBlank(inspectUrlQuery.getUrlLike())) {
			where.append(" and url like :urlLike ");
		}
		sql.append(where);

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("startTime", inspectUrlQuery.getStartTime());
		params.put("endTime", inspectUrlQuery.getEndTime());
		params.put("inspectLevel", inspectUrlQuery.getInspectLevel());
		params.put("inspectStatus", inspectUrlQuery.getInspectStatus());
		params.put("category", inspectUrlQuery.getCategory());
		params.put("brandId", inspectUrlQuery.getBrandId());
		params.put("urlLike", "%" + inspectUrlQuery.getUrlLike() + "%");

		PageUtils.appendPage(sql, page);

		List<InspectUrl> inspectUrlList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(InspectUrl.class));

		// total
		StringBuilder totalSql = new StringBuilder();
		totalSql.append("select count(id) from ");
		totalSql.append(" pu_inspect_url ");
		totalSql.append(where);

		Integer total = namedParamJdbcTemplate.queryForObject(totalSql.toString(), params, Integer.class);
		return new PageImpl<InspectUrl>(inspectUrlList, page, total);
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectUrlDao#queryInspectUrls(com.x.apa.inspecturl.vo.InspectUrlQuery)
	 */
	@Override
	public List<InspectUrl> queryInspectUrls(InspectUrlQuery inspectUrlQuery) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, category, url, ip_address, inspect_level, inspect_keyword, inspect_keyword_score, ");
		sql.append(" inspect_status, inspect_message, inspect_time, inspect_next_time, ");
		sql.append(" inspect_times, active_duration, brand_id, incident_no, ");
		sql.append(" whois_reg_name, whois_reg_email, whois_webhost, ");
		sql.append(" create_time, update_time ");
		sql.append(" from ");
		sql.append(" pu_inspect_url ");
		sql.append(" where deleted = :deleted ");
		if (StringUtils.isNotBlank(inspectUrlQuery.getStartTime())) {
			sql.append(" and create_time >= :startTime ");
		}
		if (StringUtils.isNotBlank(inspectUrlQuery.getEndTime())) {
			sql.append(" and create_time < :endTime ");
		}
		if (StringUtils.isNotBlank(inspectUrlQuery.getInspectLevel())) {
			sql.append(" and inspect_level = :inspectLevel ");
		}
		if (StringUtils.isNotBlank(inspectUrlQuery.getInspectStatus())) {
			sql.append(" and inspect_status = :inspectStatus ");
		}
		if (StringUtils.isNotBlank(inspectUrlQuery.getCategory())) {
			sql.append(" and category = :category ");
		}
		if (StringUtils.isNotBlank(inspectUrlQuery.getBrandId())) {
			sql.append(" and brand_id = :brandId ");
		}
		if (StringUtils.isNotBlank(inspectUrlQuery.getUrlLike())) {
			sql.append(" and url like :urlLike ");
		}
		sql.append(" order by create_time ASC ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("startTime", inspectUrlQuery.getStartTime());
		params.put("endTime", inspectUrlQuery.getEndTime());
		params.put("inspectLevel", inspectUrlQuery.getInspectLevel());
		params.put("inspectStatus", inspectUrlQuery.getInspectStatus());
		params.put("category", inspectUrlQuery.getCategory());
		params.put("brandId", inspectUrlQuery.getBrandId());
		params.put("urlLike", "%" + inspectUrlQuery.getUrlLike() + "%");

		return namedParamJdbcTemplate.query(sql.toString(), params, new BeanPropertyRowMapper<>(InspectUrl.class));
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectUrlDao#queryInspectUrl(java.lang.String)
	 */
	@Override
	public InspectUrl queryInspectUrl(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, category, url, ip_address, inspect_level, inspect_keyword, inspect_keyword_score, ");
		sql.append(" inspect_status, inspect_message, inspect_time, inspect_next_time, ");
		sql.append(" inspect_times, active_duration, brand_id, incident_no, ");
		sql.append(" whois_reg_name, whois_reg_email, whois_webhost, ");
		sql.append(" create_time, update_time ");
		sql.append(" from ");
		sql.append(" pu_inspect_url ");
		sql.append(" where deleted = :deleted ");
		sql.append(" and id = :id ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("id", id);

		List<InspectUrl> inspectUrlList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(InspectUrl.class));
		return CollectionUtils.isEmpty(inspectUrlList) ? new InspectUrl() : inspectUrlList.get(0);
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectUrlDao#queryInspectUrlByUrl(java.lang.String)
	 */
	@Override
	public InspectUrl queryInspectUrlByUrl(String url) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, category, url, ip_address, inspect_level, inspect_keyword, inspect_keyword_score, ");
		sql.append(" inspect_status, inspect_message, inspect_time, inspect_next_time, ");
		sql.append(" inspect_times, active_duration, brand_id, incident_no, ");
		sql.append(" whois_reg_name, whois_reg_email, whois_webhost, ");
		sql.append(" create_time, update_time ");
		sql.append(" from ");
		sql.append(" pu_inspect_url ");
		sql.append(" where deleted = :deleted ");
		sql.append(" and url = :url ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("url", url);

		List<InspectUrl> inspectUrlList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(InspectUrl.class));
		return CollectionUtils.isEmpty(inspectUrlList) ? new InspectUrl() : inspectUrlList.get(0);
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectUrlDao#saveInspectUrl(com.x.apa.inspecturl.data.InspectUrl)
	 */
	@Override
	public InspectUrl saveInspectUrl(InspectUrl inspectUrl) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		sql.append(" pu_inspect_url ");
		sql.append(" (id, category, url, ip_address, inspect_level, inspect_keyword, inspect_keyword_score, ");
		sql.append(" inspect_status, inspect_message, inspect_time, inspect_next_time, ");
		sql.append(" inspect_times, active_duration, brand_id, incident_no, ");
		sql.append(" whois_reg_name, whois_reg_email, whois_webhost, ");
		sql.append(" create_time, update_time, deleted) ");
		sql.append(" values (:id, :category, :url, :ipAddress, :inspectLevel, :inspectKeyword, :inspectKeywordScore, ");
		sql.append(" :inspectStatus, :inspectMessage, :inspectTime, :inspectNextTime,");
		sql.append(" :inspectTimes, :activeDuration, :brandId, :incidentNo, ");
		sql.append(" :whoisRegName, :whoisRegEmail, :whoisWebhost, ");
		sql.append(" :createTime, :updateTime, :deleted) ");

		inspectUrl.setId(UUIDGenerator.generateUniqueID(""));
		inspectUrl.setCreateTime(new Date());
		inspectUrl.setUpdateTime(inspectUrl.getCreateTime());
		inspectUrl.setDeleted(DELETED_NO);

		SqlParameterSource ps = new BeanPropertySqlParameterSource(inspectUrl);
		namedParamJdbcTemplate.update(sql.toString(), ps);
		return inspectUrl;
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectUrlDao#updateInspectUrl(com.x.apa.inspecturl.data.InspectUrl)
	 */
	@Override
	public int updateInspectUrl(InspectUrl inspectUrl) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" pu_inspect_url ");
		sql.append(" set url = :url, inspect_level = :inspectLevel, inspect_status = :inspectStatus, ");
		sql.append(" inspect_keyword = :inspectKeyword, inspect_next_time = :inspectNextTime, ");
		sql.append(" brand_id = :brandId, incident_no = :incidentNo, ");
		sql.append(" whois_reg_name = :whoisRegName, whois_reg_email = :whoisRegEmail, ");
		sql.append(" whois_webhost = :whoisWebhost, update_time = :updateTime ");
		sql.append(" where id = :id");

		inspectUrl.setUpdateTime(new Date());
		SqlParameterSource ps = new BeanPropertySqlParameterSource(inspectUrl);
		return namedParamJdbcTemplate.update(sql.toString(), ps);
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectUrlDao#updateInspectUrlStatus(com.x.apa.inspecturl.data.InspectUrl)
	 */
	@Override
	public int updateInspectUrlStatus(InspectUrl inspectUrl) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" pu_inspect_url ");
		sql.append(" set ip_address = :ipAddress, ");
		sql.append(" inspect_keyword = :inspectKeyword, inspect_keyword_score = :inspectKeywordScore, ");
		sql.append(" inspect_status = :inspectStatus, inspect_message = :inspectMessage, ");
		sql.append(" inspect_time = :inspectTime, inspect_next_time = :inspectNextTime, ");
		sql.append(" inspect_times = :inspectTimes, active_duration = :activeDuration ");
		sql.append(" where id = :id");

		SqlParameterSource ps = new BeanPropertySqlParameterSource(inspectUrl);
		return namedParamJdbcTemplate.update(sql.toString(), ps);
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectUrlDao#updateInpsectUrlWhois(com.x.apa.inspecturl.data.InspectUrl)
	 */
	@Override
	public int updateInpsectUrlWhois(InspectUrl inspectUrl) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" pu_inspect_url ");
		sql.append(" set whois_reg_name = :whoisRegName, whois_reg_email = :whoisRegEmail ");
		sql.append(" where id = :id");

		SqlParameterSource ps = new BeanPropertySqlParameterSource(inspectUrl);
		return namedParamJdbcTemplate.update(sql.toString(), ps);
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectUrlDao#changeInspectUrlLock(java.lang.String,
	 *      boolean)
	 */
	@Override
	public int changeInspectUrlLock(String id, boolean isLock) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" pu_inspect_url ");
		if (isLock) {
			sql.append(" set inspect_next_time = null ");
		} else {
			sql.append(" set inspect_next_time = :currentTime ");
		}
		sql.append(" where id = :id");

		Map<String, Object> params = Maps.newHashMap();
		params.put("id", id);
		params.put("currentTime", new Date());

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectUrlDao#changeInspectUrlLevel(java.lang.String,
	 *      int, int)
	 */
	@Override
	public int changeInspectUrlLevel(String inspectUrlId, int inspectLevel, int inspectStatus) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" pu_inspect_url ");
		sql.append(" set inspect_level = :inspectLevel, inspect_status = :inspectStatus ");
		sql.append(" where id = :inspectUrlId");

		Map<String, Object> params = Maps.newHashMap();
		params.put("inspectLevel", inspectLevel);
		params.put("inspectStatus", inspectStatus);
		params.put("inspectUrlId", inspectUrlId);
		params.put("currentTime", new Date());

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

	/**
	 * @see com.x.apa.inspecturl.dao.InspectUrlDao#deleteInspectUrl(java.lang.String)
	 */
	@Override
	public int deleteInspectUrl(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" pu_inspect_url ");
		sql.append(" set deleted = :deleted ,");
		sql.append(" delete_time = :deleteTime");
		sql.append(" where id = :id");

		Map<String, Object> params = Maps.newHashMap();
		params.put("id", id);
		params.put("deleteTime", new Date());
		params.put("deleted", DELETED_YES);

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

}
