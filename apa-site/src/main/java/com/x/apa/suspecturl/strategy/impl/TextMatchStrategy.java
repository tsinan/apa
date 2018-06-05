package com.x.apa.suspecturl.strategy.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.memory.MemoryIndex;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.beust.jcommander.internal.Sets;
import com.x.apa.common.Constant;
import com.x.apa.common.metric.MetricCenter;
import com.x.apa.common.util.LuceneUtils;
import com.x.apa.suspecturl.dao.TagDao;
import com.x.apa.suspecturl.data.ClueUrl;
import com.x.apa.suspecturl.data.Tag;
import com.x.apa.suspecturl.data.TagRule;
import com.x.apa.suspecturl.strategy.UrlStrategyIntf;

import edu.uci.ics.crawler4j.parser.HtmlParseData;

/**
 * @author liumeng
 */
@Component
@Order(9)
public class TextMatchStrategy implements UrlStrategyIntf, Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private AtomicBoolean initial = new AtomicBoolean(false);

	@Autowired
	private TagDao tagDao;

	private List<TagRule> tagRules;

	private Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]");

	/**
	 * @see com.x.apa.suspecturl.strategy.UrlStrategyIntf#doMatch(com.x.apa.suspecturl.data.ClueUrl)
	 */
	@Override
	public Map<String, String> doMatch(ClueUrl clueUrl) {

		if (!initial.get()) {
			loadTagRules();
			initial.set(true);
		}

		Map<String, String> matchResult = null;

		String fullText = "";
		StringBuilder textBuilder = new StringBuilder();
		for (HtmlParseData htmlParseData : clueUrl.getHtmlDataList()) {
			String text = htmlParseData.getText();
			text = text.replaceAll("[^\\u4e00-\\u9fa5\\n\\r]", ""); // 去掉非汉字和空格
			text = text.replaceAll("[\\r\\n]", " ");// 换行替换为空格
			textBuilder.append(text).append("\n");
		}
		fullText = textBuilder.toString();

		// 首先检查汉字
		Matcher matcher = pattern.matcher(fullText);
		if (!matcher.find()) {
			MetricCenter.countSuspectUrlMatch("TextMatchStrategy_MustHaveChinese_Hit");

			matchResult = Maps.newHashMap();
			matchResult.put("matchedTagResult", SUSPECT_URL_MATCH_NO);
			matchResult.put("matchedTagRule", "no chinese");
			return matchResult;
		}

		// 其次进行关键词计算
		MemoryIndex index = new MemoryIndex();
		Analyzer analyzer = LuceneUtils.getAnalyzer();
		index.addField("content", fullText, analyzer);
		// LuceneUtils.printTokens(fullText);
		QueryParser parser = new QueryParser("content", analyzer);

		String matchedTagId = "";
		String matchedTagRule = "";
		float matchedScore = 0f;
		for (TagRule rule : tagRules) {

			if (StringUtils.isBlank(rule.getRule())) {
				continue;
			}

			try {
				// 使用各种规则进行检索，根据检索结果判断是否命中
				float score = index.search(parser.parse(rule.getRule()));
				if (score > 0.0f) {

					logger.debug("url {} match rule {} with score {}.", clueUrl.getUrl(), rule.getRule(), score);
					MetricCenter.countSuspectUrlMatch("TextMatchStrategy_TagRule_Hit");

					// 找到打分最高的规则
					if (matchedScore < score) {
						matchedScore = score;
						matchedTagId = rule.getTagId();
						matchedTagRule = rule.getRule();
					}
				} else {
					MetricCenter.countSuspectUrlMatch("TextMatchStrategy_TagRule_Miss");
				}
			} catch (ParseException e) {
				logger.warn("keyword match failed for url " + clueUrl.getUrl() + " rule " + rule.getRule() + ".", e);
			}
		}

		// 记录关键字识别结果
		if (StringUtils.isNotBlank(matchedTagId)) {

			logger.debug("keyword match tag {} with rule {} for url {}.", matchedTagId, matchedTagRule,
					clueUrl.getUrl());
			MetricCenter.countSuspectUrlMatch("TextMatchStrategy_Tag_Hit");

			matchResult = Maps.newHashMap();
			matchResult.put("matchedTagResult", SUSPECT_URL_MATCH_YES);
			matchResult.put("matchedTagId", matchedTagId);
			matchResult.put("matchedTagRule", matchedTagRule);
			matchResult.put("matchedScore", String.valueOf(matchedScore));
			return matchResult;
		}

		logger.debug("keyword match no tag for url {}.", clueUrl.getUrl());
		MetricCenter.countSuspectUrlMatch("TextMatchStrategy_Miss");
		return matchResult;
	}

	/**
	 * 定时更新标签规则
	 */
	@Scheduled(initialDelay = 3 * 1000, fixedDelay = 5 * 1000)
	public void syncTagRules() {
		loadTagRules();
	}

	private void loadTagRules() {
		List<Tag> tagList = tagDao.queryTags();
		Set<String> openedTagIdSet = Sets.newHashSet();
		for (Tag tag : tagList) {
			if (tag.getStatus() == STATUS_OPEN) {
				openedTagIdSet.add(tag.getId());
			}
		}

		List<TagRule> newTagRuleList = Lists.newArrayList();
		List<TagRule> tagRuleList = tagDao.queryTagRules();
		for (TagRule tagRule : tagRuleList) {
			if (openedTagIdSet.contains(tagRule.getTagId())) {
				newTagRuleList.add(tagRule);
			}
		}
		tagRules = newTagRuleList;

		logger.debug("load {} tag rules.", tagRules.size());
	}

}
