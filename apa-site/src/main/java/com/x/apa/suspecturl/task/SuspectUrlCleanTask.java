package com.x.apa.suspecturl.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.x.apa.common.Constant;
import com.x.apa.common.util.DateUtils;
import com.x.apa.suspecturl.service.ClueUrlService;
import com.x.apa.suspecturl.service.SuspectUrlService;

/**
 * @author liumeng
 */
@Component
public class SuspectUrlCleanTask implements Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ClueUrlService clueUrlService;

	@Autowired
	private SuspectUrlService suspectUrlService;

	@Scheduled(cron = "0 0 2 * * ? ")
	public void cleanData() {

		logger.info("start suspect url clean task...");

		Date expireDate = DateUtils.beforeDayDate(new Date(), 3);

		int count = clueUrlService.deleteClueUrlBeforeDate(expireDate);
		logger.info("delete {} from clue url before {}.", count, expireDate);

		count = suspectUrlService.deleteSuspectUrlBeforeDate(expireDate);
		logger.info("delete {} from suspectUrlService url before {}.", count, expireDate);

		logger.info("end suspect url clean task...");
	}

}
