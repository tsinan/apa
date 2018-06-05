package com.x.apa.loader.task;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.lang.BooleanUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.common.util.DateUtils;
import com.x.apa.common.util.HotPropertiesAccessor;
import com.x.apa.loader.service.RawDomainService;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * 
 * @author liumeng
 */
@Component
public class NewlyDomainDownloadTask implements Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RawDomainService rawDomainService;

	// 每小时同步一次
	@Scheduled(cron = "0 1/59 * * * ?")
	// @Scheduled(cron = "0 0/1 * * * ?")
	public void load() {

		logger.info("download newly domain files start...");

		boolean crawling = BooleanUtils.toBoolean(HotPropertiesAccessor.getProperty("ld.file.crawling"));
		if (!crawling) {
			logger.info("download newly domain files escape...");
			return;
		}

		// 计算当天零点时间
		Date newRegistrationDate = DateUtils
				.parseYYYY_MM_DDDate(DateUtils.toString_YYYY_MM_DD(DateUtils.beforeDayDate(new Date(), 1)));
		// 检查数据库中最新注册时间
		Date latestRegistrationDate = rawDomainService.queryLatestRegistrationDate();
		if (!newRegistrationDate.after(latestRegistrationDate)) {
			logger.debug("download newly domain files skip, becuase {} already loaded.", newRegistrationDate);
			return;
		}

		// 需要下载的注册日期
		String registrationDateText = DateUtils.toString_YYYY_MM_DD(newRegistrationDate);

		// 文件获取地址
		String fileFetchUrl = HotPropertiesAccessor.getProperty("ld.file.download.url");
		if (StringUtils.isBlank(fileFetchUrl)) {
			logger.debug("download newly domain files skip, becuase no download url setting.");
			return;
		}
		fileFetchUrl = MessageFormat.format(fileFetchUrl, registrationDateText);
		String unzipFileName = HotPropertiesAccessor.getProperty("ld.file.folder") + registrationDateText + ".txt";

		logger.info("download newly domain files from {} to {} start.", fileFetchUrl, unzipFileName);

		// 下载每天更新的新域名文件
		downloadDomainFile(fileFetchUrl, unzipFileName);

		logger.info("download newly domain files from {} to {} end.", fileFetchUrl, unzipFileName);
	}

	private void downloadDomainFile(String fileFetchUrl, String unzipFileName) {
		// 存储爬回数据的文件夹
		String crawlStorageFolder = HotPropertiesAccessor.getProperty("common.crawler.storage.folder");
		crawlStorageFolder += ("_" + Thread.currentThread().getId());

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setPolitenessDelay(100);
		config.setMaxDepthOfCrawling(0);
		config.setMaxPagesToFetch(-1);
		config.setIncludeBinaryContentInCrawling(true);
		config.setResumableCrawling(false);
		config.setThreadMonitoringDelaySeconds(1);
		config.setThreadShutdownDelaySeconds(1);
		config.setCleanupDelaySeconds(1);
		config.setMaxDownloadSize(config.getMaxDownloadSize() * 10);

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		robotstxtConfig.setEnabled(false);

		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller;
		try {
			controller = new CrawlController(config, pageFetcher, robotstxtServer);
			controller.addSeed(fileFetchUrl);

			// 启动爬虫，阻塞直到爬取完成
			controller.setCustomData(unzipFileName);
			controller.start(NewlyDomainCrawler.class, 1);

			// 爬取完毕
			logger.debug("crawling newly domain file finished from url {}.", fileFetchUrl);

		} catch (Exception e) {
			logger.warn("crawling newly domain file faield from url " + fileFetchUrl + ".", e);
		}
	}

}
