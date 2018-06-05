package com.x.apa.suspecturl.dao;

import java.util.Date;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.suspecturl.data.SuspectUrl;

/**
 * @author liumeng
 */
public interface SuspectUrlDao {

	Page<SuspectUrl> querySuspectUrls(PageRequest page, String tagId, String urlLike);

	SuspectUrl querySuspectUrl(String id);

	SuspectUrl saveSuspectUrl(SuspectUrl suspectUrl);

	int updateSuspectUrlVerify(String id, int verify);
	
	int deleteSupsectUrl(String id);
	
	int deleteSuspectUrlBeforeDate(Date date);
}
