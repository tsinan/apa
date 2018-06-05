package com.x.apa.suspecturl.strategy;

import java.util.Map;

import com.x.apa.suspecturl.data.ClueUrl;

/**
 * @author liumeng
 */
public interface UrlStrategyIntf {

	Map<String, String> doMatch(ClueUrl clueUrl);
}
