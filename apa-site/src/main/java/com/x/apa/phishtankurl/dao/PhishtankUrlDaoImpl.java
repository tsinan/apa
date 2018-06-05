package com.x.apa.phishtankurl.dao;

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
import com.x.apa.phishtankurl.data.PhishtankUrl;

/**
 * @author liumeng
 */
@Component
public class PhishtankUrlDaoImpl implements PhishtankUrlDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.phishtankurl.dao.PhishtankUrlDao#queryPhishtankUrls(com.x.apa.common.pageable.PageRequest)
	 */
	@Override
	public Page<PhishtankUrl> queryPhishtankUrls(PageRequest page) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, phish_id, url, phish_detail_url, submission_time, verified,  ");
		sql.append(" verification_time, online, target, create_time ");
		sql.append(" from ");
		sql.append(" phishtank_valid_url ");

		StringBuilder where = new StringBuilder();
		where.append(" where deleted = :deleted ");
		sql.append(where);

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);

		PageUtils.appendPage(sql, page);

		List<PhishtankUrl> phishtankUrlList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(PhishtankUrl.class));

		// total
		StringBuilder totalSql = new StringBuilder();
		totalSql.append("select count(id) from ");
		totalSql.append(" phishtank_valid_url ");
		totalSql.append(where);

		Integer total = namedParamJdbcTemplate.queryForObject(totalSql.toString(), params, Integer.class);
		return new PageImpl<PhishtankUrl>(phishtankUrlList, page, total);
	}

	/**
	 * @see com.x.apa.phishtankurl.dao.PhishtankUrlDao#queryLatestVerifiedPhishtankUrl()
	 */
	@Override
	public PhishtankUrl queryLatestVerifiedPhishtankUrl() {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, phish_id, url, phish_detail_url, submission_time, verified,  ");
		sql.append(" verification_time, online, target, create_time ");
		sql.append(" from ");
		sql.append(" phishtank_valid_url ");
		sql.append(" where deleted = :deleted ");
		sql.append(" order by verification_time DESC limit 0,1 ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);

		List<PhishtankUrl> phishtankUrlList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(PhishtankUrl.class));
		return CollectionUtils.isEmpty(phishtankUrlList) ? new PhishtankUrl() : phishtankUrlList.get(0);
	}

	/**
	 * @see com.x.apa.phishtankurl.dao.PhishtankUrlDao#savePhishtankUrl(com.x.apa.phishtankurl.data.PhishtankUrl)
	 */
	@Override
	public PhishtankUrl savePhishtankUrl(PhishtankUrl phishtankUrl) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		sql.append(" phishtank_valid_url ");
		sql.append(" (id, phish_id, url, phish_detail_url, submission_time, verified,  ");
		sql.append("  verification_time, online, target, record_json, create_time, update_time, deleted) ");
		sql.append(" values (:id, :phishId, :url, :phishDetailUrl, :submissionTime, :verified, ");
		sql.append(" :verificationTime, :online, :target, :recordJson, :createTime, :updateTime, :deleted) ");

		phishtankUrl.setId(UUIDGenerator.generateUniqueID(""));
		phishtankUrl.setCreateTime(new Date());
		phishtankUrl.setUpdateTime(phishtankUrl.getCreateTime());
		phishtankUrl.setDeleted(DELETED_NO);

		SqlParameterSource ps = new BeanPropertySqlParameterSource(phishtankUrl);
		namedParamJdbcTemplate.update(sql.toString(), ps);
		return phishtankUrl;
	}

}
