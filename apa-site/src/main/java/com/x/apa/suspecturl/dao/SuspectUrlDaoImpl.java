package com.x.apa.suspecturl.dao;

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
import com.x.apa.suspecturl.data.SuspectUrl;

/**
 * @author liumeng
 */
@Component
public class SuspectUrlDaoImpl implements SuspectUrlDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.suspecturl.dao.SuspectUrlDao#querySuspectUrls(com.x.apa.common.pageable.PageRequest,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public Page<SuspectUrl> querySuspectUrls(PageRequest page, String tagId, String urlLike) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, url, verify, tag_id, tag_rule, tag_rule_score, ");
		sql.append(" raw_domain_id, suspect_domain_id, clue_url_id, ");
		sql.append(" create_time, update_time ");
		sql.append(" from ");
		sql.append(" su_suspect_url ");

		StringBuilder where = new StringBuilder();
		where.append(" where deleted = :deleted ");
		if (StringUtils.isNotBlank(tagId)) {
			where.append(" and tag_id = :tagId ");
		}
		if (StringUtils.isNotBlank(urlLike)) {
			where.append(" and url like :urlLike ");
		}
		sql.append(where);

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("tagId", tagId);
		params.put("urlLike", "%" + urlLike + "%");

		PageUtils.appendPage(sql, page);

		List<SuspectUrl> suspectUrlList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(SuspectUrl.class));

		// total
		StringBuilder totalSql = new StringBuilder();
		totalSql.append("select count(id) from ");
		totalSql.append(" su_suspect_url");
		totalSql.append(where);

		Integer total = namedParamJdbcTemplate.queryForObject(totalSql.toString(), params, Integer.class);
		return new PageImpl<SuspectUrl>(suspectUrlList, page, total);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.SuspectUrlDao#querySuspectUrl(java.lang.String)
	 */
	@Override
	public SuspectUrl querySuspectUrl(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, url, verify, tag_id, tag_rule, tag_rule_score, ");
		sql.append(" raw_domain_id, suspect_domain_id, clue_url_id, ");
		sql.append(" create_time, update_time ");
		sql.append(" from ");
		sql.append(" su_suspect_url ");

		sql.append(" where deleted = :deleted ");
		sql.append(" and id = :id ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("id", id);

		List<SuspectUrl> suspectUrlList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(SuspectUrl.class));
		return CollectionUtils.isEmpty(suspectUrlList) ? new SuspectUrl() : suspectUrlList.get(0);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.SuspectUrlDao#saveSuspectUrl(com.x.apa.suspecturl.data.SuspectUrl)
	 */
	@Override
	public SuspectUrl saveSuspectUrl(SuspectUrl suspectUrl) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		sql.append(" su_suspect_url ");
		sql.append(" (id, url, verify, tag_id, tag_rule, tag_rule_score, raw_domain_id,  ");
		sql.append(" suspect_domain_id, clue_url_id, create_time, update_time, deleted) ");
		sql.append(" values (:id, :url, :verify, :tagId, :tagRule, :tagRuleScore, :rawDomainId, ");
		sql.append(" :suspectDomainId, :clueUrlId, :createTime, :updateTime, :deleted) ");

		suspectUrl.setId(UUIDGenerator.generateUniqueID(""));
		suspectUrl.setCreateTime(new Date());
		suspectUrl.setUpdateTime(suspectUrl.getCreateTime());
		suspectUrl.setDeleted(DELETED_NO);

		SqlParameterSource ps = new BeanPropertySqlParameterSource(suspectUrl);
		namedParamJdbcTemplate.update(sql.toString(), ps);
		return suspectUrl;
	}

	/**
	 * @see com.x.apa.suspecturl.dao.SuspectUrlDao#updateSuspectUrlVerify(java.lang.String,
	 *      int)
	 */
	@Override
	public int updateSuspectUrlVerify(String id, int verify) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" su_suspect_url ");
		sql.append(" set verify = :verify, update_time = :updateTime ");
		sql.append(" where id = :id");

		Map<String, Object> params = Maps.newHashMap();
		params.put("id", id);
		params.put("verify", verify);
		params.put("updateTime", new Date());

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.SuspectUrlDao#deleteSupsectUrl(java.lang.String)
	 */
	@Override
	public int deleteSupsectUrl(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" su_suspect_url ");
		sql.append(" set deleted = :deleted, delete_time = :deleteTime ");
		sql.append(" where id = :id");

		Map<String, Object> params = Maps.newHashMap();
		params.put("id", id);
		params.put("deleted", DELETED_YES);
		params.put("deleteTime", new Date());

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.SuspectUrlDao#deleteSuspectUrlBeforeDate(java.util.Date)
	 */
	@Override
	public int deleteSuspectUrlBeforeDate(Date date) {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from su_suspect_url ");
		sql.append(" where create_time < :date and verify <> :verify ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("date", date);
		params.put("verify", SUSPECT_URL_VERIFY_PHISHING);

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

}
