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
import com.x.apa.suspecturl.data.PoisonDomain;

/**
 * @author liumeng
 */
@Component
public class PoisonDomainDaoImpl implements PoisonDomainDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.suspecturl.dao.PoisonDomainDao#queryPoisonDomains(boolean)
	 */
	@Override
	public List<PoisonDomain> queryPoisonDomains(boolean openOnly) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, description, domain_name, creator_id, status, create_time ");
		sql.append(" from ");
		sql.append(" config_poison_domain ");
		sql.append(" where deleted = :deleted ");
		if (openOnly) {
			sql.append(" and status = :status ");
		}
		sql.append(" order by domain_name ASC ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("status", STATUS_OPEN);

		List<PoisonDomain> poisonDomainList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(PoisonDomain.class));
		return poisonDomainList;
	}

	/**
	 * @see com.x.apa.suspecturl.dao.PoisonDomainDao#queryPoisonDomains(com.x.apa.common.pageable.PageRequest)
	 */
	@Override
	public Page<PoisonDomain> queryPoisonDomains(PageRequest page) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, description, domain_name, creator_id, status, create_time ");
		sql.append(" from ");
		sql.append(" config_poison_domain ");

		StringBuilder where = new StringBuilder();
		where.append(" where deleted = :deleted ");
		sql.append(where);

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);

		PageUtils.appendPage(sql, page);

		List<PoisonDomain> poisonDomainList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(PoisonDomain.class));

		// total
		StringBuilder totalSql = new StringBuilder();
		totalSql.append("select count(id) from ");
		totalSql.append(" config_poison_domain ");
		totalSql.append(where);

		Integer total = namedParamJdbcTemplate.queryForObject(totalSql.toString(), params, Integer.class);
		return new PageImpl<PoisonDomain>(poisonDomainList, page, total);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.PoisonDomainDao#queryPoisonDomain(java.lang.String)
	 */
	@Override
	public PoisonDomain queryPoisonDomain(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, description, domain_name, creator_id, status, create_time ");
		sql.append(" from ");
		sql.append(" config_poison_domain ");
		sql.append(" where deleted = :deleted ");
		sql.append(" and id = :id ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("id", id);

		List<PoisonDomain> poisonDomainList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(PoisonDomain.class));
		return CollectionUtils.isEmpty(poisonDomainList) ? new PoisonDomain() : poisonDomainList.get(0);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.PoisonDomainDao#savePoisonDomain(com.x.apa.suspecturl.data.PoisonDomain)
	 */
	@Override
	public PoisonDomain savePoisonDomain(PoisonDomain poisonDomain) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		sql.append(" config_poison_domain ");
		sql.append(" (id, name, description, domain_name, creator_id, status, create_time, update_time, deleted) ");
		sql.append(
				" values (:id, :name, :description, :domainName, :creatorId, :status, :createTime, :updateTime, :deleted) ");

		poisonDomain.setId(UUIDGenerator.generateUniqueID(""));
		poisonDomain.setCreateTime(new Date());
		poisonDomain.setUpdateTime(poisonDomain.getCreateTime());
		poisonDomain.setDeleted(DELETED_NO);

		SqlParameterSource ps = new BeanPropertySqlParameterSource(poisonDomain);

		namedParamJdbcTemplate.update(sql.toString(), ps);
		return poisonDomain;
	}

	/**
	 * @see com.x.apa.suspecturl.dao.PoisonDomainDao#updatePoisonDomain(com.x.apa.suspecturl.data.PoisonDomain)
	 */
	@Override
	public int updatePoisonDomain(PoisonDomain poisonDomain) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_poison_domain ");
		sql.append(" set name = :name, description = :description, domain_name = :domainName, ");
		sql.append(" status = :status, update_time = :updateTime ");
		sql.append(" where id = :id");

		poisonDomain.setUpdateTime(new Date());
		SqlParameterSource ps = new BeanPropertySqlParameterSource(poisonDomain);
		return namedParamJdbcTemplate.update(sql.toString(), ps);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.PoisonDomainDao#changePoisonDomainStatus(java.lang.String,int)
	 */
	@Override
	public int changePoisonDomainStatus(String poisonDomainId, int status) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_poison_domain ");
		sql.append(" set status = :status, update_time = :updateTime ");
		sql.append(" where id = :poisonDomainId");

		Map<String, Object> params = Maps.newHashMap();
		params.put("poisonDomainId", poisonDomainId);
		params.put("status", status);
		params.put("updateTime", new Date());

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

	/**
	 * @see com.x.apa.suspecturl.dao.PoisonDomainDao#deletePoisonDomain(java.lang.String)
	 */
	@Override
	public int deletePoisonDomain(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_poison_domain ");
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
