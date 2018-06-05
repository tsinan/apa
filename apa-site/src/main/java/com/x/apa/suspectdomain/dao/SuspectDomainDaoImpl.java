package com.x.apa.suspectdomain.dao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.common.data.SuspectDomain;
import com.x.apa.common.util.UUIDGenerator;

/**
 * @author liumeng
 */
@Component
public class SuspectDomainDaoImpl implements SuspectDomainDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.suspectdomain.dao.SuspectDomainDao#saveSuspectDomain(com.x.apa.suspectdomain.data.SuspectDomain)
	 */
	@Override
	public SuspectDomain saveSuspectDomain(SuspectDomain suspectDomain) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		sql.append(" sd_suspect_domain ");
		sql.append(" (id, domain_name, suspect_match, suspect_match_obj_id, suspect_match_obj_text, ");
		sql.append(" raw_domain_id, create_time, update_time, deleted) ");
		sql.append(" values (:id, :domainName, :suspectMatch, :suspectMatchObjId, :suspectMatchObjText, ");
		sql.append(" :rawDomainId, :createTime, :updateTime, :deleted) ");

		suspectDomain.setId(UUIDGenerator.generateUniqueID(""));
		suspectDomain.setCreateTime(new Date());
		suspectDomain.setUpdateTime(suspectDomain.getCreateTime());
		suspectDomain.setDeleted(DELETED_NO);

		SqlParameterSource ps = new BeanPropertySqlParameterSource(suspectDomain);
		namedParamJdbcTemplate.update(sql.toString(), ps);
		return suspectDomain;
	}

}
