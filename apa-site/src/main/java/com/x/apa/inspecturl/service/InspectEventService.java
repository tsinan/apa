package com.x.apa.inspecturl.service;

import java.util.Date;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.inspecturl.data.InspectEvent;

/**
 * @author liumeng
 */
public interface InspectEventService {

	Page<InspectEvent> queryInspectEvents(PageRequest page, String progress, String urlLike);

	int changeInspectEventProgress(String id, int progress);

	int deleteInspectEvent(String id);

	int deleteInspectEventBeforeDate(Date date);
}
