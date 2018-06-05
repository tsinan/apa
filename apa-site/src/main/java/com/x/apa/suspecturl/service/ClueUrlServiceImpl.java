package com.x.apa.suspecturl.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.google.common.collect.Lists;
import com.x.apa.common.Constant;
import com.x.apa.common.data.SuspectDomain;
import com.x.apa.suspecturl.dao.ClueUrlDao;
import com.x.apa.suspecturl.data.ClueUrl;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * @author liumeng
 */
@Component
public class ClueUrlServiceImpl implements ClueUrlService, Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	@Autowired
	private TemplateLoader templateLoader;

	@Autowired
	private ClueUrlDao clueUrlDao;

	private Configuration configuration;

	private static AtomicBoolean initial = new AtomicBoolean(false);

	/**
	 * @see com.x.apa.suspecturl.service.ClueUrlService#createClueUrls(com.x.apa.common.data.SuspectDomain)
	 */
	@Override
	public List<ClueUrl> createClueUrls(SuspectDomain suspectDomain) {

		if (!initial.get()) {
			loadTemplateConfiguration();
			initial.set(true);
		}

		List<ClueUrl> clueUrls = Lists.newArrayList();

		// 如果是phishtank识别出的URL，不走模板直接生成线索URL
		if (StringUtils.equals(suspectDomain.getSuspectMatch(), SUSPECT_DOMAIN_MATCH_PHISHTANK)) {
			// 每行构造一个线索URL对象
			ClueUrl clueUrl = new ClueUrl();
			BeanUtils.copyProperties(suspectDomain, clueUrl);
			clueUrl.setSuspectDomainId(suspectDomain.getId());
			clueUrl.setUrl(suspectDomain.getDomainName());
			clueUrl.setProgress(CLUE_URL_PROGRESS_INIT);

			// 保存到数据库
			clueUrlDao.saveClueUrl(clueUrl);

			clueUrls.add(clueUrl);
		} else {

			Map<String, Object> model = new HashMap<String, Object>();
			model.put("domainName", suspectDomain.getDomainName());

			// 使用模板构造线索URL
			String content = "";
			try {
				Template t = configuration.getTemplate(CLUE_URL_TEMPLATE_ID);
				content = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

				logger.debug("construct clue url list {}.", content);
			} catch (IOException | TemplateException e) {
				logger.warn("construct clue url for " + suspectDomain.getDomainName() + " failed.", e);
			}

			if (StringUtils.isNotBlank(content)) {

				String[] clueUrlTextArray = content.split(LINE_SEPARATOR);
				for (String clueUrlText : clueUrlTextArray) {

					// 忽略空行注释行
					if (StringUtils.isBlank(clueUrlText) || StringUtils.startsWith(clueUrlText, "#")) {
						continue;
					}

					// 每行构造一个线索URL对象
					ClueUrl clueUrl = new ClueUrl();
					BeanUtils.copyProperties(suspectDomain, clueUrl);
					clueUrl.setSuspectDomainId(suspectDomain.getId());
					clueUrl.setUrl(clueUrlText);
					clueUrl.setProgress(CLUE_URL_PROGRESS_INIT);

					// 保存到数据库
					clueUrlDao.saveClueUrl(clueUrl);

					clueUrls.add(clueUrl);
				}
			}
		}

		return clueUrls;
	}

	/**
	 * @see com.x.apa.suspecturl.service.ClueUrlService#updateClueUrlProgress(com.x.apa.suspecturl.data.ClueUrl)
	 */
	@Override
	public int updateClueUrlProgress(ClueUrl clueUrl) {
		return clueUrlDao.updateClueUrl(clueUrl);
	}

	/**
	 * @see com.x.apa.suspecturl.service.ClueUrlService#deleteClueUrlBeforeDate(java.util.Date)
	 */
	@Override
	public int deleteClueUrlBeforeDate(Date date) {
		return clueUrlDao.deleteClueUrlBeforeDate(date);
	}

	private void loadTemplateConfiguration() {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
		cfg.setTemplateLoader(templateLoader);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		cfg.setWrapUncheckedExceptions(true);

		this.configuration = cfg;
	}

}
