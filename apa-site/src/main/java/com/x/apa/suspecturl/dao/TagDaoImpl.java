package com.x.apa.suspecturl.dao;

import java.util.Date;
import java.util.HashMap;
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
import com.x.apa.suspecturl.data.Tag;
import com.x.apa.suspecturl.data.TagRule;

/**
 * @author liumeng
 */
@Component
public class TagDaoImpl implements TagDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.suspecturl.dao.TagDao#queryTags()
	 */
	@Override
	public List<Tag> queryTags() {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, creator_id, status ");
		sql.append(" from ");
		sql.append(" config_tag ");
		sql.append(" where deleted = :deleted ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);

		List<Tag> tagList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(Tag.class));
		return tagList;
	}

	/**
	 * @see com.x.apa.suspecturl.dao.TagDao#queryTags(com.x.apa.common.pageable.PageRequest)
	 */
	@Override
	public Page<Tag> queryTags(PageRequest page) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, creator_id, status, create_time ");
		sql.append(" from ");
		sql.append(" config_tag ");

		StringBuilder where = new StringBuilder();
		where.append(" where deleted = :deleted ");
		sql.append(where);

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);

		PageUtils.appendPage(sql, page);

		List<Tag> tagList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(Tag.class));

		// total
		StringBuilder totalSql = new StringBuilder();
		totalSql.append("select count(id) from ");
		totalSql.append(" config_tag ");
		totalSql.append(where);

		Integer total = namedParamJdbcTemplate.queryForObject(totalSql.toString(), params, Integer.class);
		return new PageImpl<Tag>(tagList, page, total);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.TagDao#queryTag(java.lang.String)
	 */
	@Override
	public Tag queryTag(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, creator_id, status, create_time ");
		sql.append(" from ");
		sql.append(" config_tag ");
		sql.append(" where deleted = :deleted ");
		sql.append(" and id = :id ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("id", id);

		List<Tag> tagList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(Tag.class));
		return CollectionUtils.isEmpty(tagList) ? new Tag() : tagList.get(0);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.TagDao#saveTag(com.x.apa.suspecturl.data.Tag)
	 */
	@Override
	public Tag saveTag(Tag tag) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		sql.append(" config_tag ");
		sql.append(" (id, name, creator_id, status, create_time, update_time, deleted) ");
		sql.append(" values (:id, :name, :creatorId, :status, :createTime, :updateTime, :deleted) ");

		tag.setId(UUIDGenerator.generateUniqueID(""));
		tag.setCreateTime(new Date());
		tag.setUpdateTime(tag.getCreateTime());
		tag.setDeleted(DELETED_NO);

		SqlParameterSource ps = new BeanPropertySqlParameterSource(tag);
		namedParamJdbcTemplate.update(sql.toString(), ps);
		return tag;
	}

	/**
	 * @see com.x.apa.suspecturl.dao.TagDao#updateTag(com.x.apa.suspecturl.data.Tag)
	 */
	@Override
	public int updateTag(Tag tag) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_tag ");
		sql.append(" set name = :name, status = :status, update_time = :updateTime ");
		sql.append(" where id = :id");

		tag.setUpdateTime(new Date());
		SqlParameterSource ps = new BeanPropertySqlParameterSource(tag);
		return namedParamJdbcTemplate.update(sql.toString(), ps);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.TagDao#changeTagStatus(java.lang.String,int)
	 */
	@Override
	public int changeTagStatus(String id, int status) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_tag ");
		sql.append(" set status = :status, update_time = :updateTime ");
		sql.append(" where id = :id");

		Map<String, Object> params = Maps.newHashMap();
		params.put("id", id);
		params.put("status", status);
		params.put("updateTime", new Date());

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.TagDao#deleteTag(java.lang.String)
	 */
	@Override
	public int deleteTag(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_tag ");
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
	 * @see com.x.apa.suspecturl.dao.TagDao#queryTagRules()
	 */
	@Override
	public List<TagRule> queryTagRules() {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, tag_id, rule, description, creator_id ");
		sql.append(" from ");
		sql.append(" config_tag_rule ");
		sql.append(" where deleted = :deleted ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("status", STATUS_OPEN);

		List<TagRule> tagRuleList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(TagRule.class));
		return tagRuleList;
	}

	/**
	 * @see com.x.apa.suspecturl.dao.TagDao#queryTagRules(java.lang.String)
	 */
	@Override
	public List<TagRule> queryTagRules(String tagId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, tag_id, rule, description, creator_id ");
		sql.append(" from ");
		sql.append(" config_tag_rule ");
		sql.append(" where deleted = :deleted ");
		sql.append(" and tag_id = :tagId ");
		sql.append(" order by create_time ASC ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("tagId", tagId);

		List<TagRule> tagRuleList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(TagRule.class));
		return tagRuleList;
	}

	/**
	 * @see com.x.apa.suspecturl.dao.TagDao#saveTagRules(java.util.List)
	 */
	@Override
	public int[] saveTagRules(List<TagRule> tagRules) {

		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		sql.append(" config_tag_rule ");
		sql.append(" (id, tag_id, rule, description, creator_id, create_time, update_time, deleted) ");
		sql.append(" values (:id, :tagId, :rule, :description, :creatorId, :createTime, :updateTime, :deleted) ");

		@SuppressWarnings("unchecked")
		Map<String, Object>[] paramsArray = new HashMap[tagRules.size()];

		Date nowDate = new Date();
		int i = 0;
		for (TagRule rule : tagRules) {

			Map<String, Object> param = Maps.newHashMap();
			param.put("id", UUIDGenerator.generateUniqueID(""));
			param.put("createTime", nowDate);
			param.put("updateTime", nowDate);
			param.put("deleted", DELETED_NO);
			param.put("tagId", rule.getTagId());
			param.put("rule", rule.getRule());
			param.put("description", rule.getDescription());
			param.put("creatorId", rule.getCreatorId());
			paramsArray[i] = param;
			i++;
		}

		namedParamJdbcTemplate.batchUpdate(sql.toString(), paramsArray);
		return null;
	}

	/**
	 * @see com.x.apa.suspecturl.dao.TagDao#deleteTagRules(java.lang.String)
	 */
	@Override
	public int deleteTagRules(String tagId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_tag_rule ");
		sql.append(" set deleted = :deleted ,");
		sql.append(" delete_time = :deleteTime");
		sql.append(" where tag_id = :tagId");

		Map<String, Object> params = Maps.newHashMap();
		params.put("tagId", tagId);
		params.put("deleteTime", new Date());
		params.put("deleted", DELETED_YES);

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.TagDao#deleteTagRules(java.util.List)
	 */
	@Override
	public int deleteTagRules(List<String> tagRuleIds) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_tag_rule ");
		sql.append(" set deleted = :deleted ,");
		sql.append(" delete_time = :deleteTime");
		sql.append(" where id in ( :tagRuleIds ) ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("tagRuleIds", tagRuleIds);
		params.put("deleteTime", new Date());
		params.put("deleted", DELETED_YES);

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

}
