package com.x.apa.inspecturl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.inspecturl.dao.BrandDao;
import com.x.apa.inspecturl.data.Brand;

/**
 * @author liumeng
 */
@Component
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandDao brandDao;

	/**
	 * @see com.x.apa.inspecturl.service.BrandService#queryBrands()
	 */
	@Override
	public List<Brand> queryBrands() {
		return brandDao.queryBrands();
	}

	/**
	 * @see com.x.apa.inspecturl.service.BrandService#queryBrands(com.x.apa.common.pageable.PageRequest)
	 */
	@Override
	public Page<Brand> queryBrands(PageRequest page) {
		return brandDao.queryBrands(page);
	}

	/**
	 * @see com.x.apa.inspecturl.service.BrandService#queryBrand(java.lang.String)
	 */
	@Override
	public Brand queryBrand(String id) {
		return brandDao.queryBrand(id);
	}

	/**
	 * @see com.x.apa.inspecturl.service.BrandService#createBrand(com.x.apa.inspecturl.data.Brand)
	 */
	@Override
	public Brand createBrand(Brand brand) {
		return brandDao.saveBrand(brand);
	}

	/**
	 * @see com.x.apa.inspecturl.service.BrandService#updateBrand(com.x.apa.inspecturl.data.Brand)
	 */
	@Override
	public int updateBrand(Brand brand) {
		return brandDao.updateBrand(brand);
	}

	/**
	 * @see com.x.apa.inspecturl.service.BrandService#deleteBrand(java.lang.String)
	 */
	@Override
	public int deleteBrand(String id) {
		return brandDao.deleteBrand(id);
	}

}
