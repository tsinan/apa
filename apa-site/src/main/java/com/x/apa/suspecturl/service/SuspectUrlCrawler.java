package com.x.apa.suspecturl.service;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.plexus.util.StringUtils;

import com.google.common.collect.Lists;
import com.x.apa.common.Constant;
import com.x.apa.common.metric.MetricCenter;
import com.x.apa.common.util.HotPropertiesAccessor;
import com.x.apa.suspecturl.data.ClueUrl;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author liumeng
 */
public class SuspectUrlCrawler extends WebCrawler implements Constant {

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
		SuspectUrlHandler handler = (SuspectUrlHandler) getMyController().getCustomData();
		ClueUrl clueUrl = handler.getClueUrl();

		// 如果是重定向，返回true
		if (referringPage.isRedirect()) {
			// 检查是否在域名白名单中
			if (handler.getTrustDomainService().isTrustDomain(url.getDomain())) {
				MetricCenter.countTrustDomainVisit(url.getDomain());
				return false;
			}
			// 重定向次数超过阈值，就不要继续重定向了
			if (clueUrl.getRedirectNum() > maxRedirectNum) {
				return false;
			}
			clueUrl.setRedirectNum(clueUrl.getRedirectNum() + 1);
			return true;
		}

		// 先计算外链和图片数量，再判断是否进入
		if (clueUrl.getPageOutlinkNum() == 0) { // 只有外链页面会进入shouldVisit方法，如果为0只能说明是第一个外链页面
			// 记录此页面来源页的外链数量
			Set<WebURL> outlinkSet = referringPage.getParseData().getOutgoingUrls();
			int outlinkNum = 0;
			int outsitelinkNum = 0;
			int trustlinkNum = 0;
			int imageNum = 0;
			for (WebURL outlink : outlinkSet) {

				if (StringUtils.equalsIgnoreCase(outlink.getTag(), "img")) {
					imageNum++;
					continue;
				} else {
					if (!StringUtils.equalsIgnoreCase(outlink.getDomain(), referringPage.getWebURL().getDomain())) {
						outsitelinkNum++;
					}
					if (handler.getTrustDomainService().isTrustDomain(url.getDomain())) {
						trustlinkNum++;
					}
					outlinkNum++;
					continue;
				}
			}
			clueUrl.setPageOutlinkNum(outlinkNum);
			clueUrl.setPageOutsitelinkNum(outsitelinkNum);
			clueUrl.setPageTrustlinkNum(trustlinkNum);
			clueUrl.setPageImageNum(imageNum);
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
		if (clueUrl.getPageOutlinkNum() > maxOutlinkNum || clueUrl.getPageImageNum() > maxImageNum) {
			return false;
		}

		clueUrl.setPageShouldVisitNum(clueUrl.getPageShouldVisitNum() + 1);
		return true;
	}

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#visit(edu.uci.ics.crawler4j.crawler.Page)
	 */
	@Override
	public void visit(Page page) {
		logger.debug("crawling url {} from {} success.", page.getWebURL().getURL(), page.getWebURL().getParentUrl());

		// 爬到内容的URL记录爬到的次数
		SuspectUrlHandler handler = (SuspectUrlHandler) getMyController().getCustomData();
		ClueUrl clueUrl = handler.getClueUrl();

		// 记录爬到页面的内容和统计信息
		if (page.getParseData() instanceof HtmlParseData) {
			MetricCenter.countUrlPageVisit(page.getWebURL().getURL());

			clueUrl.setPageVisitNum(clueUrl.getPageVisitNum() + 1);
			clueUrl.setPageVisitSize(clueUrl.getPageVisitSize() + ArrayUtils.getLength(page.getContentData()));

			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			text = text.replaceAll("[^\\u4e00-\\u9fa5\\n\\r]", ""); // 去掉非汉字和空格
			text = text.replaceAll("[\\r\\n]", " ");// 换行替换为空格

			if (clueUrl.getHtmlDataList() == null) {
				// 抓到HTML内容标记进展
				clueUrl.setProgress(CLUE_URL_PROGRESS_OK_HTML);

				// 直接进入的页面或者302跳转后第一次进入的页面
				List<HtmlParseData> htmlDataList = Lists.newArrayList();
				htmlDataList.add(htmlParseData);
				clueUrl.setHtmlDataList(htmlDataList);
			} else {
				// clueUrl.setPageText(clueUrl.getPageText() + "\n" + text);
				clueUrl.getHtmlDataList().add(htmlParseData);
			}

		} else {
			String url = page.getWebURL().getURL();
			int contentSize = ArrayUtils.getLength(page.getContentData());

			MetricCenter.countUrlNohtmlVisit(url);
			MetricCenter.countUrlNohtmlVisitSize(url, contentSize);

			clueUrl.setNohtmlVisitNum(clueUrl.getNohtmlVisitNum() + 1);
			clueUrl.setNohtmlVisitSize(clueUrl.getNohtmlVisitSize() + contentSize);

			// 第一次进入就不是HTML，标记为非HTML OK
			if (clueUrl.getHtmlDataList() == null) {
				clueUrl.setProgress(CLUE_URL_PROGRESS_OK_OTHER);
			}
		}

	}

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#onBeforeExit()
	 */
	@Override
	public void onBeforeExit() {
		SuspectUrlHandler handler = (SuspectUrlHandler) getMyController().getCustomData();
		ClueUrl clueUrl = handler.getClueUrl();
		if (clueUrl.getProgress() == CLUE_URL_PROGRESS_INIT) {

			if (clueUrl.getPageTrustlinkNum() > 0 || clueUrl.getRedirectNum() > 0) {
				clueUrl.setProgress(CLUE_URL_PROGRESS_GIVEUP);
			} else {
				clueUrl.setProgress(CLUE_URL_PROGRESS_UNKNOWN);
			}
		}
	}

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#onUnexpectedStatusCode(java.lang.String,
	 *      int, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onUnexpectedStatusCode(String urlStr, int statusCode, String contentType, String description) {
		logger.debug("crawling url {} and response status {} / {}.", urlStr, statusCode, description);
		onCrawlingFailed();
	}

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#onPageBiggerThanMaxSize(java.lang.String,
	 *      long)
	 */
	@Override
	protected void onPageBiggerThanMaxSize(String urlStr, long pageSize) {
		logger.debug("crawling url {} which was bigger ( {} ) than max allowed size.", urlStr, pageSize);
		onCrawlingFailed();
	}

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#onParseError(edu.uci.ics.crawler4j.url.WebURL)
	 */
	@Override
	protected void onParseError(WebURL webUrl) {
		logger.debug("crawling url {} parsing error.", webUrl.getURL());
		onCrawlingFailed();
	}

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#onContentFetchError(edu.uci.ics.crawler4j.url.WebURL)
	 */
	@Override
	protected void onContentFetchError(WebURL webUrl) {
		logger.debug("crawling url {} fetch content failed.", webUrl.getURL());
		onCrawlingFailed();
	}

	/**
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#onUnhandledException(edu.uci.ics.crawler4j.url.WebURL,
	 *      java.lang.Throwable)
	 */
	@Override
	protected void onUnhandledException(WebURL webUrl, Throwable e) {
		String urlStr = (webUrl == null ? "NULL" : webUrl.getURL());
		logger.debug("crawling url {} unhandled exception{}.", urlStr, e.getMessage());
		logger.debug("Stacktrace: ", e);
		onCrawlingFailed();
	}

	private void onCrawlingFailed() {
		SuspectUrlHandler handler = (SuspectUrlHandler) getMyController().getCustomData();
		ClueUrl clueUrl = handler.getClueUrl();
		clueUrl.setProgress(CLUE_URL_PROGRESS_ERR);
	}
}
