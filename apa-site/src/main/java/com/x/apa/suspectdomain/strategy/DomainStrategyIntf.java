package com.x.apa.suspectdomain.strategy;

import java.util.Map;

import com.x.apa.common.data.RawDomain;

/**
 * @author liumeng
 */
public interface DomainStrategyIntf {

	Map<String, String> doMatch(RawDomain rawDomain);
}
