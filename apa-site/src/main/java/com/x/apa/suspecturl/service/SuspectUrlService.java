package com.x.apa.suspecturl.service;

import java.util.Date;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.suspecturl.data.ClueUrl;
import com.x.apa.suspecturl.data.SuspectUrl;

/**
 * @author liumeng
 */
public interface SuspectUrlService {

	void discernSuspectUrl(ClueUrl clueUrl);

	Page<SuspectUrl> querySuspectUrls(PageRequest page, String tagId, String urlLike);

	SuspectUrl querySuspectUrl(String id);

	void changeSuspectUrlVerify(String id, int verify);
	
	int deleteSuspectUrlBeforeDate(Date date);

}
