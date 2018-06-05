package com.x.apa.suspecturl.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Maps;
import com.beust.jcommander.internal.Sets;
import com.google.common.collect.Lists;
import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.suspecturl.dao.TagDao;
import com.x.apa.suspecturl.data.Tag;
import com.x.apa.suspecturl.data.TagRule;

/**
 * @author liumeng
 */
@Component
public class TagServiceImpl implements TagService {

	@Autowired
	private TagDao tagDao;

	/**
	 * @see com.x.apa.suspecturl.service.TagService#queryTags()
	 */
	@Override
	public List<Tag> queryTags() {
		return tagDao.queryTags();
	}

	/**
	 * @see com.x.apa.suspecturl.service.TagService#queryTags(com.x.apa.common.pageable.PageRequest)
	 */
	@Override
	public Page<Tag> queryTags(PageRequest page) {

		Page<Tag> tagPage = tagDao.queryTags(page);
		for (Tag tag : tagPage) {

			StringBuilder builder = new StringBuilder();
			List<TagRule> tagRuleList = tagDao.queryTagRules(tag.getId());
			for (TagRule tagRule : tagRuleList) {
				builder.append(tagRule.getRule());
				builder.append("\n");
			}

			tag.setRules(builder.toString());
		}
		return tagPage;
	}

	/**
	 * @see com.x.apa.suspecturl.service.TagService#createTag(com.x.apa.suspecturl.data.Tag)
	 */
	@Override
	public void createTag(Tag tag) {

		tag = tagDao.saveTag(tag);

		String lines[] = StringUtils.split(tag.getRules(), "\n");
		if (ArrayUtils.isNotEmpty(lines)) {
			Set<String> ruleSet = Sets.newHashSet();
			List<TagRule> tagRules = Lists.newArrayList();
			for (String line : lines) {
				if (ruleSet.contains(line)) {
					continue;
				}
				ruleSet.add(line);

				TagRule rule = new TagRule();
				rule.setTagId(tag.getId());
				rule.setRule(line);
				rule.setDescription("");
				rule.setCreatorId(tag.getCreatorId());

				tagRules.add(rule);
			}
			tagDao.saveTagRules(tagRules);
		}
	}

	/**
	 * @see com.x.apa.suspecturl.service.TagService#updateTag(com.x.apa.suspecturl.data.Tag)
	 */
	@Override
	public int updateTag(Tag tag) {
		Tag exsitTag = tagDao.queryTag(tag.getId());
		if (StringUtils.isBlank(exsitTag.getId())) {
			return 0;
		}

		tagDao.updateTag(tag);

		// 查询已有规则
		List<TagRule> exsitTagRuleList = tagDao.queryTagRules(tag.getId());
		Set<String> exsitRuleSet = Sets.newHashSet();
		Map<String, String> ruleTagIdMap = Maps.newHashMap();
		for (TagRule tagRule : exsitTagRuleList) {
			exsitRuleSet.add(tagRule.getRule());
			ruleTagIdMap.put(tagRule.getRule(), tagRule.getId());
		}
		// 计算修改后规则
		String lines[] = StringUtils.split(tag.getRules(), "\n");
		Set<String> ruleSet = Sets.newHashSet();
		if (ArrayUtils.isNotEmpty(lines)) {
			for (String line : lines) {
				ruleSet.add(line);
			}
		}

		// 计算新增删除
		Set<String> toAddRuleSet = Sets.newHashSet();
		toAddRuleSet.addAll(ruleSet);
		Set<String> toDeleteRuleSet = Sets.newHashSet();
		toDeleteRuleSet.addAll(exsitRuleSet);
		toAddRuleSet.removeAll(exsitRuleSet);
		toDeleteRuleSet.removeAll(ruleSet);

		// 处理新增删除
		if (CollectionUtils.isNotEmpty(toAddRuleSet)) {
			List<TagRule> toAddTagRules = Lists.newArrayList();
			for (String rule : toAddRuleSet) {
				TagRule tagRule = new TagRule();
				tagRule.setTagId(tag.getId());
				tagRule.setRule(rule);
				tagRule.setDescription("");
				tagRule.setCreatorId(tag.getCreatorId());

				toAddTagRules.add(tagRule);
			}
			tagDao.saveTagRules(toAddTagRules);
		}
		if (CollectionUtils.isNotEmpty(toDeleteRuleSet)) {
			List<String> toDeleteTagRuleIds = Lists.newArrayList();
			for (String rule : toDeleteRuleSet) {
				toDeleteTagRuleIds.add(ruleTagIdMap.get(rule));
			}
			tagDao.deleteTagRules(toDeleteTagRuleIds);
		}

		return 0;
	}

	/**
	 * @see com.x.apa.suspecturl.service.TagService#changeTagStatus(java.lang.String,int)
	 */
	@Override
	public int changeTagStatus(String id, int status) {
		return tagDao.changeTagStatus(id, status);
	}

	/**
	 * @see com.x.apa.suspecturl.service.TagService#deleteTag(java.lang.String)
	 */
	@Override
	public int deleteTag(String id) {
		tagDao.deleteTagRules(id);
		return tagDao.deleteTag(id);
	}

}
