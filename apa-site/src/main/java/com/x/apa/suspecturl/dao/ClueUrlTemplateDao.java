package com.x.apa.suspecturl.dao;

import com.x.apa.suspecturl.data.ClueUrlTemplate;

/**
 * @author liumeng
 */
public interface ClueUrlTemplateDao {

	ClueUrlTemplate queryClueUrlTemplate(String id);

	int updateClueUrlTemplate(ClueUrlTemplate clueUrlTemplate);
}
