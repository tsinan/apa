package com.x.apa.loader.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.loader.service.SensitiveDomainProducer;

/**
 * 
 * @author liumeng
 */
@Component
public class SensitiveDomainProduceDailyTask implements Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SensitiveDomainProducer sensitiveDomainProducerDetectImpl;

	@Autowired
	private SensitiveDomainProducer sensitiveDomainProducerReverseImpl;

	// 8点1分1秒开始
	@Scheduled(cron = "1 1 8 * * ?")
	// @Scheduled(initialDelay = 3 * 1000, fixedDelay = 3600 * 1000)
	public void produce() {

		logger.info("produce sensitive domain daily start...");

		Date current = new Date();

		// TA反查添加敏感域名
		int count = sensitiveDomainProducerReverseImpl.produce(current);
		logger.info("produce {} sensitive domain daily from reverse query end...", count);

		// 前一天原始域名识别敏感域名
		count = sensitiveDomainProducerDetectImpl.produce(current);
		logger.info("produce {} sensitive domain daily from domain detect end...", count);
	}

}
