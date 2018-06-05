package com.x.apa.loader.dao;

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
import com.x.apa.loader.data.SensitiveDomain;

/**
 * @author liumeng
 */
@Component
public class SensitiveDomainDaoImpl implements SensitiveDomainDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.loader.dao.SensitiveDomainDao#querySensitiveDomains(com.x.apa.common.pageable.PageRequest,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Page<SensitiveDomain> querySensitiveDomains(PageRequest page, String startTime, String endTime,
			String domainLike) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, category, domain_name, sensitive_word_name, sensitive_word, ");
		sql.append(" lcs_length, registration_date, whois_reg_name, whois_reg_email, create_time ");
		sql.append(" from ");
		sql.append(" ld_sensitive_domain ");

		StringBuilder where = new StringBuilder();
		where.append(" where deleted = :deleted ");
		if (StringUtils.isNotBlank(startTime)) {
			where.append(" and create_time >= :startTime ");
		}
		if (StringUtils.isNotBlank(endTime)) {
			where.append(" and create_time < :endTime ");
		}
		if (StringUtils.isNotBlank(domainLike)) {
			where.append(" and domain_name like :domainLike ");
		}
		sql.append(where);

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("domainLike", "%" + domainLike + "%");

		PageUtils.appendPage(sql, page);

		List<SensitiveDomain> sensitiveDomainList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(SensitiveDomain.class));

		// total
		StringBuilder totalSql = new StringBuilder();
		totalSql.append("select count(id) from ");
		totalSql.append(" ld_sensitive_domain ");
		totalSql.append(where);

		Integer total = namedParamJdbcTemplate.queryForObject(totalSql.toString(), params, Integer.class);
		return new PageImpl<SensitiveDomain>(sensitiveDomainList, page, total);
	}

	/**
	 * @see com.x.apa.loader.dao.SensitiveDomainDao#querySensitiveDomains(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public List<SensitiveDomain> querySensitiveDomains(String startTime, String endTime, String domainLike) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, category, domain_name, sensitive_word_name, sensitive_word, ");
		sql.append(" lcs_length, registration_date, whois_reg_name, whois_reg_email, create_time ");
		sql.append(" from ");
		sql.append(" ld_sensitive_domain ");
		sql.append(" where deleted = :deleted ");
		if (StringUtils.isNotBlank(startTime)) {
			sql.append(" and create_time >= :startTime ");
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql.append(" and create_time < :endTime ");
		}
		if (StringUtils.isNotBlank(domainLike)) {
			sql.append(" and domain_name like :domainLike ");
		}
		sql.append(" order by create_time ASC ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("domainLike", "%" + domainLike + "%");

		return namedParamJdbcTemplate.query(sql.toString(), params, new BeanPropertyRowMapper<>(SensitiveDomain.class));
	}

	/**
	 * @see com.x.apa.loader.dao.SensitiveDomainDao#querySensitiveDomain(java.lang.String)
	 */
	@Override
	public SensitiveDomain querySensitiveDomain(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, category, domain_name, sensitive_word_name, sensitive_word, ");
		sql.append(" lcs_length, registration_date, whois_reg_name, whois_reg_email, create_time ");
		sql.append(" from ");
		sql.append(" ld_sensitive_domain ");
		sql.append(" where deleted = :deleted ");
		sql.append(" and id = :id ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("id", id);

		List<SensitiveDomain> sensitiveDomainList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(SensitiveDomain.class));
		return CollectionUtils.isEmpty(sensitiveDomainList) ? new SensitiveDomain() : sensitiveDomainList.get(0);
	}

	/**
	 * @see com.x.apa.loader.dao.SensitiveDomainDao#querySensitiveDomainByDomainName(java.lang.String)
	 */
	@Override
	public SensitiveDomain querySensitiveDomainByDomainName(String domainName) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, category, domain_name, sensitive_word_name, sensitive_word, ");
		sql.append(" lcs_length, registration_date, whois_reg_name, whois_reg_email, create_time ");
		sql.append(" from ");
		sql.append(" ld_sensitive_domain ");
		sql.append(" where deleted = :deleted ");
		sql.append(" and domain_name = :domainName ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("domainName", domainName);

		List<SensitiveDomain> sensitiveDomainList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(SensitiveDomain.class));
		return CollectionUtils.isEmpty(sensitiveDomainList) ? new SensitiveDomain() : sensitiveDomainList.get(0);
	}

	/**
	 * @see com.x.apa.loader.dao.SensitiveDomainDao#saveSensitiveDomain(com.x.apa.loader.data.SensitiveDomain)
	 */
	@Override
	public SensitiveDomain saveSensitiveDomain(SensitiveDomain sensitiveDomain) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		sql.append(" ld_sensitive_domain ");
		sql.append(" (id, category, domain_name, sensitive_word_name, sensitive_word, lcs_length, ");
		sql.append(" registration_date, whois_reg_name, whois_reg_email, create_time, update_time, deleted) ");
		sql.append(" values (:id, :category, :domainName, :sensitiveWordName, :sensitiveWord, :lcsLength, ");
		sql.append(" :registrationDate, :whoisRegName, :whoisRegEmail, :createTime, :updateTime, :deleted) ");

		sensitiveDomain.setId(UUIDGenerator.generateUniqueID(""));
		sensitiveDomain.setCreateTime(new Date());
		sensitiveDomain.setUpdateTime(sensitiveDomain.getCreateTime());
		sensitiveDomain.setDeleted(DELETED_NO);

		SqlParameterSource ps = new BeanPropertySqlParameterSource(sensitiveDomain);
		namedParamJdbcTemplate.update(sql.toString(), ps);
		return sensitiveDomain;
	}

	/**
	 * @see com.x.apa.loader.dao.SensitiveDomainDao#updateSensitiveDomain(com.x.apa.loader.data.SensitiveDomain)
	 */
	@Override
	public int updateSensitiveDomain(SensitiveDomain sensitiveDomain) {
		StringBuilder sql = new StringBuilder();
		sql.append("update ld_sensitive_domain ");
		sql.append(" set domain_name = :domainName ");
		sql.append(" where id = :id  ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("domainName", sensitiveDomain.getDomainName());
		params.put("id", sensitiveDomain.getId());

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}
	
	/**
	 * @see com.x.apa.loader.dao.SensitiveDomainDao#updateSensitiveDomainWhois(com.x.apa.loader.data.SensitiveDomain)
	 */
	@Override
	public int updateSensitiveDomainWhois(SensitiveDomain sensitiveDomain){
		StringBuilder sql = new StringBuilder();
		sql.append("update ld_sensitive_domain ");
		sql.append(" set whois_reg_name = :whoisRegName, whois_reg_email = :whoisRegEmail ");
		sql.append(" where id = :id  ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("whoisRegName", sensitiveDomain.getWhoisRegName());
		params.put("whoisRegEmail", sensitiveDomain.getWhoisRegEmail());
		params.put("id", sensitiveDomain.getId());

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

	/**
	 * @see com.x.apa.loader.dao.SensitiveDomainDao#deleteLearningSensitiveDomain()
	 */
	@Override
	public int deleteLearningSensitiveDomain() {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ld_sensitive_domain ");
		sql.append(" where category = :category  ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("category", SENSITIVE_DOMAIN_CATEGORY_LEARNING);

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

	/**
	 * @see com.x.apa.loader.dao.SensitiveDomainDao#deleteSensitiveDomain(java.lang.String)
	 */
	@Override
	public int deleteSensitiveDomain(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ld_sensitive_domain ");
		sql.append(" where id = :id  ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("id", id);

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

}
