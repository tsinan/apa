package com.x.apa.inspecturl.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.inspecturl.dao.InspectEventDao;
import com.x.apa.inspecturl.data.InspectEvent;

/**
 * @author liumeng
 */
@Component
public class InspectEventServiceImpl implements InspectEventService, Constant {

	@Autowired
	private InspectEventDao inspectEventDao;

	/**
	 * @see com.x.apa.inspecturl.service.InspectEventService#queryInspectEvents(com.x.apa.common.pageable.PageRequest,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public Page<InspectEvent> queryInspectEvents(PageRequest page, String progress, String urlLike) {
		return inspectEventDao.queryInspectEvents(page, progress, urlLike);
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectEventService#changeInspectEventProgress(java.lang.String,
	 *      int)
	 */
	@Override
	public int changeInspectEventProgress(String id, int progress) {
		return inspectEventDao.changeInspectEventProgress(id, progress);
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectEventService#deleteInspectEvent(java.lang.String)
	 */
	@Override
	public int deleteInspectEvent(String id) {
		return inspectEventDao.deleteInspectEvent(id);
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectEventService#deleteInspectEventBeforeDate(java.util.Date)
	 */
	@Override
	public int deleteInspectEventBeforeDate(Date date) {
		return inspectEventDao.deleteInspectEventBeforeDate(date);
	}

}
