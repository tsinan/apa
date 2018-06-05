package com.x.apa.inspecturl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.apa.common.Constant;
import com.x.apa.common.crawler.PageFetcherHtmlOnly;
import com.x.apa.common.util.HotPropertiesAccessor;
import com.x.apa.inspecturl.data.InspectUrl;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * @author liumeng
 */
public class InspectUrlController implements Runnable, Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private InspectUrlHandler handler;

	public InspectUrlController(InspectUrlHandler handler) {
		this.handler = handler;
	}

	@Override
	public void run() {

		// 存储爬回数据的文件夹
		String crawlStorageFolder = HotPropertiesAccessor.getProperty("common.crawler.storage.folder");
		crawlStorageFolder += ("_" + Thread.currentThread().getId());

		// 爬虫使用的UA
		String userAgent = HotPropertiesAccessor.getProperty("common.crawler.ua.setting");
		int socketTimeout = Integer.parseInt(HotPropertiesAccessor.getProperty("common.crawler.timeout.socket"));
		int connectionTimeout = Integer
				.parseInt(HotPropertiesAccessor.getProperty("common.crawler.timeout.connection"));
		int politenessDelay = Integer
				.parseInt(HotPropertiesAccessor.getProperty("pu.inspect.crawler.politeness.delay"));

		CrawlConfig config = new CrawlConfig();
		config.setUserAgentString(userAgent);
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setPolitenessDelay(politenessDelay); // 同一网站两个请求间隔200毫秒
		config.setMaxDepthOfCrawling(1); // 最多爬一层
		config.setMaxPagesToFetch(-1); // 最多爬多少页面
		config.setIncludeBinaryContentInCrawling(false);
		config.setResumableCrawling(false);
		config.setThreadMonitoringDelaySeconds(1);
		config.setThreadShutdownDelaySeconds(1);
		config.setCleanupDelaySeconds(1);
		config.setSocketTimeout(socketTimeout); // 5秒socket超时
		config.setConnectionTimeout(connectionTimeout); // 5秒连接超时

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcherHtmlOnly(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		robotstxtConfig.setEnabled(false);

		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller;
		InspectUrl inspectUrl = handler.getInspectUrl();
		try {
			controller = new CrawlController(config, pageFetcher, robotstxtServer);
			controller.addSeed(inspectUrl.getUrl());

			// 启动爬虫，阻塞直到爬取完成
			controller.setCustomData(handler);
			controller.start(InspectUrlCrawler.class, 1); // 爬虫并发执行线程数(1个就可以，因为在爬虫内部每个线程处理50个页面，我们页面很少的时候不需要多个线程并发)

			// 爬取完毕
			logger.debug("crawling inspect finished for url {}.", inspectUrl);

		} catch (Exception e) {
			logger.warn("crawling inspect faield for url " + inspectUrl + ".", e);
		}

		// 爬取完成后检查内容是否变化
		handler.getInspectUrlService().updateInspectUrlStatus(inspectUrl, handler.getStatusCode(), handler.getHeaders(),
				handler.getHtmlDataList());
		return;
	}

}
