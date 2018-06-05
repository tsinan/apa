package com.x.apa.inspecturl.service;

import java.util.Date;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.inspecturl.data.InspectTrace;

/**
 * @author liumeng
 */
public interface InspectTraceService {

	Page<InspectTrace> queryInspectTraces(PageRequest page, String inspectUrlId);

	InspectTrace queryInspectTrace(String inspectTraceId);

	InspectTrace createInspectTrace(InspectTrace inspectTrace);

	int deleteInspectTraceBeforeDate(Date date);
}
