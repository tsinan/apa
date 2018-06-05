package com.x.apa.loader.task;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.common.util.HotPropertiesAccessor;
import com.x.apa.loader.service.SensitiveDomainProducer;
import com.x.apa.loader.service.SensitiveDomainService;

/**
 * 
 * @author liumeng
 */
@Component
public class SensitiveDomainProduceTask implements Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SensitiveDomainService sensitiveDomainService;

	@Autowired
	private SensitiveDomainProducer sensitiveDomainProducerDetectImpl;

	@Scheduled(initialDelay = 3 * 1000, fixedDelay = 60 * 1000)
	public void scan() {

		logger.info("produce sensitive domain start...");

		// 开关打开时进行生产
		boolean produce = BooleanUtils.toBoolean(HotPropertiesAccessor.getProperty("ld.sensitive.produce"));
		if (!produce) {
			logger.info("produce sensitvie domain escape...");
			return;
		}

		// 清理上次生产的敏感域名
		sensitiveDomainService.deleteLearningSensitiveDomain();

		// 全量生产敏感域名
		int totalCount = sensitiveDomainProducerDetectImpl.produce(null);

		logger.info("produce {} sensitive domain finished...", totalCount);
	}

}
