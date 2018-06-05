package com.x.apa.suspectdomain.strategy.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.x.apa.common.Constant;
import com.x.apa.common.data.RawDomain;
import com.x.apa.suspectdomain.dao.DomainRegularDao;
import com.x.apa.suspectdomain.data.DomainRegular;
import com.x.apa.suspectdomain.strategy.DomainStrategyIntf;

/**
 * @author liumeng
 */
@Component
@Order(5)
public class RegularMatchStrategy implements DomainStrategyIntf, Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static AtomicBoolean initial = new AtomicBoolean(false);

	private List<Pattern> patterns;
	private Map<Pattern, DomainRegular> patternMap;

	@Autowired
	private DomainRegularDao domainRegularDao;

	/**
	 * @see com.x.apa.suspectdomain.strategy.DomainStrategyIntf#doMatch(com.x.apa.common.data.RawDomain)
	 */
	@Override
	public Map<String, String> doMatch(RawDomain rawDomain) {

		if (!initial.get()) {
			loadDomainRegulars();
			initial.set(true);
		}

		Map<String, String> matchResult = null;

		// 遍历正则表达式进行匹配
		String domainName = rawDomain.getDomainName().toLowerCase();
		if (StringUtils.isBlank(domainName)) {
			return matchResult;
		}
		for (Pattern pattern : patterns) {
			Matcher matcher = pattern.matcher(domainName);
			if (!matcher.find()) {
				continue;
			}

			DomainRegular domainRegular = patternMap.get(pattern);
			if (domainRegular != null) {
				matchResult = Maps.newHashMap();
				matchResult.put("match", SUSPECT_DOMAIN_MATCH_REGULAR);
				matchResult.put("matchObjId", domainRegular.getId());
				matchResult.put("matchObjText", domainRegular.getExpression());
				return matchResult;
			}
		}
		return matchResult;
	}

	/**
	 * 定时更新正则表达式
	 */
	@Scheduled(initialDelay = 3 * 1000, fixedDelay = 5 * 1000)
	public void syncDomainRegulars() {
		loadDomainRegulars();
	}

	private void loadDomainRegulars() {
		List<Pattern> newPatterns = Lists.newArrayList();
		Map<Pattern, DomainRegular> newPatternMap = Maps.newHashMap();

		List<DomainRegular> domainRegulars = domainRegularDao.queryDomainRegulars(true);
		for (DomainRegular regular : domainRegulars) {
			Pattern pattern = Pattern.compile(regular.getExpression());

			newPatterns.add(pattern);
			newPatternMap.put(pattern, regular);
		}
		this.patterns = newPatterns;
		this.patternMap = newPatternMap;

		logger.debug("load {} domain regulars. ", domainRegulars.size());
	}

}
