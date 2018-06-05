package com.x.apa.suspectdomain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.common.data.SuspectDomain;
import com.x.apa.suspectdomain.dao.SuspectDomainDao;

/**
 * @author liumeng
 */
@Component
public class SuspectDomainServiceImpl implements SuspectDomainService, Constant {

	@Autowired
	private SuspectDomainDao suspectDomainDao;

	/**
	 * @see com.x.apa.suspectdomain.service.SuspectDomainService#createSuspectDomain(com.x.apa.suspectdomain.data.RegularSuspectMatch)
	 */
	@Override
	public SuspectDomain createSuspectDomain(SuspectDomain suspectDomain) {
		return suspectDomainDao.saveSuspectDomain(suspectDomain);
	}

}
