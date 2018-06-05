package com.x.apa.loader.task;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.x.apa.common.Constant;
import com.x.apa.common.data.RawDomain;
import com.x.apa.common.queue.RawDomainQueue;
import com.x.apa.common.util.DateUtils;
import com.x.apa.common.util.HotPropertiesAccessor;
import com.x.apa.inspecturl.service.InspectUrlService;
import com.x.apa.loader.data.SensitiveDomain;
import com.x.apa.loader.service.RawDomainService;
import com.x.apa.loader.service.SensitiveDomainService;

/**
 * 
 * @author liumeng
 */
@Component
public class NewlyDomainLoadTask implements Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RawDomainService rawDomainService;

	@Autowired
	private SensitiveDomainService sensitiveDomainService;

	@Autowired
	private InspectUrlService inspectUrlService;

	@Autowired
	private RawDomainQueue rawDomainQueue;

	private Map<String, Long> duplicatedFileMap = Maps.newHashMap();

	@Scheduled(initialDelay = 3 * 1000, fixedDelay = 5 * 1000)
	public void load() {

		// 获取扫描根路径
		String rootPath = HotPropertiesAccessor.getProperty("ld.file.folder");
		File rootPathFile = FileUtils.getFile(rootPath);
		List<File> domainFiles = Lists.newArrayList(FileUtils.listFiles(rootPathFile, new String[] { "txt" }, false));
		Collections.sort(domainFiles, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		// 如果没有文件则返回
		if (domainFiles.size() > 0) {
			logger.info("load {} domain files from {}.", domainFiles.size(), rootPath);
		} else {
			return;
		}

		// 逐个文件处理
		int count = 0;
		for (File domainFile : domainFiles) {

			// 判断文件名字和最近更新时间，如果重名且时间一致则不处理
			Long lastModifyTime = duplicatedFileMap.get(domainFile.getName());
			if (lastModifyTime == null) {
				lastModifyTime = domainFile.lastModified();
				duplicatedFileMap.put(domainFile.getName(), lastModifyTime);
			} else {
				if (domainFile.lastModified() == lastModifyTime.longValue()) {
					continue;
				}
			}

			// 解析域名文件
			count = parseDomainTxt(domainFile);

			// 处理后是否删除
			if (BooleanUtils.toBoolean(HotPropertiesAccessor.getProperty("ld.file.deleteAfterLoad"))) {
				FileUtils.deleteQuietly(domainFile);
			}
		}
		logger.info("load {} domains from {} files end.", count, domainFiles.size());

		// 读取敏感域名列表
		logger.info("load sensitive domain start...");

		boolean sensitiveLoad = BooleanUtils.toBoolean(HotPropertiesAccessor.getProperty("ld.sensitive.load"));
		if (!sensitiveLoad) {
			logger.info("load sensitive domain escape...");
			return;
		}

		// 查询巡检URL，如果敏感域名已经被巡检，就不要再送去爬虫了
		Set<String> inspectUrlDomainSet = inspectUrlService.queryInspectUrlDomains();

		// 开始处理敏感域名
		int inspectAlready = 0;
		List<SensitiveDomain> sensitiveDomainList = sensitiveDomainService.querySensitiveDomains();
		for (SensitiveDomain sensitiveDomain : sensitiveDomainList) {

			// 如果敏感域名在巡检域名中，则跳过
			if (inspectUrlDomainSet.contains(sensitiveDomain.getDomainName())) {
				inspectAlready++;
				continue;
			}

			RawDomain rawDomain = new RawDomain();
			rawDomain.setId(sensitiveDomain.getId());
			rawDomain.setDomainName(sensitiveDomain.getDomainName());
			rawDomain.setRegistrationDate(sensitiveDomain.getRegistrationDate());
			rawDomainQueue.put(rawDomain);
		}

		logger.info("load {} sensitive domain end, {} domains already in inpsect.", sensitiveDomainList.size(),
				inspectAlready);
	}

	private int parseDomainTxt(File domainFile) {
		int count = 0;

		logger.info("load line from {}.", domainFile.getName());

		// 默认取处理时间的前一天
		Date registrationDate = DateUtils.beforeDayDate(new Date(), 1);
		String fileName = domainFile.getName();
		if (fileName.contains("20")) {
			String guessDate = fileName.substring(fileName.indexOf("20"), fileName.indexOf("20") + 10);
			try {
				registrationDate = DateUtils.parseYYYY_MM_DDDate(guessDate);
			} catch (Exception e) {
				logger.warn("guess registration date from filename failed.");
			}
		}

		List<String> domains = Lists.newArrayList();
		try {
			domains = FileUtils.readLines(domainFile, "utf8");
		} catch (IOException e) {
			logger.warn("load domain file " + domainFile.getName() + " failed.", e);
			return count;
		}
		for (String domain : domains) {

			if (StringUtils.isBlank(domain)) {
				continue;
			}

			// 个别域名没有.，排除这部分域名
			if (!domain.contains(".")) {
				continue;
			}

			RawDomain rawDomain = new RawDomain();
			rawDomain.setDomainName(domain.toLowerCase());
			rawDomain.setRegistrationDate(registrationDate);
			rawDomain.setIsSensitive(DOMAIN_COMMON);
			rawDomain.setRegistrantName("");
			rawDomain.setRegistrantEmail("");
			rawDomain.setRegistrantPhone("");
			rawDomain.setDomainRegistrarId("");
			rawDomain.setRecordJson(domain);

			// 写入数据库
			rawDomainService.createRawDomain(rawDomain);

			// 原始记录发送到raw record队列
			rawDomainQueue.put(rawDomain);

			count++;
		}

		logger.info("load domain file {} finished.", domainFile.getName());
		return count;
	}

}
