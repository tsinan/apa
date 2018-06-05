package com.x.apa.suspecturl.dao;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Maps;
import com.x.apa.common.Constant;
import com.x.apa.common.util.UUIDGenerator;
import com.x.apa.suspecturl.data.ClueUrl;

/**
 * @author liumeng
 */
@Component
public class ClueUrlDaoImpl implements ClueUrlDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.suspecturl.dao.ClueUrlDao#saveClueUrl(com.x.apa.suspecturl.data.ClueUrl)
	 */
	@Override
	public ClueUrl saveClueUrl(ClueUrl clueUrl) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		sql.append(" su_clue_url ");
		sql.append(" (id, url, progress, raw_domain_id, suspect_domain_id, ");
		sql.append("  create_time, update_time, deleted) ");
		sql.append(" values (:id, :url, :progress, :rawDomainId, :suspectDomainId, ");
		sql.append("  :createTime, :updateTime, :deleted) ");

		clueUrl.setId(UUIDGenerator.generateUniqueID(""));
		clueUrl.setCreateTime(new Date());
		clueUrl.setUpdateTime(clueUrl.getCreateTime());
		clueUrl.setDeleted(DELETED_NO);

		SqlParameterSource ps = new BeanPropertySqlParameterSource(clueUrl);
		namedParamJdbcTemplate.update(sql.toString(), ps);
		return clueUrl;
	}

	/**
	 * @see com.x.apa.suspecturl.dao.ClueUrlDao#updateClueUrl(com.x.apa.suspecturl.data.ClueUrl)
	 */
	@Override
	public int updateClueUrl(ClueUrl clueUrl) {
		StringBuilder sql = new StringBuilder();
		sql.append("update su_clue_url ");
		sql.append(" set progress = :progress, page_outlink_num = :pageOutlinkNum, ");
		sql.append(" page_outsitelink_num = :pageOutsitelinkNum, page_trustlink_num = :pageTrustlinkNum,  ");
		sql.append(" page_image_num = :pageImageNum, page_should_visit_num = :pageShouldVisitNum, ");
		sql.append(" page_visit_num = :pageVisitNum, page_visit_size = :pageVisitSize, ");
		sql.append(" nohtml_visit_num = :nohtmlVisitNum, nohtml_visit_size = :nohtmlVisitSize, ");
		sql.append(" redirect_num = :redirectNum, update_time = :updateTime ");
		sql.append(" where id = :id");

		clueUrl.setUpdateTime(new Date());

		SqlParameterSource ps = new BeanPropertySqlParameterSource(clueUrl);
		return namedParamJdbcTemplate.update(sql.toString(), ps);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.ClueUrlDao#deleteClueUrlBeforeDate(java.util.Date)
	 */
	@Override
	public int deleteClueUrlBeforeDate(Date date) {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from su_clue_url ");
		sql.append(" where create_time < :date ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("date", date);

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

}
