package com.x.apa.inspecturl.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.inspecturl.dao.InspectTraceDao;
import com.x.apa.inspecturl.data.InspectTrace;

/**
 * @author liumeng
 */
@Component
public class InspectTraceServiceImpl implements InspectTraceService, Constant {

	@Autowired
	private InspectTraceDao inspectTraceDao;

	/**
	 * @see com.x.apa.inspecturl.service.InspectTraceService#queryInspectTraces(com.x.apa.common.pageable.PageRequest,
	 *      java.lang.String)
	 */
	@Override
	public Page<InspectTrace> queryInspectTraces(PageRequest page, String inspectUrlId) {
		return inspectTraceDao.queryInspectTraces(page, inspectUrlId);
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectTraceService#queryInspectTrace(java.lang.String)
	 */
	@Override
	public InspectTrace queryInspectTrace(String inspectTraceId) {
		return inspectTraceDao.queryInspectTrace(inspectTraceId);
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectTraceService#createInspectTrace(com.x.apa.inspecturl.data.InspectTrace)
	 */
	@Override
	public InspectTrace createInspectTrace(InspectTrace inspectTrace) {
		return inspectTraceDao.saveInspectTrace(inspectTrace);
	}

	/**
	 * @see com.x.apa.inspecturl.service.InspectTraceService#deleteInspectTraceBeforeDate(java.util.Date)
	 */
	@Override
	public int deleteInspectTraceBeforeDate(Date date) {
		return inspectTraceDao.deleteInspectTraceBeforeDate(date);
	}

}
