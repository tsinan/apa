package com.x.apa.common.util;

import edu.uci.ics.crawler4j.url.TLDList;

/**
 * @author liumeng
 */
public class UrlUtils {

	public static String parseDomain(String url) {
		String domain = "";

		// 根据URL计算域名
		url = url.trim();
		int domainStartIdx = url.indexOf("//") + 2;
		int domainEndIdx = url.indexOf('/', domainStartIdx);
		domainEndIdx = (domainEndIdx > domainStartIdx) ? domainEndIdx : url.length();
		domain = url.substring(domainStartIdx, domainEndIdx);

		String[] parts = domain.split("\\.");
		if (parts.length > 2) {
			domain = parts[parts.length - 2] + "." + parts[parts.length - 1];
			if (TLDList.getInstance().contains(domain)) {
				domain = parts[parts.length - 3] + "." + domain;
			}
		}
		return domain;
	}
}
