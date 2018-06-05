package com.x.apa.suspecturl.strategy.impl;

import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Maps;
import com.x.apa.common.Constant;
import com.x.apa.common.metric.MetricCenter;
import com.x.apa.common.util.HotPropertiesAccessor;
import com.x.apa.suspecturl.data.ClueUrl;
import com.x.apa.suspecturl.strategy.UrlStrategyIntf;

/**
 * @author liumeng
 */
@Component
@Order(1)
public class OutlinkNumMatchStrategy implements UrlStrategyIntf, Constant {

	/**
	 * @see com.x.apa.suspecturl.strategy.UrlStrategyIntf#doMatch(com.x.apa.suspecturl.data.ClueUrl)
	 */
	@Override
	public Map<String, String> doMatch(ClueUrl clueUrl) {

		Map<String, String> matchResult = null;

		// 是否绕过
		boolean bypass = BooleanUtils.toBoolean(HotPropertiesAccessor.getProperty("su.strategy.outlink.bypass"));
		if (bypass) {
			return matchResult;
		}

		int suspectUrlMaxOutlinkNum = Integer
				.parseInt(HotPropertiesAccessor.getProperty("su.strategy.max.outlink.number"));
		int suspectUrlMaxImageNum = Integer.parseInt(HotPropertiesAccessor.getProperty("su.strategy.max.image.number"));

		if (clueUrl.getPageOutlinkNum() > suspectUrlMaxOutlinkNum) {
			MetricCenter.countSuspectUrlMatch("OutlinkNumMatchStrategy_Outlink");

			matchResult = Maps.newHashMap();
			matchResult.put("matchedTagResult", SUSPECT_URL_MATCH_NO);
			matchResult.put("matchedTagRule", clueUrl.getPageOutlinkNum() + " > " + suspectUrlMaxOutlinkNum);
		} else if (clueUrl.getPageImageNum() > suspectUrlMaxImageNum) {
			MetricCenter.countSuspectUrlMatch("OutlinkNumMatchStrategy_Image");

			matchResult = Maps.newHashMap();
			matchResult.put("matchedTagResult", SUSPECT_URL_MATCH_NO);
			matchResult.put("matchedTagRule", clueUrl.getPageImageNum() + " > " + suspectUrlMaxImageNum);
		} else {
			MetricCenter.countSuspectUrlMatch("OutlinkNumMatchStrategy_Pass");
		}

		return matchResult;
	}

}
