package com.x.apa.phishtankurl.task;

import java.io.IOException;
import java.io.Reader;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.x.apa.common.Constant;
import com.x.apa.common.queue.SuspectDomainQueue;
import com.x.apa.common.util.HotPropertiesAccessor;
import com.x.apa.common.util.JsonHelper;
import com.x.apa.phishtankurl.data.PhishtankSuspectMatch;
import com.x.apa.phishtankurl.data.PhishtankUrl;
import com.x.apa.phishtankurl.service.PhishtankUrlService;

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
public class PhishUrlLoadTask implements Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SuspectDomainQueue domainCrawlingQueue;

	@Autowired
	private PhishtankUrlService phishtankUrlService;

	// 每隔20分钟同步一次
	@Scheduled(cron = "0 0/20 * * * ?")
	public void load() {

		// 数据获取地址
		String dataFetchUrl = HotPropertiesAccessor.getProperty("phishtank.data.fetch.url");
		if (StringUtils.isBlank(dataFetchUrl)) {
			logger.info("load phishtank online valid data escape.");
			return;
		}

		Set<Reader> onlineValidDataSet = Sets.newHashSet();

		logger.info("load phishtank online valid data from {} start.", dataFetchUrl);

		// 抓取已确认在线的钓鱼URL
		fetchOnlineValidData(dataFetchUrl, onlineValidDataSet);

		// 解析已确认在线钓鱼URL
		if (CollectionUtils.isNotEmpty(onlineValidDataSet)) {
			// 查询已处理的时间戳
			String latestVerificationTime = phishtankUrlService.queryLatestVerificationTime();
			parsePhishtankDataCsv(onlineValidDataSet.iterator().next(), latestVerificationTime);
		} else {

			logger.info("load phishtank online valid data from {} failed.", dataFetchUrl);
		}

		logger.info("load phishtank online valid data from {} end.", dataFetchUrl);

	}

	private void fetchOnlineValidData(String dataFetchUrl, Set<Reader> onlineValidDataSet) {
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
			controller.addSeed(dataFetchUrl);

			// 启动爬虫，阻塞直到爬取完成
			controller.setCustomData(onlineValidDataSet);
			controller.start(PhishUrlCrawler.class, 1);

			// 爬取完毕
			logger.debug("crawling phishtank online valid data finished for url {}.", dataFetchUrl);

		} catch (Exception e) {
			logger.warn("crawling phishtank online valid data faield for url " + dataFetchUrl + ".", e);
		}
	}

	private void parsePhishtankDataCsv(Reader in, String latestVerificationTime) {
		try {
			Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);

			logger.info("load phishtank online valid data line.");

			int maxLength = 0;
			for (CSVRecord record : records) {

				// 判断确认时间是否在最近确认时间之后，如果已经处理过则不处理
				String verificationTime = parsePuttyTime(record.get("verification_time"));
				if (latestVerificationTime.compareTo(verificationTime) >= 0) {
					continue;
				}

				// 记录最大URL长度
				if (record.get("url").length() > maxLength) {
					maxLength = record.get("url").length();
				}

				// 原始记录构造成json
				TreeMap<String, String> sortedColumnMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
				sortedColumnMap.putAll(record.toMap());
				String recordJson = JsonHelper.serialize(sortedColumnMap);
				// logger.debug("read line: {} ", recordJson);

				PhishtankUrl phishtankValidUrl = new PhishtankUrl();
				phishtankValidUrl.setPhishId(record.get("phish_id"));
				phishtankValidUrl.setUrl(record.get("url"));
				phishtankValidUrl.setPhishDetailUrl(record.get("phish_detail_url"));
				phishtankValidUrl.setSubmissionTime(parsePuttyTime(record.get("submission_time")));
				phishtankValidUrl.setVerified(record.get("verified"));
				phishtankValidUrl.setVerificationTime(verificationTime);
				phishtankValidUrl.setOnline(record.get("online"));
				phishtankValidUrl.setTarget(record.get("target"));
				phishtankValidUrl.setRecordJson(recordJson);

				// 写入数据库
				phishtankValidUrl = phishtankUrlService.createPhishtankUrl(phishtankValidUrl);

				// 钓鱼URL放入爬虫队列
				PhishtankSuspectMatch phishtankMatch = new PhishtankSuspectMatch();
				phishtankMatch.setId(phishtankValidUrl.getId());
				phishtankMatch.setDomainName(phishtankValidUrl.getUrl());
				phishtankMatch.setRawDomainId(phishtankValidUrl.getId());

				phishtankMatch.setSuspectMatch(SUSPECT_DOMAIN_MATCH_PHISHTANK);
				phishtankMatch.setSuspectMatchObjId("");
				phishtankMatch.setSuspectMatchObjText("");

				domainCrawlingQueue.put(phishtankMatch);

			}

			logger.info("load phishtank online valid data finished, the max url length is {}.", maxLength);
		} catch (IOException e) {
			logger.warn("load phishtank online valid data failed.", e);
		}
	}

	private String parsePuttyTime(String timeString) {
		String puttyTime = timeString.replace("T", " ");
		if (puttyTime.contains("+")) {
			puttyTime = puttyTime.substring(0, puttyTime.lastIndexOf("+"));
		}
		return puttyTime;
	}
}
