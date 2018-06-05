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
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageImpl;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.common.util.PageUtils;
import com.x.apa.common.util.UUIDGenerator;
import com.x.apa.suspecturl.data.TrustDomain;

/**
 * @author liumeng
 */
@Component
public class TrustDomainDaoImpl implements TrustDomainDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.suspecturl.dao.TrustDomainDao#queryTrustDomains(boolean)
	 */
	@Override
	public List<TrustDomain> queryTrustDomains(boolean openOnly) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, description, domain_name, creator_id, status, create_time ");
		sql.append(" from ");
		sql.append(" config_trust_domain ");
		sql.append(" where deleted = :deleted ");
		if (openOnly) {
			sql.append(" and status = :status ");
		}
		sql.append(" order by domain_name ASC ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("status", STATUS_OPEN);

		List<TrustDomain> trustDomainList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(TrustDomain.class));
		return trustDomainList;
	}

	/**
	 * @see com.x.apa.suspecturl.dao.TrustDomainDao#queryTrustDomains(com.x.apa.common.pageable.PageRequest)
	 */
	@Override
	public Page<TrustDomain> queryTrustDomains(PageRequest page) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, description, domain_name, creator_id, status, create_time ");
		sql.append(" from ");
		sql.append(" config_trust_domain ");

		StringBuilder where = new StringBuilder();
		where.append(" where deleted = :deleted ");
		sql.append(where);

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);

		PageUtils.appendPage(sql, page);

		List<TrustDomain> trustDomainList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(TrustDomain.class));

		// total
		StringBuilder totalSql = new StringBuilder();
		totalSql.append("select count(id) from ");
		totalSql.append(" config_trust_domain ");
		totalSql.append(where);

		Integer total = namedParamJdbcTemplate.queryForObject(totalSql.toString(), params, Integer.class);
		return new PageImpl<TrustDomain>(trustDomainList, page, total);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.TrustDomainDao#queryTrustDomain(java.lang.String)
	 */
	@Override
	public TrustDomain queryTrustDomain(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, description, domain_name, creator_id, status, create_time ");
		sql.append(" from ");
		sql.append(" config_trust_domain ");
		sql.append(" where deleted = :deleted ");
		sql.append(" and id = :id ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("id", id);

		List<TrustDomain> trustDomainList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(TrustDomain.class));
		return CollectionUtils.isEmpty(trustDomainList) ? new TrustDomain() : trustDomainList.get(0);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.TrustDomainDao#saveTrustDomain(com.x.apa.suspecturl.data.TrustDomain)
	 */
	@Override
	public TrustDomain saveTrustDomain(TrustDomain trustDomain) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		sql.append(" config_trust_domain ");
		sql.append(" (id, name, description, domain_name, creator_id, status, create_time, update_time, deleted) ");
		sql.append(
				" values (:id, :name, :description, :domainName, :creatorId, :status, :createTime, :updateTime, :deleted) ");

		trustDomain.setId(UUIDGenerator.generateUniqueID(""));
		trustDomain.setCreateTime(new Date());
		trustDomain.setUpdateTime(trustDomain.getCreateTime());
		trustDomain.setDeleted(DELETED_NO);

		SqlParameterSource ps = new BeanPropertySqlParameterSource(trustDomain);

		namedParamJdbcTemplate.update(sql.toString(), ps);
		return trustDomain;
	}

	/**
	 * @see com.x.apa.suspecturl.dao.TrustDomainDao#updateTrustDomain(com.x.apa.suspecturl.data.TrustDomain)
	 */
	@Override
	public int updateTrustDomain(TrustDomain trustDomain) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_trust_domain ");
		sql.append(" set name = :name, description = :description, domain_name = :domainName, ");
		sql.append(" status = :status, update_time = :updateTime ");
		sql.append(" where id = :id");

		trustDomain.setUpdateTime(new Date());
		SqlParameterSource ps = new BeanPropertySqlParameterSource(trustDomain);
		return namedParamJdbcTemplate.update(sql.toString(), ps);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.TrustDomainDao#changeTrustDomainStatus(java.lang.String,int)
	 */
	@Override
	public int changeTrustDomainStatus(String trustDomainId, int status) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_trust_domain ");
		sql.append(" set status = :status, update_time = :updateTime ");
		sql.append(" where id = :trustDomainId");

		Map<String, Object> params = Maps.newHashMap();
		params.put("trustDomainId", trustDomainId);
		params.put("status", status);
		params.put("updateTime", new Date());

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.TrustDomainDao#deleteTrustDomain(java.lang.String)
	 */
	@Override
	public int deleteTrustDomain(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_trust_domain ");
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
