package com.x.apa.suspecturl.service;

import java.util.Date;
import java.util.List;

import com.x.apa.common.data.SuspectDomain;
import com.x.apa.suspecturl.data.ClueUrl;

/**
 * @author liumeng
 */
public interface ClueUrlService {

	List<ClueUrl> createClueUrls(SuspectDomain suspectDomain);

	int updateClueUrlProgress(ClueUrl clueUrl);
	
	int deleteClueUrlBeforeDate(Date date);

}
