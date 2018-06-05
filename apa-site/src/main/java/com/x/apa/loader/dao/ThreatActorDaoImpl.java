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
import com.x.apa.loader.data.ThreatActor;

/**
 * @author liumeng
 */
@Component
public class ThreatActorDaoImpl implements ThreatActorDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.loader.dao.ThreatActorDao#queryThreatActors(com.x.apa.common.pageable.PageRequest)
	 */
	@Override
	public Page<ThreatActor> queryThreatActors(PageRequest page) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, ip_address, registrant_email, description, status, create_time ");
		sql.append(" from ");
		sql.append(" config_threat_actor ");

		StringBuilder where = new StringBuilder();
		where.append(" where deleted = :deleted ");
		sql.append(where);

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);

		PageUtils.appendPage(sql, page);

		List<ThreatActor> threatActorList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(ThreatActor.class));

		// total
		StringBuilder totalSql = new StringBuilder();
		totalSql.append("select count(id) from ");
		totalSql.append(" config_threat_actor ");
		totalSql.append(where);

		Integer total = namedParamJdbcTemplate.queryForObject(totalSql.toString(), params, Integer.class);
		return new PageImpl<ThreatActor>(threatActorList, page, total);
	}

	/**
	 * @see com.x.apa.loader.dao.ThreatActorDao#queryThreatActors(boolean)
	 */
	@Override
	public List<ThreatActor> queryThreatActors(boolean openedOnly) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, ip_address, registrant_email, description, status ");
		sql.append(" from ");
		sql.append(" config_threat_actor ");
		sql.append(" where deleted = :deleted ");
		if (openedOnly) {
			sql.append(" and status = :status ");
		}
		sql.append(" order by create_time ASC ");
		
		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("status", STATUS_OPEN);

		List<ThreatActor> threatActorList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(ThreatActor.class));
		return threatActorList;
	}

	/**
	 * @see com.x.apa.loader.dao.ThreatActorDao#queryThreatActor(java.lang.String)
	 */
	@Override
	public ThreatActor queryThreatActor(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, ip_address, registrant_email, description, status ");
		sql.append(" from ");
		sql.append(" config_threat_actor ");
		sql.append(" where deleted = :deleted ");
		sql.append(" and id = :id ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("id", id);

		List<ThreatActor> threatActorList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(ThreatActor.class));
		return CollectionUtils.isNotEmpty(threatActorList) ? threatActorList.get(0) : new ThreatActor();
	}

	/**
	 * @see com.x.apa.loader.dao.ThreatActorDao#queryThreatActorByIpOrEmail(java.lang.String)
	 */
	@Override
	public ThreatActor queryThreatActorByIpAndEmail(String ip, String email) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, ip_address, registrant_email, description, status ");
		sql.append(" from ");
		sql.append(" config_threat_actor ");
		sql.append(" where deleted = :deleted ");
		sql.append(" and ( ip_address = :ip and registrant_email = :email ) ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("ip", ip);
		params.put("email", email);

		List<ThreatActor> threatActorList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(ThreatActor.class));
		return CollectionUtils.isNotEmpty(threatActorList) ? threatActorList.get(0) : new ThreatActor();
	}

	/**
	 * @see com.x.apa.loader.dao.ThreatActorDao#saveThreatActor(com.x.apa.loader.data.ThreatActor)
	 */
	@Override
	public ThreatActor saveThreatActor(ThreatActor threatActor) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		sql.append(" config_threat_actor ");
		sql.append(" (id, ip_address, registrant_email, description, ");
		sql.append(" creator_id, status, create_time, update_time, deleted) ");
		sql.append(" values (:id, :ipAddress, :registrantEmail, :description, ");
		sql.append(" :creatorId, :status, :createTime, :updateTime, :deleted) ");

		threatActor.setId(UUIDGenerator.generateUniqueID(""));
		threatActor.setCreateTime(new Date());
		threatActor.setUpdateTime(threatActor.getCreateTime());
		threatActor.setDeleted(DELETED_NO);

		SqlParameterSource ps = new BeanPropertySqlParameterSource(threatActor);
		namedParamJdbcTemplate.update(sql.toString(), ps);
		return threatActor;
	}

	/**
	 * @see com.x.apa.loader.dao.ThreatActorDao#updateThreatActor(com.x.apa.loader.data.ThreatActor)
	 */
	@Override
	public int updateThreatActor(ThreatActor threatActor) {
		StringBuilder sql = new StringBuilder();
		sql.append("update config_threat_actor ");
		sql.append(" set ip_address = :ipAddress, registrant_email = :registrantEmail, description = :description ");
		sql.append(" where id = :id  ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("ipAddress", threatActor.getIpAddress());
		params.put("registrantEmail", threatActor.getRegistrantEmail());
		params.put("description", threatActor.getDescription());
		params.put("id", threatActor.getId());

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

	/**
	 * @see com.x.apa.loader.dao.ThreatActorDao#changeThreatActorStatus(java.lang.String,
	 *      int)
	 */
	@Override
	public int changeThreatActorStatus(String id, int status) {
		StringBuilder sql = new StringBuilder();
		sql.append("update config_threat_actor ");
		sql.append(" set status = :status ");
		sql.append(" where id = :id  ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("status", status);
		params.put("id", id);

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

	/**
	 * @see com.x.apa.loader.dao.ThreatActorDao#deleteThreatActor(java.lang.String)
	 */
	@Override
	public int deleteThreatActor(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from config_threat_actor ");
		sql.append(" where id = :id  ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("id", id);

		return namedParamJdbcTemplate.update(sql.toString(), params);
	}

}
