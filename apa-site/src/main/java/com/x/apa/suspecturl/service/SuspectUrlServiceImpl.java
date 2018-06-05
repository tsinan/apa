package com.x.apa.suspecturl.service;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Maps;
import com.x.apa.common.Constant;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.suspecturl.dao.SuspectUrlDao;
import com.x.apa.suspecturl.dao.TagDao;
import com.x.apa.suspecturl.data.ClueUrl;
import com.x.apa.suspecturl.data.SuspectUrl;
import com.x.apa.suspecturl.data.Tag;
import com.x.apa.suspecturl.strategy.UrlStrategyChain;

/**
 * @author liumeng
 */
@Component
public class SuspectUrlServiceImpl implements SuspectUrlService, Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UrlStrategyChain urlStrategyChain;

	@Autowired
	private SuspectUrlDao suspectUrlDao;

	@Autowired
	private TagDao tagDao;

	/**
	 * @see com.x.apa.suspecturl.service.SuspectUrlService#discernSuspectUrl(com.x.apa.suspecturl.data.ClueUrl)
	 */
	@Override
	public void discernSuspectUrl(ClueUrl clueUrl) {
		// 调用分析链，检查是否疑似Domain
		Map<String, String> matchResult = null;
		if ((matchResult = urlStrategyChain.match(clueUrl)) != null) {
			doSuspectUrlMatch(clueUrl, matchResult);
		} else {
			logger.debug("no url match for url {}.", clueUrl.getUrl());
		}
	}

	/**
	 * @see com.x.apa.suspecturl.service.SuspectUrlService#querySuspectUrls(com.x.apa.common.pageable.PageRequest,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public Page<SuspectUrl> querySuspectUrls(PageRequest page, String tagId, String urlLike) {

		Map<String, Tag> tagMap = Maps.newHashMap();

		Page<SuspectUrl> suspectUrlPage = suspectUrlDao.querySuspectUrls(page, tagId, urlLike);
		for (SuspectUrl suspectUrl : suspectUrlPage) {
			Tag tag = tagMap.get(suspectUrl.getTagId());
			if (tag == null) {
				tag = tagDao.queryTag(suspectUrl.getTagId());
				tagMap.put(tag.getId(), tag);
			}

			suspectUrl.setTagName(tag.getName());
		}
		return suspectUrlPage;
	}

	/**
	 * @see com.x.apa.suspecturl.service.SuspectUrlService#querySuspectUrl(java.lang.String)
	 */
	@Override
	public SuspectUrl querySuspectUrl(String id) {
		SuspectUrl suspectUrl = suspectUrlDao.querySuspectUrl(id);
		if (StringUtils.isNotBlank(suspectUrl.getId())) {
			Tag tag = tagDao.queryTag(suspectUrl.getTagId());
			suspectUrl.setTagName(tag.getName());
		}
		return suspectUrl;
	}

	/**
	 * @see com.x.apa.suspecturl.service.SuspectUrlService#changeSuspectUrlVerify(java.lang.String,
	 *      int)
	 */
	@Override
	public void changeSuspectUrlVerify(String id, int verify) {
		suspectUrlDao.updateSuspectUrlVerify(id, verify);
		if (verify == SUSPECT_URL_VERIFY_PHISHING) {
			suspectUrlDao.deleteSupsectUrl(id);
		}
	}

	private void doSuspectUrlMatch(ClueUrl clueUrl, Map<String, String> matchResult) {

		// 如果判定为非嫌疑，返回
		if (StringUtils.equals(matchResult.get("matchedTagResult"), SUSPECT_URL_MATCH_NO)) {
			logger.debug("url match for no suspect, rule is {}.", matchResult.get("matchedTagRule"));
			return;
		}

		SuspectUrl suspectUrl = new SuspectUrl();
		BeanUtils.copyProperties(clueUrl, suspectUrl);
		suspectUrl.setClueUrlId(clueUrl.getId());
		suspectUrl.setVerify(SUSPECT_URL_VERIFY_NONE);
		suspectUrl.setTagId(matchResult.get("matchedTagId"));
		suspectUrl.setTagRule(matchResult.get("matchedTagRule"));
		suspectUrl.setTagRuleScore(Float.parseFloat(matchResult.get("matchedScore")));
		suspectUrlDao.saveSuspectUrl(suspectUrl);
	}

	/**
	 * @see com.x.apa.suspecturl.service.SuspectUrlService#deleteSuspectUrlBeforeDate(java.util.Date)
	 */
	@Override
	public int deleteSuspectUrlBeforeDate(Date date) {
		return suspectUrlDao.deleteSuspectUrlBeforeDate(date);
	}

}
