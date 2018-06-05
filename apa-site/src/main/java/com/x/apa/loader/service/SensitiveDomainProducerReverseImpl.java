package com.x.apa.loader.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.x.apa.common.Constant;
import com.x.apa.common.util.DateUtils;
import com.x.apa.common.util.HotPropertiesAccessor;
import com.x.apa.common.util.JsonHelper;
import com.x.apa.loader.data.SensitiveDomain;
import com.x.apa.loader.data.ThreatActor;

/**
 * @author liumeng
 */
@Service
public class SensitiveDomainProducerReverseImpl implements SensitiveDomainProducer, Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ThreatActorService threatActorService;

	@Autowired
	private SensitiveDomainService sensitiveDomainService;

	/**
	 * @see com.x.apa.loader.service.SensitiveDomainProducer#produce(java.util.Date)
	 */
	@Override
	public int produce(Date date) {
		int count = 0;

		boolean produce = BooleanUtils.toBoolean(HotPropertiesAccessor.getProperty("ld.sensitive.produce.daily.reverse"));
		if (!produce) {
			logger.info("produce sensitive domain daily by reverse escape...");
			return count;
		}
		
		// appkey设置
		String appkey = HotPropertiesAccessor.getProperty("common.whois.api.appkey");
		if (StringUtils.isBlank(appkey)) {
			return count;
		}

		// 每月总配额
		int quotaOfMonth = Integer.parseInt(HotPropertiesAccessor.getProperty("common.whois.api.quota"));
		int quotaOfDay = quotaOfMonth / 31;

		// 查询敏感域名，如果已经被添加到敏感域名，则不需要重复添加
		Set<String> sensitiveDomainSet = sensitiveDomainService.querySensitiveDomainNames();

		// ThreatActor读取
		List<ThreatActor> threatActorList = threatActorService.queryThreatActors(true);
		int totalThreatActors = threatActorList.size();
		// 如果TA配置数量超过每天可以查询的配额，需要计算查询的子集合
		if (totalThreatActors > quotaOfDay) {
			// 集合size除以每天配额取上整数，计算得出查询一轮需要几天
			int daysOfRound = (int) Math.ceil(totalThreatActors / (double) quotaOfDay);
			// 当天在一月中是第几天，从0开始
			int dayOfMonth = DateUtils.getDayOfMonth(new Date()) - 1;
			// 当天天数模当月批次数量，得出当天所属批次
			int times = dayOfMonth % daysOfRound;
			threatActorList = threatActorList.subList(times * quotaOfDay,
					(times + 1) * quotaOfDay > totalThreatActors ? totalThreatActors : (times + 1) * quotaOfDay);
		}

		CloseableHttpClient httpclient = HttpClients.createDefault();
		for (ThreatActor threatActor : threatActorList) {
			// IP反查
			if (StringUtils.isNotBlank(threatActor.getIpAddress())) {

				Map<String, Object> jsonMap = reverseQueryByIp(threatActor, httpclient, appkey);
				if (MapUtils.isEmpty(jsonMap)) {
					continue;
				}

				@SuppressWarnings("unchecked")
				Map<String, Object> responseMap = (Map<String, Object>) jsonMap.get("response");
				if (MapUtils.isEmpty(responseMap)) {
					continue;
				}

				int domainCount = MapUtils.getInteger(responseMap, "domain_count", 0);
				if (domainCount <= 0) {
					continue;
				}

				@SuppressWarnings("unchecked")
				List<Map<String, String>> domains = (List<Map<String, String>>) responseMap.get("domains");
				if (domainCount != domains.size()) {
					logger.warn("domainCount={}, domains.size={}", domainCount, domains.size());
				}

				for (Map<String, String> domain : domains) {
					String domainName = domain.get("name");
					domainName = domainName.toLowerCase();
					String lastResolved = domain.get("last_resolved");
					logger.debug("domain={}, lastResolved={}", domainName, lastResolved);

					if (sensitiveDomainSet.contains(domainName)) {
						continue;
					}
					sensitiveDomainSet.add(domainName);

					SensitiveDomain sensitiveDomain = new SensitiveDomain();
					sensitiveDomain.setCategory(SENSITIVE_DOMAIN_CATEGORY_IP_REVERSE);
					sensitiveDomain.setDomainName(domainName);
					sensitiveDomain.setRegistrationDate(DateUtils.parseYYYY_MM_DDDate(lastResolved));
					sensitiveDomain.setSensitiveWordName("IP反查");
					sensitiveDomain.setSensitiveWord(threatActor.getIpAddress());
					sensitiveDomain.setLcsLength(domainCount);

					sensitiveDomainService.createSensitiveDomain(sensitiveDomain);
					count++;
				}
			}

			// Email反查
			if (StringUtils.isNotBlank(threatActor.getRegistrantEmail())) {
				Map<String, Object> jsonMap = reverseQueryByEmail(threatActor, httpclient, appkey);
				if (MapUtils.isEmpty(jsonMap)) {
					continue;
				}

				@SuppressWarnings("unchecked")
				Map<String, Object> responseMap = (Map<String, Object>) jsonMap.get("response");
				if (MapUtils.isEmpty(responseMap)) {
					continue;
				}

				int resultCount = MapUtils.getInteger(responseMap, "result_count", 0);
				if (resultCount <= 0) {
					continue;
				}

				int totalPages = MapUtils.getIntValue(responseMap, "total_pages", 0);
				if (totalPages != 1) {
					logger.warn("totalPages={}, currentPage={}", totalPages,
							MapUtils.getIntValue(responseMap, "current_page", 0));
				}

				@SuppressWarnings("unchecked")
				List<Map<String, String>> matches = (List<Map<String, String>>) responseMap.get("matches");
				for (Map<String, String> match : matches) {
					String domainName = match.get("domain");
					domainName = domainName.toLowerCase();
					String createdDate = match.get("created_date");
					logger.debug("domain={}, createdDate={}", domainName, createdDate);

					if (sensitiveDomainSet.contains(domainName)) {
						continue;
					}
					sensitiveDomainSet.add(domainName);

					SensitiveDomain sensitiveDomain = new SensitiveDomain();
					sensitiveDomain.setCategory(SENSITIVE_DOMAIN_CATEGORY_EMAIL_REVERSE);
					sensitiveDomain.setDomainName(domainName);
					sensitiveDomain.setRegistrationDate(DateUtils.parseYYYY_MM_DDDate(createdDate));
					sensitiveDomain.setSensitiveWordName("EMAIL反查");
					sensitiveDomain.setSensitiveWord(threatActor.getRegistrantEmail());
					sensitiveDomain.setLcsLength(resultCount);

					sensitiveDomainService.createSensitiveDomain(sensitiveDomain);
					count++;
				}
			}
		}
		return count;
	}

	private Map<String, Object> reverseQueryByIp(ThreatActor threatActor, CloseableHttpClient httpclient,
			String appkey) {
		Map<String, Object> jsonMap = Maps.newHashMap();
		// jsonMap = JsonHelper.deserialize(IP_REVERSE_RESP, new
		// TypeReference<HashMap<String, Object>>() {
		// });
		// if (jsonMap != null) {
		// return jsonMap;
		// }

		String url = "https://api.viewdns.info/reverseip/?host=" + threatActor.getIpAddress() + "&apikey=" + appkey
				+ "&output=json";
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
				HttpEntity entity = response.getEntity();
				jsonMap = JsonHelper.deserialize(entity.getContent(), new TypeReference<HashMap<String, Object>>() {
				});
			} else {
				logger.warn("ip {} reverse failed, status code={}. ", threatActor.getIpAddress(),
						statusLine.getStatusCode());
			}

		} catch (Exception e) {
			logger.warn("ip " + threatActor.getIpAddress() + " reverse faile. ", e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
				}
			}
		}
		return jsonMap;
	}

	private Map<String, Object> reverseQueryByEmail(ThreatActor threatActor, CloseableHttpClient httpclient,
			String appkey) {
		Map<String, Object> jsonMap = Maps.newHashMap();
		// jsonMap = JsonHelper.deserialize(IP_REVERSE_RESP, new
		// TypeReference<HashMap<String, Object>>() {
		// });
		// if (jsonMap != null) {
		// return jsonMap;
		// }

		String url = "https://api.viewdns.info/reversewhois/?q=" + threatActor.getRegistrantEmail() + "&apikey="
				+ appkey + "&output=json";

		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
				HttpEntity entity = response.getEntity();
				jsonMap = JsonHelper.deserialize(entity.getContent(), new TypeReference<HashMap<String, Object>>() {
				});
			} else {
				logger.warn("email {} reverse failed, status code={}. ", threatActor.getRegistrantEmail(),
						statusLine.getStatusCode());
			}

		} catch (Exception e) {
			logger.warn("email " + threatActor.getIpAddress() + " reverse faile. ", e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
				}
			}
		}
		return jsonMap;
	}

}
