package com.x.apa.inspecturl.dao;

import java.util.Date;
import java.util.List;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.inspecturl.data.InspectUrl;
import com.x.apa.inspecturl.vo.InspectUrlQuery;

/**
 * @author liumeng
 */
public interface InspectUrlDao {

	List<InspectUrl> queryInspectUrlsByNextTime(Date datetime);

	List<InspectUrl> queryInspectUrls(boolean onlyLocked);

	Page<InspectUrl> queryInspectUrls(PageRequest page, InspectUrlQuery inspectUrlQuery);

	List<InspectUrl> queryInspectUrls(InspectUrlQuery inspectUrlQuery);

	InspectUrl queryInspectUrl(String id);
	
	InspectUrl queryInspectUrlByUrl(String url);

	InspectUrl saveInspectUrl(InspectUrl inspectUrl);

	int updateInspectUrl(InspectUrl inspectUrl);

	int updateInspectUrlStatus(InspectUrl inspectUrl);
	
	int updateInpsectUrlWhois(InspectUrl inspectUrl);

	int changeInspectUrlLock(String id, boolean lockOrUnlock);

	int changeInspectUrlLevel(String inspectUrlId, int inspectLevel, int inspectStatus);

	int deleteInspectUrl(String id);

}
