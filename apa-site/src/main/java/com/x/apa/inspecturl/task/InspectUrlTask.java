package com.x.apa.inspecturl.task;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.x.apa.common.util.DateUtils;
import com.x.apa.common.util.HotPropertiesAccessor;
import com.x.apa.inspecturl.data.InspectUrl;
import com.x.apa.inspecturl.service.InspectUrlController;
import com.x.apa.inspecturl.service.InspectUrlHandler;
import com.x.apa.inspecturl.service.InspectUrlService;
import com.x.apa.suspecturl.service.TrustDomainService;

/**
 * 
 * @author liumeng
 */
@Component
public class InspectUrlTask {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private InspectUrlService inspectUrlService;

	@Autowired
	private TrustDomainService trustDomainService;

	@Value("${pu.inspect.thread.number}")
	private int threadNumber;

	private ThreadPoolExecutor executor;

	@PostConstruct
	public void init() {
		executor = new ThreadPoolExecutor(threadNumber, threadNumber, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>());

		// 防止死锁，将next_time为null的设置为当前时间
		List<InspectUrl> inspectUrlList = inspectUrlService.queryInspectUrls(true);
		if (CollectionUtils.isEmpty(inspectUrlList)) {
			return;
		}

		logger.info("inspect task dead-lock found, unlock {} task .", inspectUrlList.size());
		for (InspectUrl inspectUrl : inspectUrlList) {
			inspectUrlService.changeInspectUrlLock(inspectUrl.getId(), false);
		}
	}

	@Scheduled(initialDelay = 3 * 1000, fixedDelay = 10 * 1000)
	public void inspect() {
		logger.info("inspect phishing url start...");

		boolean inspect = BooleanUtils.toBoolean(HotPropertiesAccessor.getProperty("pu.inspect"));
		if (!inspect) {
			logger.info("inspect phishing url escape...");
			return;
		}

		Date nowDate = new Date();
		List<InspectUrl> inspectUrlList = inspectUrlService.queryInspectUrlsByNextTime(nowDate);
		if (CollectionUtils.isEmpty(inspectUrlList)) {
			logger.debug("inspect phishing url skip because no phishing url need inspect.");
			return;
		}

		// 线程全忙，本轮跳过
		int activeTaskCount = executor.getActiveCount();
		if (activeTaskCount == threadNumber) {
			logger.debug("inspect phishing url skip because too many task{} activied.", activeTaskCount);
			return;
		}

		logger.info("inspect {} phishing urls start at {}.", inspectUrlList.size(),
				DateUtils.toString_YYYY_MM_DD_HH_MM_SS(nowDate));

		// 爬虫处理
		for (InspectUrl inspectUrl : inspectUrlList) {

			// 检查IP地址是否已在本轮处理过，如果处理过，则下轮再处理
			boolean locked = false;
			try {
				String ipAddress = "";
				URL url = new URL(inspectUrl.getUrl());
				InetAddress[] addressArray = InetAddress.getAllByName(url.getHost());
				List<String> addressList = Lists.newArrayList();
				for (InetAddress address : addressArray) {
					logger.debug("find url {} with ip {} 1/{}.", inspectUrl.getUrl(), address.getHostAddress(),
							addressArray.length);

					addressList.add(address.getHostAddress());
					if (!InspectUrlLatch.tryLock(address.getHostAddress(), url.getHost())) {
						locked = true;
					}
				}
				ipAddress = String.join(",", addressList);
				if (ipAddress.length() > 256) {
					ipAddress = ipAddress.substring(0, 256);
				}
				inspectUrl.setIpAddress(ipAddress);

				if (locked) {
					logger.info("skip url {} because ip {} in shield.", inspectUrl.getUrl(), ipAddress);
					continue;
				} else {
					logger.info("goon url {} with ip {}.", inspectUrl.getUrl(), ipAddress);
				}
			} catch (UnknownHostException | MalformedURLException e) {
				// 域名解析失败，直接判断离线
				inspectUrl.setIpAddress("");
				logger.info("skip url " + inspectUrl.getUrl() + " because unkownhost error.");
				inspectUrlService.updateInspectUrlStatus(inspectUrl, -1, null, null);
				continue;
			}

			// 锁住正在执行的巡检URL，避免重复
			inspectUrlService.changeInspectUrlLock(inspectUrl.getId(), true);

			// 线程池处理
			executor.execute(
					new InspectUrlController(new InspectUrlHandler(inspectUrl, inspectUrlService, trustDomainService)));
		}

		logger.info("inspect {} phishing urls finished.", inspectUrlList.size());
	}
}
