package com.x.apa.suspectdomain.strategy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.x.apa.common.data.RawDomain;

/**
 * @author liumeng
 */
@Component
public class DomainStrategyChain {

	@Autowired
	private List<DomainStrategyIntf> strategies = Lists.newArrayList();

	public Map<String, String> match(RawDomain rawDomain) {

		Map<String, String> matchResult = null;
		for (DomainStrategyIntf strategy : strategies) {
			if ((matchResult = strategy.doMatch(rawDomain)) != null) {
				break;
			}
		}
		return matchResult;
	}
}
