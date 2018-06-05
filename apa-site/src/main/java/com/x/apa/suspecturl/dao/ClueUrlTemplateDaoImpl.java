package com.x.apa.suspecturl.dao;

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
import com.x.apa.suspecturl.data.ClueUrlTemplate;

/**
 * @author liumeng
 */
@Component
public class ClueUrlTemplateDaoImpl implements ClueUrlTemplateDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.suspecturl.dao.ClueUrlTemplateDao#queryClueUrlTemplate(java.lang.String)
	 */
	@Override
	public ClueUrlTemplate queryClueUrlTemplate(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, content, create_time, update_time ");
		sql.append(" from ");
		sql.append(" config_clue_url_template ");
		sql.append(" where deleted = :deleted ");
		sql.append(" and id = :id ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("id", id);

		List<ClueUrlTemplate> templateList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(ClueUrlTemplate.class));
		return CollectionUtils.isEmpty(templateList) ? new ClueUrlTemplate() : templateList.get(0);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.ClueUrlTemplateDao#updateClueUrlTemplate(com.x.apa.suspecturl.data.ClueUrlTemplate)
	 */
	@Override
	public int updateClueUrlTemplate(ClueUrlTemplate clueUrlTemplate) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_clue_url_template ");
		sql.append(" set content = :content, update_time = :updateTime ");
		sql.append(" where id = :id");

		clueUrlTemplate.setUpdateTime(new Date());
		SqlParameterSource ps = new BeanPropertySqlParameterSource(clueUrlTemplate);
		return namedParamJdbcTemplate.update(sql.toString(), ps);
	}

}
