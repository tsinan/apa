package com.x.apa.loader.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.loader.dao.ThreatActorDao;
import com.x.apa.loader.data.ThreatActor;

/**
 * @author liumeng
 */
@Component
public class ThreatActorServiceImpl implements ThreatActorService, Constant {

	@Autowired
	private ThreatActorDao threatActorDao;

	/**
	 * @see com.x.apa.loader.service.ThreatActorService#queryThreatActors(boolean)
	 */
	@Override
	public List<ThreatActor> queryThreatActors(boolean openedOnly) {
		return threatActorDao.queryThreatActors(openedOnly);
	}

	/**
	 * @see com.x.apa.loader.service.SensitiveDomainService#querySensitiveDomains(com.x.apa.common.pageable.PageRequest)
	 */
	@Override
	public Page<ThreatActor> queryThreatActors(PageRequest page) {
		return threatActorDao.queryThreatActors(page);
	}

	/**
	 * @see com.x.apa.loader.service.ThreatActorService#queryThreatActor(java.lang.String)
	 */
	@Override
	public ThreatActor queryThreatActor(String id) {
		return threatActorDao.queryThreatActor(id);
	}

	/**
	 * @see com.x.apa.loader.service.ThreatActorService#createThreatActor(com.x.apa.loader.data.ThreatActor)
	 */
	@Override
	public ThreatActor createThreatActor(ThreatActor threatActor) {

		ThreatActor exsitThreatActor = threatActorDao.queryThreatActorByIpAndEmail(threatActor.getIpAddress(),
				threatActor.getRegistrantEmail());
		if (StringUtils.isNotBlank(exsitThreatActor.getId())) {
			return exsitThreatActor;
		}
		return threatActorDao.saveThreatActor(threatActor);
	}

	/**
	 * @see com.x.apa.loader.service.ThreatActorService#updateThreatActor(com.x.apa.loader.data.ThreatActor)
	 */
	@Override
	public int updateThreatActor(ThreatActor threatActor) {
		return threatActorDao.updateThreatActor(threatActor);
	}

	/**
	 * @see com.x.apa.loader.service.ThreatActorService#changeThreatActorStatus(java.lang.String, int)
	 */
	@Override
	public int changeThreatActorStatus(String id, int status) {
		return threatActorDao.changeThreatActorStatus(id, status);
	}

	/**
	 * @see com.x.apa.loader.service.ThreatActorService#deleteThreatActor(java.lang.String)
	 */
	@Override
	public int deleteThreatActor(String id) {

		ThreatActor exsitThreatActor = threatActorDao.queryThreatActor(id);
		if (StringUtils.isBlank(exsitThreatActor.getId())) {
			return 0;
		}
		return threatActorDao.deleteThreatActor(id);
	}

}
