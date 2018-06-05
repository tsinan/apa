package com.x.apa.suspecturl.service;

import com.x.apa.suspecturl.data.ClueUrlTemplate;

/**
 * @author liumeng
 */
public interface ClueUrlTemplateService {

	ClueUrlTemplate queryClueUrlTemplate();
	
	int updateClueUrlTemplate(ClueUrlTemplate clueUrlTemplate);
}
