package com.x.apa.phishtankurl.task;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang.ArrayUtils;

import com.x.apa.common.Constant;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author liumeng
 */
public class PhishUrlCrawler extends WebCrawler implements Constant {

	private static final Pattern CSV_GZ_EXTENSIONS = Pattern.compile(".*\\.csv.gz$");

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#shouldVisit(edu.uci.ics.crawler4j.crawler.Page,
	 *      edu.uci.ics.crawler4j.url.WebURL)
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		if (href.contains("?")) {
			href = href.substring(0, href.lastIndexOf("?"));
		}

		if (!CSV_GZ_EXTENSIONS.matcher(href).matches()) {
			return false;
		}

		return true;
	}

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#visit(edu.uci.ics.crawler4j.crawler.Page)
	 */
	@Override
	public void visit(Page page) {

		String url = page.getWebURL().getURL();

		logger.debug("crawling data {} from {} success.", url, page.getWebURL().getParentUrl());

		// 判断是否是csv文件
		if (url.contains("?")) {
			url = url.substring(0, url.lastIndexOf("?"));
		}
		if (!CSV_GZ_EXTENSIONS.matcher(url).matches() || !(page.getParseData() instanceof BinaryParseData)) {
			return;
		}
		if (ArrayUtils.isEmpty(page.getContentData())) {
			return;
		}

		StringBuilder builder = new StringBuilder();
		GZIPInputStream gzin = null;
		try {
			gzin = new GZIPInputStream(new ByteArrayInputStream(page.getContentData()));

			int num;
			byte[] buf = new byte[1024];
			while ((num = gzin.read(buf, 0, buf.length)) != -1) {
				builder.append(new String(buf, 0, num));
			}
		} catch (IOException e) {
			logger.warn("unzip " + url + " failed.", e);
		} finally {
			if (gzin != null) {
				try {
					gzin.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}
		Reader reader = new StringReader(builder.toString());

		@SuppressWarnings("unchecked")
		Set<Reader> onlineValidDataSet = (Set<Reader>) getMyController().getCustomData();
		onlineValidDataSet.add(reader);
	}

}
