package com.x.apa.inspecturl.service;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.plexus.util.StringUtils;

import com.google.common.collect.Lists;
import com.x.apa.common.Constant;
import com.x.apa.common.metric.MetricCenter;
import com.x.apa.common.util.HotPropertiesAccessor;
import com.x.apa.inspecturl.data.InspectUrl;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author liumeng
 */
public class InspectUrlCrawler extends WebCrawler implements Constant {

	private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|ico|gif|jpg|jpeg|png|svg)$");

	private static final Pattern STATIC_EXTENSIONS = Pattern.compile(".*\\.(css|js|ttf|eot|otf|woff|woff2)$");

	private static final Pattern FILE_EXTENSIONS = Pattern.compile(
			".*\\.(log|xml|txt|tar|zip|gzip|rar|doc|docx|ppt|pptx|xls|xlsx|pdf|mp3|mp4|mpg|mpeg|avi|rm|rmvb|mov|wmv|flv|swf)$");

	private final int maxOutlinkNum = Integer
			.parseInt(HotPropertiesAccessor.getProperty("common.crawler.max.outlink.number"));

	private final int maxImageNum = Integer
			.parseInt(HotPropertiesAccessor.getProperty("common.crawler.max.image.number"));

	private final int maxRedirectNum = Integer
			.parseInt(HotPropertiesAccessor.getProperty("common.crawler.max.redirect.number"));

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#shouldVisit(edu.uci.ics.crawler4j.crawler.Page,
	 *      edu.uci.ics.crawler4j.url.WebURL)
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		InspectUrlHandler handler = (InspectUrlHandler) getMyController().getCustomData();
		InspectUrl inspectUrl = handler.getInspectUrl();

		// 如果是重定向，返回true
		if (referringPage.isRedirect()) {

			// 检查是否在域名白名单中
			if (handler.getTrustDomainService().isTrustDomain(url.getDomain())) {
				MetricCenter.countTrustDomainVisit(url.getDomain());
				return false;
			}
			// 重定向次数超过阈值，就不要继续重定向了
			if (inspectUrl.getRedirectNum() > maxRedirectNum) {
				return false;
			}
			inspectUrl.setRedirectNum(inspectUrl.getRedirectNum() + 1);
			return true;
		}

		// 先计算外链和图片数量，再判断是否进入
		if (inspectUrl.getPageOutlinkNum() == 0) { // 只有外链页面会进入shouldVisit方法，如果为0只能说明是第一个外链页面
			// 记录此页面来源页的外链数量
			Set<WebURL> outlinkSet = referringPage.getParseData().getOutgoingUrls();
			if (CollectionUtils.isNotEmpty(outlinkSet)) {
				int outlinkNum = 0;
				int imageNum = 0;
				for (WebURL outlink : outlinkSet) {
					if (StringUtils.equalsIgnoreCase(outlink.getTag(), "img")) {
						imageNum++;
						continue;
					} else {
						outlinkNum++;
						continue;
					}
				}
				inspectUrl.setPageOutlinkNum(outlinkNum);
				inspectUrl.setPageImageNum(imageNum);
			}
		}

		// 如果是图片标签，不抓取
		if (StringUtils.equalsIgnoreCase(url.getTag(), "img")) {
			return false;
		}
		// 忽略图片或js/css后缀的URL
		String href = url.getURL().toLowerCase();
		int paramStartIndex = href.indexOf("?");
		if (paramStartIndex >= 0) {
			href = href.substring(0, paramStartIndex);
		}
		if (IMAGE_EXTENSIONS.matcher(href).matches() || STATIC_EXTENSIONS.matcher(href).matches()
				|| FILE_EXTENSIONS.matcher(href).matches()) {
			return false;
		}

		// 检查是否在域名白名单中
		if (handler.getTrustDomainService().isTrustDomain(url.getDomain())) {
			MetricCenter.countTrustDomainVisit(url.getDomain());
			return false;
		}

		// 判断来源页的外链个数/图片个数，超过阈值个数则不爬
		if (inspectUrl.getPageOutlinkNum() > maxOutlinkNum || inspectUrl.getPageImageNum() > maxImageNum) {
			return false;
		}
		return true;
	}

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#visit(edu.uci.ics.crawler4j.crawler.Page)
	 */
	@Override
	public void visit(Page page) {
		logger.debug("crawling inspect url {} from {} success.", page.getWebURL().getURL(),
				page.getWebURL().getParentUrl());

		InspectUrlHandler handler = (InspectUrlHandler) getMyController().getCustomData();
		// 记录爬到页面的内容
		if (page.getParseData() instanceof HtmlParseData) {

			if (handler.getStatusCode() == 0) {
				// 第一次进入，将statusCode放入
				handler.setStatusCode(page.getStatusCode());
				handler.setHeaders(page.getFetchResponseHeaders());

				// 直接进入的页面或者302跳转后第一次进入的页面
				List<HtmlParseData> htmlDataList = Lists.newLinkedList();
				htmlDataList.add((HtmlParseData) page.getParseData());
				handler.setHtmlDataList(htmlDataList);

			} else {
				handler.getHtmlDataList().add((HtmlParseData) page.getParseData());
			}
		} else {
			if (handler.getStatusCode() == 0) {
				handler.setStatusCode(page.getStatusCode());
			}
		}
	}

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#onBeforeExit()
	 */
	@Override
	public void onBeforeExit() {

	}

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#onUnexpectedStatusCode(java.lang.String,
	 *      int, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onUnexpectedStatusCode(String urlStr, int statusCode, String contentType, String description) {
		logger.debug("crawling inspect url {} and response status {} / {}.", urlStr, statusCode, description);

		InspectUrlHandler handler = (InspectUrlHandler) getMyController().getCustomData();
		// 如果第一次访问就返回错误码，则记录
		if (handler.getStatusCode() == 0) {
			handler.setStatusCode(statusCode);
		}
		onCrawlingFailed();
	}

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#onPageBiggerThanMaxSize(java.lang.String,
	 *      long)
	 */
	@Override
	protected void onPageBiggerThanMaxSize(String urlStr, long pageSize) {
		logger.debug("crawling inspect url {} which was bigger ( {} ) than max allowed size.", urlStr, pageSize);
		onCrawlingFailed();
	}

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#onParseError(edu.uci.ics.crawler4j.url.WebURL)
	 */
	@Override
	protected void onParseError(WebURL webUrl) {
		logger.debug("crawling inspect url {} parsing error.", webUrl.getURL());
		onCrawlingFailed();
	}

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#onContentFetchError(edu.uci.ics.crawler4j.url.WebURL)
	 */
	@Override
	protected void onContentFetchError(WebURL webUrl) {
		logger.debug("crawling inspect url {} fetch content failed.", webUrl.getURL());
		onCrawlingFailed();
	}

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#onUnhandledException(edu.uci.ics.crawler4j.url.WebURL,
	 *      java.lang.Throwable)
	 */
	@Override
	protected void onUnhandledException(WebURL webUrl, Throwable e) {
		String urlStr = (webUrl == null ? "NULL" : webUrl.getURL());
		logger.debug("crawling inspect url {} unhandled exception{}.", urlStr, e.getMessage());
		logger.debug("Stacktrace: ", e);
		onCrawlingFailed();
	}

	private void onCrawlingFailed() {

	}
}
