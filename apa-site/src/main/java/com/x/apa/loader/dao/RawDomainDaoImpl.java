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
import com.x.apa.common.data.RawDomain;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageImpl;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.common.util.DateUtils;
import com.x.apa.common.util.PageUtils;
import com.x.apa.common.util.UUIDGenerator;

/**
 * @author liumeng
 */
@Component
public class RawDomainDaoImpl implements RawDomainDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.loader.dao.RawDomainDao#queryLatestRawDomains()
	 */
	@Override
	public RawDomain queryLatestRawDomains() {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, domain_name, registration_date, is_sensitive, registrant_name, registrant_email, ");
		sql.append(" registrant_phone, domain_registrar_id, create_time ");
		sql.append(" from ");
		sql.append(" v_ld_raw_domain ");
		sql.append(" where deleted = :deleted ");
		sql.append(" order by registration_date DESC limit 0,1 ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);

		List<RawDomain> domainList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(RawDomain.class));
		return CollectionUtils.isEmpty(domainList) ? new RawDomain() : domainList.get(0);
	}

	/**
	 * @see com.x.apa.loader.dao.RawDomainDao#queryRawDomains(com.x.apa.common.pageable.PageRequest,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Page<RawDomain> queryRawDomains(PageRequest page, String startTime, String endTime, String domainLike) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, domain_name, registration_date, is_sensitive, registrant_name, registrant_email, ");
		sql.append(" registrant_phone, domain_registrar_id, create_time ");
		sql.append(" from ");
		sql.append(" v_ld_raw_domain ");

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

		List<RawDomain> rawDomainList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(RawDomain.class));

		// total
		StringBuilder totalSql = new StringBuilder();
		totalSql.append("select count(id) from ");
		totalSql.append(" v_ld_raw_domain ");
		totalSql.append(where);

		Integer total = namedParamJdbcTemplate.queryForObject(totalSql.toString(), params, Integer.class);
		return new PageImpl<RawDomain>(rawDomainList, page, total);
	}

	/**
	 * @see com.x.apa.loader.dao.RawDomainDao#queryRawDomains(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public List<RawDomain> queryRawDomains(String startTime, String endTime, String domainLike) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, domain_name, registration_date, is_sensitive, registrant_name, registrant_email, ");
		sql.append(" registrant_phone, domain_registrar_id, create_time ");
		sql.append(" from ");
		sql.append(" v_ld_raw_domain ");
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

		return namedParamJdbcTemplate.query(sql.toString(), params, new BeanPropertyRowMapper<>(RawDomain.class));
	}

	/**
	 * @see com.x.apa.loader.dao.RawDomainDao#queryRawDomains(int, int,
	 *      java.util.Date)
	 */
	@Override
	public List<RawDomain> queryRawDomains(int startPos, int size, Date date) {
		StringBuilder sql = new StringBuilder();

		if (date == null) {
			sql.append("select id, domain_name, registration_date, is_sensitive ");
			sql.append(" from ");
			sql.append(" v_ld_raw_domain ");
			sql.append(" where id in (select t.id from (select id from v_ld_raw_domain order by id ASC limit "
					+ startPos + "," + size + ") as t) ");
		} else {
			sql.append("select id, domain_name, registration_date, is_sensitive ");
			sql.append(" from ");
			sql.append(" ld_raw_domain ");
			sql.append(" where create_time >= :startTime and create_time < :endTime order by create_time ASC limit "
					+ startPos + "," + size);
		}

		Map<String, Object> params = Maps.newHashMap();
		params.put("startTime", DateUtils.toString_YYYY_MM_DD(DateUtils.beforeDayDate(date, 1)));
		params.put("endTime", DateUtils.toString_YYYY_MM_DD(date));

		return namedParamJdbcTemplate.query(sql.toString(), params, new BeanPropertyRowMapper<>(RawDomain.class));
	}

	/**
	 * @see com.x.apa.loader.dao.RawDomainDao#saveRawDomain(com.x.apa.common.data.RawDomain)
	 */
	@Override
	public RawDomain saveRawDomain(RawDomain rawDomain) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		sql.append(" ld_raw_domain ");
		sql.append(" (id, domain_name, registration_date, is_sensitive, registrant_name, registrant_email, ");
		sql.append(" registrant_phone, domain_registrar_id, record_json, create_time, update_time, deleted) ");
		sql.append(" values (:id, :domainName, :registrationDate, :isSensitive, :registrantName, :registrantEmail, ");
		sql.append(" :registrantPhone, :domainRegistrarId, :recordJson, :createTime, :updateTime, :deleted) ");

		rawDomain.setId(UUIDGenerator.generateUniqueID(""));
		rawDomain.setCreateTime(new Date());
		rawDomain.setUpdateTime(rawDomain.getCreateTime());
		rawDomain.setDeleted(DELETED_NO);

		SqlParameterSource ps = new BeanPropertySqlParameterSource(rawDomain);
		namedParamJdbcTemplate.update(sql.toString(), ps);
		return rawDomain;
	}

	/**
	 * @see com.x.apa.loader.dao.RawDomainDao#changeRawDomainSensitive(java.lang.String,
	 *      int)
	 */
	@Override
	public int changeRawDomainSensitive(String domainName, int isSensitive) {
		StringBuilder sql = new StringBuilder();
		sql.append("update ld_raw_domain ");
		sql.append(" set is_sensitive = :isSensitive ");
		sql.append(" where domain_name = :domainName  ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("domainName", domainName);
		params.put("isSensitive", isSensitive);

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

}
