package com.x.apa.inspecturl.dao;

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
import com.x.apa.inspecturl.data.Brand;

/**
 * @author liumeng
 */
@Component
public class BrandDaoImpl implements BrandDao, Constant {

	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;

	/**
	 * @see com.x.apa.inspecturl.dao.BrandDao#queryBrands()
	 */
	@Override
	public List<Brand> queryBrands() {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, category, description, creator_id, create_time ");
		sql.append(" from ");
		sql.append(" config_brand ");
		sql.append(" where deleted = :deleted ");
		sql.append(" order by name ASC ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);

		List<Brand> brandList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(Brand.class));
		return brandList;
	}

	/**
	 * @see com.x.apa.inspecturl.dao.BrandDao#queryBrands(com.x.apa.common.pageable.PageRequest)
	 */
	@Override
	public Page<Brand> queryBrands(PageRequest page) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, category, description, creator_id, create_time ");
		sql.append(" from ");
		sql.append(" config_brand ");

		StringBuilder where = new StringBuilder();
		where.append(" where deleted = :deleted ");
		sql.append(where);

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);

		PageUtils.appendPage(sql, page);

		List<Brand> brandList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(Brand.class));

		// total
		StringBuilder totalSql = new StringBuilder();
		totalSql.append("select count(id) from ");
		totalSql.append(" config_brand ");
		totalSql.append(where);

		Integer total = namedParamJdbcTemplate.queryForObject(totalSql.toString(), params, Integer.class);
		return new PageImpl<Brand>(brandList, page, total);
	}

	/**
	 * @see com.x.apa.inspecturl.dao.BrandDao#queryBrand(java.lang.String)
	 */
	@Override
	public Brand queryBrand(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, name, category, description, creator_id, create_time ");
		sql.append(" from ");
		sql.append(" config_brand ");
		sql.append(" where deleted = :deleted ");
		sql.append(" and id = :id ");

		Map<String, Object> params = Maps.newHashMap();
		params.put("deleted", DELETED_NO);
		params.put("id", id);

		List<Brand> brandList = namedParamJdbcTemplate.query(sql.toString(), params,
				new BeanPropertyRowMapper<>(Brand.class));
		return CollectionUtils.isEmpty(brandList) ? new Brand() : brandList.get(0);
	}

	/**
	 * @see com.x.apa.inspecturl.dao.BrandDao#saveBrand(com.x.apa.inspecturl.data.Brand)
	 */
	@Override
	public Brand saveBrand(Brand brand) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ");
		sql.append(" config_brand ");
		sql.append(" (id, name, category, description, creator_id, create_time, update_time, deleted) ");
		sql.append(" values (:id, :name, :category, :description, :creatorId, :createTime, :updateTime, :deleted) ");

		brand.setId(UUIDGenerator.generateUniqueID(""));
		brand.setCreateTime(new Date());
		brand.setUpdateTime(brand.getCreateTime());
		brand.setDeleted(DELETED_NO);

		SqlParameterSource ps = new BeanPropertySqlParameterSource(brand);

		namedParamJdbcTemplate.update(sql.toString(), ps);
		return brand;
	}

	/**
	 * @see com.x.apa.inspecturl.dao.BrandDao#updateBrand(com.x.apa.inspecturl.data.Brand)
	 */
	@Override
	public int updateBrand(Brand brand) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_brand ");
		sql.append(" set name = :name, category = :category, description = :description, ");
		sql.append(" update_time = :updateTime ");
		sql.append(" where id = :id");

		brand.setUpdateTime(new Date());
		SqlParameterSource ps = new BeanPropertySqlParameterSource(brand);
		return namedParamJdbcTemplate.update(sql.toString(), ps);
	}

	/**
	 * @see com.x.apa.inspecturl.dao.BrandDao#deleteBrand(java.lang.String)
	 */
	@Override
	public int deleteBrand(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update ");
		sql.append(" config_brand ");
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
