package com.x.apa.suspecturl.task;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.common.data.SuspectDomain;
import com.x.apa.common.queue.SuspectDomainQueue;
import com.x.apa.suspecturl.data.ClueUrl;
import com.x.apa.suspecturl.service.ClueUrlService;
import com.x.apa.suspecturl.service.SuspectUrlController;
import com.x.apa.suspecturl.service.SuspectUrlHandler;
import com.x.apa.suspecturl.service.SuspectUrlService;
import com.x.apa.suspecturl.service.TrustDomainService;

/**
 * @author liumeng
 */
@Component
public class SuspectUrlTask implements Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SuspectDomainQueue suspectDomainQueue;

	@Autowired
	private ClueUrlService clueUrlService;

	@Autowired
	private SuspectUrlService suspectUrlService;

	@Autowired
	private TrustDomainService trustDomainService;

	@Value("${su.crawling.thread.number}")
	private int threadNumber;

	private ThreadPoolExecutor executor;

	@PostConstruct
	public void init() {
		executor = new ThreadPoolExecutor(threadNumber, threadNumber, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>());

		new Thread(new Runnable() {
			@Override
			public void run() {
				doProcess();
			}
		}).start();
	}

	public void doProcess() {

		logger.info("start suspect url process task...");

		SuspectDomain suspectDomain = null;
		boolean goon = true;
		while (goon && (suspectDomain = suspectDomainQueue.take()) != null) {

			// 限速，如果当前未调度的任务超过1000个，就挂起等待3秒
			long count = executor.getQueue().size();
			long activeCount = executor.getActiveCount();
			if (count >= threadNumber * 10 && activeCount == threadNumber) {
				try {
					logger.info("number of waiting task is {}/{}, task need sleeping.", count, activeCount);
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// do nothing
				}
				logger.info("task awake, number of waiting task is {}/{}.", executor.getTaskCount(),
						executor.getActiveCount());
			}

			// 取域名
			String domainName = suspectDomain.getDomainName();
			logger.debug("suspect domain {} process start.", domainName);

			// 构造线索URL
			List<ClueUrl> clueUrls = clueUrlService.createClueUrls(suspectDomain);

			// 爬虫处理
			for (ClueUrl clueUrl : clueUrls) {
				executor.execute(new SuspectUrlController(
						new SuspectUrlHandler(clueUrl, clueUrlService, suspectUrlService, trustDomainService)));
			}

			logger.debug("suspect domain {} process finished.", domainName);
		}

		logger.info("end suspect url process task...");
	}

}
