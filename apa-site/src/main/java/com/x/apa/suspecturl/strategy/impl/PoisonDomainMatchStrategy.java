package com.x.apa.suspecturl.strategy.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Maps;
import com.x.apa.common.Constant;
import com.x.apa.common.metric.MetricCenter;
import com.x.apa.suspecturl.dao.PoisonDomainDao;
import com.x.apa.suspecturl.data.ClueUrl;
import com.x.apa.suspecturl.data.PoisonDomain;
import com.x.apa.suspecturl.strategy.UrlStrategyIntf;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author liumeng
 */
@Component
@Order(2)
public class PoisonDomainMatchStrategy implements UrlStrategyIntf, Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private AtomicBoolean initial = new AtomicBoolean(false);

	@Autowired
	private PoisonDomainDao poisonDomainDao;

	private Map<String, PoisonDomain> poisonDomainMap;

	/**
	 * @see com.x.apa.suspecturl.strategy.UrlStrategyIntf#doMatch(com.x.apa.suspecturl.data.ClueUrl)
	 */
	@Override
	public Map<String, String> doMatch(ClueUrl clueUrl) {

		if (!initial.get()) {
			loadPoisonDomains();
			initial.set(true);
		}

		Map<String, String> matchResult = null;

		// 检查
		String poisonDomain = "";
		String outgoingUrl = "";
		for (HtmlParseData htmlParseData : clueUrl.getHtmlDataList()) {
			if (StringUtils.isNotBlank(poisonDomain)) {
				break;
			}

			Set<WebURL> outgoings = htmlParseData.getOutgoingUrls();
			if (CollectionUtils.isEmpty(outgoings)) {
				continue;
			}

			for (WebURL outgoing : outgoings) {
				String domainName = outgoing.getDomain().toLowerCase();
				if (domainName.indexOf(":") >= 0) {
					domainName = domainName.substring(0, domainName.indexOf(":"));
				}
				PoisonDomain domain = poisonDomainMap.get(domainName);
				if (domain != null) {
					poisonDomain = domain.getDomainName();
					outgoingUrl = outgoing.getURL();
					break;
				}
			}
		}

		// 如果中毒，返回
		if (StringUtils.isNotBlank(poisonDomain)) {
			MetricCenter.countSuspectUrlMatch("PoisonDomainMatchStrategy_Hit");

			matchResult = Maps.newHashMap();
			matchResult.put("matchedTagResult", SUSPECT_URL_MATCH_NO);
			matchResult.put("matchedTagRule", outgoingUrl + " > " + poisonDomain);
		} else {
			MetricCenter.countSuspectUrlMatch("PoisonDomainMatchStrategy_Pass");
		}

		return matchResult;
	}

	/**
	 * 定时更新标签规则
	 */
	@Scheduled(initialDelay = 3 * 1000, fixedDelay = 5 * 1000)
	public void syncPoisonDomains() {
		loadPoisonDomains();
	}

	private void loadPoisonDomains() {
		Map<String, PoisonDomain> poisonDomainMap = Maps.newHashMap();
		List<PoisonDomain> poisonDomainList = poisonDomainDao.queryPoisonDomains(true);
		for (PoisonDomain poisonDomain : poisonDomainList) {
			poisonDomainMap.put(poisonDomain.getDomainName().toLowerCase(), poisonDomain);
		}
		this.poisonDomainMap = poisonDomainMap;

		logger.debug("load {} poison domains.", poisonDomainMap.size());
	}

}
