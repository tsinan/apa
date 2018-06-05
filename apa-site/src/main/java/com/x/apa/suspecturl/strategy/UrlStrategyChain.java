package com.x.apa.suspecturl.strategy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.x.apa.suspecturl.data.ClueUrl;

/**
 * @author liumeng
 */
@Component
public class UrlStrategyChain {

	@Autowired
	private List<UrlStrategyIntf> strategies = Lists.newArrayList();

	public Map<String, String> match(ClueUrl clueUrl) {

		Map<String, String> matchResult = null;
		for (UrlStrategyIntf strategy : strategies) {
			if ((matchResult = strategy.doMatch(clueUrl)) != null) {
				break;
			}
		}
		return matchResult;
	}
}
