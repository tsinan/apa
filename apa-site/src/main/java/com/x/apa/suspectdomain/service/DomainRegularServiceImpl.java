package com.x.apa.suspectdomain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.suspectdomain.dao.DomainRegularDao;
import com.x.apa.suspectdomain.data.DomainRegular;

/**
 * @author liumeng
 */
@Component
public class DomainRegularServiceImpl implements DomainRegularService, Constant {

	@Autowired
	private DomainRegularDao domainRegularDao;

	/**
	 * @see com.x.apa.suspectdomain.service.DomainRegularService#queryDomainRegulars(com.x.apa.common.pageable.PageRequest)
	 */
	@Override
	public Page<DomainRegular> queryDomainRegulars(PageRequest page) {
		return domainRegularDao.queryDomainRequlars(page);
	}

	/**
	 * @see com.x.apa.suspectdomain.service.DomainRegularService#createDomainRegular(com.x.apa.suspectdomain.data.DomainRegular)
	 */
	@Override
	public void createDomainRegular(DomainRegular domainRegular) {
		domainRegularDao.saveDomainRegular(domainRegular);
	}

	/**
	 * @see com.x.apa.suspectdomain.service.DomainRegularService#updateDomainRegular(com.x.apa.suspectdomain.data.DomainRegular)
	 */
	@Override
	public int updateDomainRegular(DomainRegular domainRegular) {
		return domainRegularDao.updateDomainRegular(domainRegular);
	}

	/**
	 * @see com.x.apa.suspectdomain.service.DomainRegularService#changeDomainRegularStatus(java.lang.String,
	 *      int)
	 */
	@Override
	public int changeDomainRegularStatus(String id, int status) {
		return domainRegularDao.changeDomainRegularStatus(id, status);
	}

	/**
	 * @see com.x.apa.suspectdomain.service.DomainRegularService#deleteDomainRegular(java.lang.String)
	 */
	@Override
	public int deleteDomainRegular(String id) {
		return domainRegularDao.deleteDomainRegular(id);
	}

}
