package com.x.apa.inspecturl.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.common.util.DateUtils;
import com.x.apa.inspecturl.service.InspectEventService;
import com.x.apa.inspecturl.service.InspectTraceService;

/**
 * @author liumeng
 */
@Component
public class InspectUrlCleanTask implements Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private InspectTraceService inspectTraceService;

	@Autowired
	private InspectEventService inspectEventService;

	@Scheduled(cron = "0 0 2 * * ? ")
	public void cleanData() {

		logger.info("start inspect url clean task...");

		Date expireDate = DateUtils.beforeDayDate(new Date(), 3);

		int count = inspectTraceService.deleteInspectTraceBeforeDate(expireDate);
		logger.info("delete {} from inspect trace before {}.", count, expireDate);

		count = inspectEventService.deleteInspectEventBeforeDate(expireDate);
		logger.info("delete {} from inspect event before {}.", count, expireDate);

		logger.info("end inspect url clean task...");
	}

}
