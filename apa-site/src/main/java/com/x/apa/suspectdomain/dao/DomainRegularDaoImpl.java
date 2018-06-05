package com.x.apa.suspectdomain.dao;

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
import com.x.apa.suspectdomain.data.DomainRegular;

/**
 * @author liumeng
 */
@Component
public class DomainRegularDaoImpl implements DomainRegularDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.suspectdomain.dao.DomainRegularDao#queryDomainRegulars(boolean)
	 */
	@Override
	public List<DomainRegular> queryDomainRegulars(boolean openedOnly) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, description, expression, status ");
		sql.append(" from ");
		sql.append(" config_domain_regular ");
		sql.append(" where deleted = :deleted ");
		if (openedOnly) {
			sql.append(" and status = :status ");
		}

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("status", STATUS_OPEN);

		List<DomainRegular> domainRegularList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(DomainRegular.class));
		return domainRegularList;
	}

	/**
	 * @see com.x.apa.suspectdomain.dao.DomainRegularDao#queryDomainRequlars(com.x.apa.common.pageable.PageRequest)
	 */
	@Override
	public Page<DomainRegular> queryDomainRequlars(PageRequest page) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, description, expression, status, create_time ");
		sql.append(" from ");
		sql.append(" config_domain_regular ");

		StringBuilder where = new StringBuilder();
		where.append(" where deleted = :deleted ");
		sql.append(where);

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);

		PageUtils.appendPage(sql, page);

		List<DomainRegular> regularList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(DomainRegular.class));

		// total
		StringBuilder totalSql = new StringBuilder();
		totalSql.append("select count(id) from ");
		totalSql.append(" config_domain_regular ");
		totalSql.append(where);

		Integer total = namedParamJdbcTemplate.queryForObject(totalSql.toString(), params, Integer.class);
		return new PageImpl<DomainRegular>(regularList, page, total);
	}

	/**
	 * @see com.x.apa.suspectdomain.dao.DomainRegularDao#queryDomainReqular(java.lang.String)
	 */
	@Override
	public DomainRegular queryDomainReqular(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, description, expression, status, create_time ");
		sql.append(" from ");
		sql.append(" config_domain_regular ");
		sql.append(" where deleted = :deleted ");
		sql.append(" and id = :id ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("id", id);

		List<DomainRegular> regularList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(DomainRegular.class));
		return CollectionUtils.isEmpty(regularList) ? new DomainRegular() : regularList.get(0);
	}

	/**
	 * @see com.x.apa.suspectdomain.dao.DomainRegularDao#saveDomainRegular(com.x.apa.suspectdomain.data.DomainRegular)
	 */
	@Override
	public DomainRegular saveDomainRegular(DomainRegular regular) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		sql.append(" config_domain_regular ");
		sql.append(" (id, name, description, expression, creator_id, status, create_time, update_time, deleted) ");
		sql.append(
				" values (:id, :name, :description, :expression, :creatorId, :status, :createTime, :updateTime, :deleted) ");

		regular.setId(UUIDGenerator.generateUniqueID(""));
		regular.setCreateTime(new Date());
		regular.setUpdateTime(regular.getCreateTime());
		regular.setDeleted(DELETED_NO);

		SqlParameterSource ps = new BeanPropertySqlParameterSource(regular);

		namedParamJdbcTemplate.update(sql.toString(), ps);
		return regular;
	}

	/**
	 * @see com.x.apa.suspectdomain.dao.DomainRegularDao#updateDomainRegular(com.x.apa.suspectdomain.data.DomainRegular)
	 */
	@Override
	public int updateDomainRegular(DomainRegular regular) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_domain_regular ");
		sql.append(" set name = :name, description = :description, expression = :expression, ");
		sql.append(" status = :status, update_time = :updateTime ");
		sql.append(" where id = :id");

		regular.setUpdateTime(new Date());
		SqlParameterSource ps = new BeanPropertySqlParameterSource(regular);
		return namedParamJdbcTemplate.update(sql.toString(), ps);
	}

	/**
	 * @see com.x.apa.suspectdomain.dao.DomainRegularDao#changeDomainRegularStatus(java.lang.String,int)
	 */
	@Override
	public int changeDomainRegularStatus(String id, int status) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_domain_regular ");
		sql.append(" set status = :status, update_time = :updateTime ");
		sql.append(" where id = :regularId");

		Map<String, Object> params = Maps.newHashMap();
		params.put("id", id);
		params.put("status", status);
		params.put("updateTime", new Date());

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

	/**
	 * @see com.x.apa.suspectdomain.dao.DomainRegularDao#deleteDomainRegular(java.lang.String)
	 */
	@Override
	public int deleteDomainRegular(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_domain_regular ");
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
