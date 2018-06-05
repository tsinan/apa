package com.x.apa.inspecturl.service;

import java.util.List;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.inspecturl.data.Brand;

/**
 * @author liumeng
 */
public interface BrandService {

	List<Brand> queryBrands();
	
	Page<Brand> queryBrands(PageRequest page);
	
	Brand queryBrand(String id);
	
	Brand createBrand(Brand brand);
	
	int updateBrand(Brand brand);
	
	int deleteBrand(String id);
}
