package com.x.apa.inspecturl.dao;

import java.util.Date;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.inspecturl.data.InspectEvent;

/**
 * @author liumeng
 */
public interface InspectEventDao {

	Page<InspectEvent> queryInspectEvents(PageRequest page, String progress, String urlLike);

	InspectEvent saveInspectEvent(InspectEvent inspectEvent);

	int changeInspectEventProgress(String id, int progress);

	int deleteInspectEvent(String id);

	int deleteInspectEventBeforeDate(Date date);

}
