//package com.x.apa.suspectdomain.strategy.impl;
//
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import com.google.common.collect.Maps;
//import com.x.apa.common.Constant;
//import com.x.apa.common.data.RawDomain;
//import com.x.apa.loader.dao.ThreatActorDao;
//import com.x.apa.loader.data.ThreatActor;
//import com.x.apa.suspectdomain.strategy.DomainStrategyIntf;
//
///**
// * @author liumeng
// */
//@Component
//@Order(9)
//public class ThreatActorMatchStrategy implements DomainStrategyIntf, Constant {
//
//	private Logger logger = LoggerFactory.getLogger(getClass());
//
//	private Map<String, String> keyByRegistrantName = Maps.newConcurrentMap();
//	private Map<String, String> keyByRegistrantEmail = Maps.newConcurrentMap();
//	private Map<String, String> keyByRegistrantPhone = Maps.newConcurrentMap();
//
//	private static AtomicBoolean initial = new AtomicBoolean(false);
//
//	@Autowired
//	private ThreatActorDao threatActorDao;
//
//	/**
//	 * @see com.x.apa.suspectdomain.strategy.WhoisStrategyIntf#doMatch(com.x.apa.common.data.RawDomain)
//	 */
//	@Override
//	public Map<String, String> doMatch(RawDomain rawDomain) {
//
//		if (!initial.get()) {
//			loadThreatActors();
//			initial.set(true);
//		}
//
//		Map<String, String> matchResult = null;
//		String matchObjId = "";
//		String matchObjText = "";
//
//		if (StringUtils.isNotBlank(rawDomain.getRegistrantName())
//				&& (matchObjId = keyByRegistrantName.get(rawDomain.getRegistrantName().toLowerCase())) != null) {
//			matchObjText = "registrantName:" + rawDomain.getRegistrantName().toLowerCase();
//		} else if (StringUtils.isNotBlank(rawDomain.getRegistrantEmail())
//				&& (matchObjId = keyByRegistrantEmail.get(rawDomain.getRegistrantEmail().toLowerCase())) != null) {
//			matchObjText = "registrantEmail:" + rawDomain.getRegistrantEmail().toLowerCase();
//		} else if (StringUtils.isNotBlank(rawDomain.getRegistrantPhone())
//				&& (matchObjId = keyByRegistrantPhone.get(rawDomain.getRegistrantPhone().toLowerCase())) != null) {
//			matchObjText = "registrantPhone:" + rawDomain.getRegistrantPhone().toLowerCase();
//		}
//
//		if (StringUtils.isNotBlank(matchObjId)) {
//			matchResult = Maps.newHashMap();
//			matchResult.put("match", SUSPECT_DOMAIN_MATCH_WHOIS);
//			matchResult.put("matchObjId", matchObjId);
//			matchResult.put("matchObjText", matchObjText);
//		}
//		return matchResult;
//	}
//
//	private void loadThreatActors() {
//
//		List<ThreatActor> threatActors = threatActorDao.queryThreatActors(true);
//		for (ThreatActor ta : threatActors) {
//
//			if (StringUtils.isNotBlank(ta.getRegistrantName())) {
//				keyByRegistrantName.put(ta.getRegistrantName().toLowerCase(), ta.getId());
//			}
//
//			if (StringUtils.isNotBlank(ta.getRegistrantEmail())) {
//				keyByRegistrantEmail.put(ta.getRegistrantEmail().toLowerCase(), ta.getId());
//			}
//
//			if (StringUtils.isNotBlank(ta.getRegistrantPhone())) {
//				keyByRegistrantPhone.put(ta.getRegistrantPhone().toLowerCase(), ta.getId());
//			}
//		}
//
//		logger.info("load {} threat actors. ", threatActors.size());
//		return;
//	}
//
//}
