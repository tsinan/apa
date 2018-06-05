package com.x.apa.suspecturl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.apa.common.Constant;
import com.x.apa.common.crawler.PageFetcherHtmlOnly;
import com.x.apa.common.util.HotPropertiesAccessor;
import com.x.apa.suspecturl.data.ClueUrl;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * @author liumeng
 */
public class SuspectUrlController implements Runnable, Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private SuspectUrlHandler handler;

	public SuspectUrlController(SuspectUrlHandler handler) {
		this.handler = handler;
	}

	@Override
	public void run() {

		// 存储爬回数据的文件夹
		String crawlStorageFolder = HotPropertiesAccessor.getProperty("common.crawler.storage.folder");
		crawlStorageFolder += ("_" + Thread.currentThread().getId());

		// 爬虫使用的UA
		String userAgent = HotPropertiesAccessor.getProperty("common.crawler.ua.setting");

		CrawlConfig config = new CrawlConfig();
		config.setUserAgentString(userAgent);
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setPolitenessDelay(200); // 同一网站两个请求间隔200毫秒
		config.setMaxDepthOfCrawling(1); // 最多爬一层
		config.setMaxPagesToFetch(-1); // 最多爬多少页面
		config.setIncludeBinaryContentInCrawling(false);
		config.setResumableCrawling(false);
		config.setThreadMonitoringDelaySeconds(1);
		config.setThreadShutdownDelaySeconds(1);
		config.setCleanupDelaySeconds(1);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcherHtmlOnly(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		robotstxtConfig.setEnabled(false);

		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller;
		ClueUrl clueUrl = handler.getClueUrl();
		try {
			controller = new CrawlController(config, pageFetcher, robotstxtServer);
			controller.addSeed(clueUrl.getUrl());

			// 启动爬虫，阻塞直到爬取完成
			controller.setCustomData(handler);
			controller.start(SuspectUrlCrawler.class, 1); // 爬虫并发执行线程数(1个就可以，因为在爬虫内部每个线程处理50个页面，我们页面很少的时候不需要多个线程并发)

			// 爬取完毕
			logger.debug("crawling finished for url {}.", clueUrl);

		} catch (Exception e) {
			logger.warn("crawling faield for url " + clueUrl + ".", e);

			clueUrl.setProgress(CLUE_URL_PROGRESS_ERR);
		}

		// 爬虫处理结果保存
		handler.getClueUrlService().updateClueUrlProgress(clueUrl);

		// 爬取成功的发到关键词检索模块
		if (clueUrl.getProgress() == CLUE_URL_PROGRESS_OK_HTML) {
			handler.getSuspectUrlService().discernSuspectUrl(clueUrl);
		}
		return;
	}

}
