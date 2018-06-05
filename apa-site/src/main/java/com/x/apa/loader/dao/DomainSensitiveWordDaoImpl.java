package com.x.apa.loader.dao;

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
import com.x.apa.loader.data.DomainSensitiveWord;

/**
 * @author liumeng
 */
@Component
public class DomainSensitiveWordDaoImpl implements DomainSensitiveWordDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.loader.dao.DomainSensitiveWordDao#queryDomainSensitiveWords()
	 */
	@Override
	public List<DomainSensitiveWord> queryDomainSensitiveWords() {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, description, word, status ");
		sql.append(" from ");
		sql.append(" config_domain_sensitive_word ");
		sql.append(" where deleted = :deleted ");
		sql.append(" and status = :status ");
		sql.append(" order by create_time asc ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("status", STATUS_OPEN);

		List<DomainSensitiveWord> domainSensitiveWordList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(DomainSensitiveWord.class));
		return domainSensitiveWordList;
	}

	/**
	 * @see com.x.apa.loader.dao.DomainSensitiveWordDao#queryDomainSensitiveWords(com.x.apa.common.pageable.PageRequest)
	 */
	@Override
	public Page<DomainSensitiveWord> queryDomainSensitiveWords(PageRequest page) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, description, word, status, create_time ");
		sql.append(" from ");
		sql.append(" config_domain_sensitive_word ");

		StringBuilder where = new StringBuilder();
		where.append(" where deleted = :deleted ");
		sql.append(where);

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);

		PageUtils.appendPage(sql, page);

		List<DomainSensitiveWord> sensitiveWordList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(DomainSensitiveWord.class));

		// total
		StringBuilder totalSql = new StringBuilder();
		totalSql.append("select count(id) from ");
		totalSql.append(" config_domain_sensitive_word ");
		totalSql.append(where);

		Integer total = namedParamJdbcTemplate.queryForObject(totalSql.toString(), params, Integer.class);
		return new PageImpl<DomainSensitiveWord>(sensitiveWordList, page, total);
	}

	/**
	 * @see com.x.apa.loader.dao.DomainSensitiveWordDao#queryDomainSensitiveWord(java.lang.String)
	 */
	@Override
	public DomainSensitiveWord queryDomainSensitiveWord(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, description, word, status, create_time ");
		sql.append(" from ");
		sql.append(" config_domain_sensitive_word ");
		sql.append(" where deleted = :deleted ");
		sql.append(" and id = :id ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("id", id);

		List<DomainSensitiveWord> sensitiveWordList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(DomainSensitiveWord.class));
		return CollectionUtils.isEmpty(sensitiveWordList) ? new DomainSensitiveWord() : sensitiveWordList.get(0);
	}

	/**
	 * @see com.x.apa.loader.dao.DomainSensitiveWordDao#saveDomainSensitiveWord(com.x.apa.loader.data.DomainSensitiveWord)
	 */
	@Override
	public DomainSensitiveWord saveDomainSensitiveWord(DomainSensitiveWord domainSensitiveWord) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		sql.append(" config_domain_sensitive_word ");
		sql.append(" (id, name, description, word, creator_id, status, create_time, update_time, deleted) ");
		sql.append(
				" values (:id, :name, :description, :word, :creatorId, :status, :createTime, :updateTime, :deleted) ");

		domainSensitiveWord.setId(UUIDGenerator.generateUniqueID(""));
		domainSensitiveWord.setCreateTime(new Date());
		domainSensitiveWord.setUpdateTime(domainSensitiveWord.getCreateTime());
		domainSensitiveWord.setDeleted(DELETED_NO);

		SqlParameterSource ps = new BeanPropertySqlParameterSource(domainSensitiveWord);

		namedParamJdbcTemplate.update(sql.toString(), ps);
		return domainSensitiveWord;
	}

	/**
	 * @see com.x.apa.loader.dao.DomainSensitiveWordDao#updateDomainSensitiveWord(com.x.apa.loader.data.DomainSensitiveWord)
	 */
	@Override
	public int updateDomainSensitiveWord(DomainSensitiveWord domainSensitiveWord) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_domain_sensitive_word ");
		sql.append(" set name = :name, description = :description, word = :word, ");
		sql.append(" status = :status, update_time = :updateTime ");
		sql.append(" where id = :id");

		domainSensitiveWord.setUpdateTime(new Date());
		SqlParameterSource ps = new BeanPropertySqlParameterSource(domainSensitiveWord);
		return namedParamJdbcTemplate.update(sql.toString(), ps);
	}

	/**
	 * @see com.x.apa.loader.dao.DomainSensitiveWordDao#changeDomainSensitiveWordStatus(java.lang.String,
	 *      int)
	 */
	@Override
	public int changeDomainSensitiveWordStatus(String id, int status) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_domain_sensitive_word ");
		sql.append(" set status = :status, update_time = :updateTime ");
		sql.append(" where id = :id");

		Map<String, Object> params = Maps.newHashMap();
		params.put("id", id);
		params.put("status", status);
		params.put("updateTime", new Date());

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

	/**
	 * @see com.x.apa.loader.dao.DomainSensitiveWordDao#deleteDomainSensitiveWord(java.lang.String)
	 */
	@Override
	public int deleteDomainSensitiveWord(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_domain_sensitive_word ");
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
