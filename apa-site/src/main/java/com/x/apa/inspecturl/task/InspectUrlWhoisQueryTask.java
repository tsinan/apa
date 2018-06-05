package com.x.apa.inspecturl.task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.x.apa.common.Constant;
import com.x.apa.common.util.HotPropertiesAccessor;
import com.x.apa.common.util.JsonHelper;
import com.x.apa.common.util.UrlUtils;
import com.x.apa.inspecturl.data.InspectUrl;
import com.x.apa.inspecturl.service.InspectUrlService;

/**
 * @author liumeng
 */
@Component
public class InspectUrlWhoisQueryTask implements Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private InspectUrlService inspectUrlService;

	@Scheduled(initialDelay = 3 * 1000, fixedDelay = 3600 * 1000)
	public void queryDomainWhois() {

		logger.info("inspect url whois query task start...");

		// 查询开关
		boolean query = BooleanUtils.toBoolean(HotPropertiesAccessor.getProperty("pu.url.whois.query"));
		if (!query) {
			logger.info("inspect url whois query task escape...");
			return;
		}

		// appkey设置
		String appkey = HotPropertiesAccessor.getProperty("common.whois.api.appkey");
		if (StringUtils.isBlank(appkey)) {
			logger.info("inspect url whois query task escape[no appkey]...");
			return;
		}

		// 查询巡检URL
		List<InspectUrl> inspectUrlList = inspectUrlService.queryInspectUrls(false);
		if (CollectionUtils.isEmpty(inspectUrlList)) {
			logger.info("inspect url whois query task escape[no inspect url]...");
			return;
		}

		logger.info("inspect url whois query task will process {} inspect urls .", inspectUrlList.size());

		int queryCount = 0;
		int updateCount = 0;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		for (InspectUrl inspectUrl : inspectUrlList) {

			if (StringUtils.isBlank(inspectUrl.getUrl())) {
				continue;
			}

			// 如果注册人Email和注册商名称已经填写，则跳过不处理
			if (StringUtils.isNotBlank(inspectUrl.getWhoisRegEmail())
					&& StringUtils.isNotBlank(inspectUrl.getWhoisRegName())) {
				continue;
			}

			String domain = UrlUtils.parseDomain(inspectUrl.getUrl());
			Map<String, Object> jsonMap = queryByDomain(domain, httpclient, appkey);
			queryCount++;
			if (MapUtils.isEmpty(jsonMap)) {
				continue;
			}

			@SuppressWarnings("unchecked")
			Map<String, Object> responseMap = (Map<String, Object>) jsonMap.get("response");
			if (MapUtils.isEmpty(responseMap)) {
				continue;
			}

			@SuppressWarnings("unchecked")
			Map<String, Object> registrationMap = (Map<String, Object>) responseMap.get("registration");
			if (MapUtils.isEmpty(registrationMap)) {
				continue;
			}

			boolean update = false;

			// 提取注册商信息
			String registrar = MapUtils.getString(registrationMap, "registrar", "");
			if (StringUtils.isNotBlank(registrar) && StringUtils.isBlank(inspectUrl.getWhoisRegName())) {
				inspectUrl.setWhoisRegName(registrar);
				update = true;
			}

			// 从rawdata中，提取Registrant Email
			String rawdata = MapUtils.getString(responseMap, "rawdata", "");
			logger.debug("rawdata is : {}", rawdata);
			if (StringUtils.isNotBlank(rawdata)) {
				int index = rawdata.indexOf("Registrant Email");
				if (index >= 0) {

					String registrantEmail = "";
					int startIndex = index + "Registrant Email".length();
					int endIndex = rawdata.indexOf("\n", startIndex);
					if (endIndex < 0) {
						registrantEmail = rawdata.substring(startIndex);
					} else {
						registrantEmail = rawdata.substring(startIndex, endIndex);
					}

					if (StringUtils.isNotBlank(registrantEmail)) {
						registrantEmail = registrantEmail.replace(":", " ");
						registrantEmail = registrantEmail.trim();
						if (StringUtils.isNotBlank(registrantEmail)
								&& StringUtils.isBlank(inspectUrl.getWhoisRegEmail())) {
							inspectUrl.setWhoisRegEmail(registrantEmail);
							update = true;
						}
					}

				}
			}

			if (update) {
				inspectUrlService.updateInpsectUrlWhois(inspectUrl);
				updateCount++;
			}
		}

		logger.info("inspect url whois query task end, query {} times for update {} whois-info.", queryCount,
				updateCount);

	}

	private Map<String, Object> queryByDomain(String domain, CloseableHttpClient httpclient, String appkey) {
		Map<String, Object> jsonMap = Maps.newHashMap();
		// jsonMap = JsonHelper.deserialize(IP_REVERSE_RESP, new
		// TypeReference<HashMap<String, Object>>() {
		// });
		// if (jsonMap != null) {
		// return jsonMap;
		// }

		String url = "https://api.viewdns.info/whois/?domain=" + domain + "&apikey=" + appkey + "&output=json";

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
				logger.warn("domain {} whois query failed, status code={}. ", domain, statusLine.getStatusCode());
			}

		} catch (Exception e) {
			logger.warn("domain " + domain + " whois query faile. ", e);
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
