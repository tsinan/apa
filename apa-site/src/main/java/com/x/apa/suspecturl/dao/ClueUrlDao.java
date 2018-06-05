package com.x.apa.suspecturl.dao;

import java.util.Date;

import com.x.apa.suspecturl.data.ClueUrl;

/**
 * @author liumeng
 */
public interface ClueUrlDao {

	ClueUrl saveClueUrl(ClueUrl clueUrl);

	int updateClueUrl(ClueUrl clueUrl);

	int deleteClueUrlBeforeDate(Date date);
}
