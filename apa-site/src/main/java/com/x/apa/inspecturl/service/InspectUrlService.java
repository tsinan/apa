package com.x.apa.inspecturl.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.inspecturl.data.InspectUrl;
import com.x.apa.inspecturl.vo.InspectUrlQuery;

import edu.uci.ics.crawler4j.parser.HtmlParseData;

/**
 * @author liumeng
 */
public interface InspectUrlService {

	List<InspectUrl> queryInspectUrlsByNextTime(Date datetime);

	Set<String> queryInspectUrlDomains();

	List<InspectUrl> queryInspectUrls(boolean onlyLocked);

	Page<InspectUrl> queryInspectUrls(PageRequest page, InspectUrlQuery inspectUrlQuery);

	HSSFWorkbook exportInpsectUrl(InspectUrlQuery inspectUrlQuery);

	InspectUrl queryInspectUrl(String id);

	int changeInspectUrlLock(String id, boolean isLock);

	InspectUrl createInspectUrl(InspectUrl inspectUrl);

	int updateInspectUrl(InspectUrl inspectUrl);
	
	int updateInpsectUrlWhois(InspectUrl inspectUrl);

	void updateInspectUrlStatus(InspectUrl inspectUrl, int statusCode, Header[] headers,
			List<HtmlParseData> htmlDataList);

	int changeInspectUrlLevel(String inspectUrlId, int inspectLevel);

	int deleteInspectUrl(String id);

}
