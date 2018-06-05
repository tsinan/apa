package com.x.apa.inspecturl.dao;

import java.util.Date;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.inspecturl.data.InspectTrace;

/**
 * @author liumeng
 */
public interface InspectTraceDao {

	Page<InspectTrace> queryInspectTraces(PageRequest page, String inspectUrlId);

	InspectTrace queryInspectTrace(String inspectTraceId);

	InspectTrace saveInspectTrace(InspectTrace inspectTrace);

	int deleteInspectTraceBeforeDate(Date date);
}
