package com.x.apa.suspecturl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.suspecturl.dao.PoisonDomainDao;
import com.x.apa.suspecturl.data.PoisonDomain;

/**
 * @author liumeng
 */
@Component
public class PoisonDomainServiceImpl implements PoisonDomainService, Constant {

	@Autowired
	private PoisonDomainDao poisonDomainDao;

	/**
	 * @see com.x.apa.suspecturl.service.PoisonDomainService#queryPoisonDomains(com.x.apa.common.pageable.PageRequest)
	 */
	@Override
	public Page<PoisonDomain> queryPoisonDomains(PageRequest page) {
		return poisonDomainDao.queryPoisonDomains(page);
	}

	/**
	 * @see com.x.apa.suspecturl.service.PoisonDomainService#queryPoisonDomain(java.lang.String)
	 */
	@Override
	public PoisonDomain queryPoisonDomain(String id) {
		return poisonDomainDao.queryPoisonDomain(id);
	}

	/**
	 * @see com.x.apa.suspecturl.service.PoisonDomainService#createPoisonDomain(com.x.apa.suspecturl.data.PoisonDomain)
	 */
	@Override
	public PoisonDomain createPoisonDomain(PoisonDomain poisonDomain) {
		String domain = poisonDomain.getDomainName();
		if (domain.endsWith("/")) {
			poisonDomain.setDomainName(domain.substring(0, domain.length() - 1));
		}
		poisonDomain.setDomainName(poisonDomain.getDomainName().toLowerCase());
		return poisonDomainDao.savePoisonDomain(poisonDomain);
	}

	/**
	 * @see com.x.apa.suspecturl.service.PoisonDomainService#updatePoisonDomain(com.x.apa.suspecturl.data.PoisonDomain)
	 */
	@Override
	public int updatePoisonDomain(PoisonDomain poisonDomain) {
		String domain = poisonDomain.getDomainName();
		if (domain.endsWith("/")) {
			poisonDomain.setDomainName(domain.substring(0, domain.length() - 1));
		}
		poisonDomain.setDomainName(poisonDomain.getDomainName().toLowerCase());
		return poisonDomainDao.updatePoisonDomain(poisonDomain);
	}

	/**
	 * @see com.x.apa.suspecturl.service.PoisonDomainService#changePoisonDomainStatus(java.lang.String,
	 *      int)
	 */
	@Override
	public int changePoisonDomainStatus(String id, int status) {
		return poisonDomainDao.changePoisonDomainStatus(id, status);
	}

	/**
	 * @see com.x.apa.suspecturl.service.PoisonDomainService#deletePoisonDomain(java.lang.String)
	 */
	@Override
	public int deletePoisonDomain(String id) {
		return poisonDomainDao.deletePoisonDomain(id);
	}

}
