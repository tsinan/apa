package com.x.apa.suspecturl.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Sets;
import com.x.apa.common.Constant;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.suspecturl.dao.TrustDomainDao;
import com.x.apa.suspecturl.data.TrustDomain;

/**
 * @author liumeng
 */
@Component
public class TrustDomainServiceImpl implements TrustDomainService, Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TrustDomainDao trustDomainDao;

	private Set<String> trustDomainNameSet;

	private static AtomicBoolean initial = new AtomicBoolean(false);

	/**
	 * 定时同步可信域名
	 */
	@Scheduled(initialDelay = 3 * 1000, fixedDelay = 5 * 1000)
	public void syncTrustDomain() {
		loadTrustDomainNames();
	}

	/**
	 * @see com.x.apa.suspecturl.service.TrustDomainService#isTrustDomain(java.lang.String)
	 */
	@Override
	public boolean isTrustDomain(String domain) {

		if (!initial.get()) {
			loadTrustDomainNames();
			initial.set(true);
		}

		domain = domain.toLowerCase();
		return trustDomainNameSet.contains(domain);
	}

	/**
	 * @see com.x.apa.suspecturl.service.TrustDomainService#queryTrustDomains(com.x.apa.common.pageable.PageRequest)
	 */
	@Override
	public Page<TrustDomain> queryTrustDomains(PageRequest page) {
		return trustDomainDao.queryTrustDomains(page);
	}

	/**
	 * @see com.x.apa.suspecturl.service.TrustDomainService#queryTrustDomain(java.lang.String)
	 */
	@Override
	public TrustDomain queryTrustDomain(String id) {
		return trustDomainDao.queryTrustDomain(id);
	}

	/**
	 * @see com.x.apa.suspecturl.service.TrustDomainService#createTrustDomain(com.x.apa.suspecturl.data.TrustDomain)
	 */
	@Override
	public TrustDomain createTrustDomain(TrustDomain trustDomain) {
		String domain = trustDomain.getDomainName();
		if (domain.endsWith("/")) {
			trustDomain.setDomainName(domain.substring(0, domain.length() - 1));
		}
		trustDomain.setDomainName(trustDomain.getDomainName().toLowerCase());
		return trustDomainDao.saveTrustDomain(trustDomain);
	}

	/**
	 * @see com.x.apa.suspecturl.service.TrustDomainService#updateTrustDomain(com.x.apa.suspecturl.data.TrustDomain)
	 */
	@Override
	public int updateTrustDomain(TrustDomain trustDomain) {
		String domain = trustDomain.getDomainName();
		if (domain.endsWith("/")) {
			trustDomain.setDomainName(domain.substring(0, domain.length() - 1));
		}
		trustDomain.setDomainName(trustDomain.getDomainName().toLowerCase());
		return trustDomainDao.updateTrustDomain(trustDomain);
	}

	/**
	 * @see com.x.apa.suspecturl.service.TrustDomainService#changeTrustDomainStatus(java.lang.String,
	 *      int)
	 */
	@Override
	public int changeTrustDomainStatus(String id, int status) {
		return trustDomainDao.changeTrustDomainStatus(id, status);
	}

	/**
	 * @see com.x.apa.suspecturl.service.TrustDomainService#deleteTrustDomain(java.lang.String)
	 */
	@Override
	public int deleteTrustDomain(String id) {
		return trustDomainDao.deleteTrustDomain(id);
	}

	private void loadTrustDomainNames() {

		Set<String> newTrustDomainSet = Sets.newHashSet();
		List<TrustDomain> trustDomainList = trustDomainDao.queryTrustDomains(true);
		for (TrustDomain trustDomain : trustDomainList) {
			newTrustDomainSet.add(trustDomain.getDomainName().toLowerCase());
		}
		trustDomainNameSet = newTrustDomainSet;

		logger.debug("load {} trust domains.", trustDomainNameSet.size());
	}

}
