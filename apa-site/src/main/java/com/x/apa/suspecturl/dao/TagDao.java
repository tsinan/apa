package com.x.apa.suspecturl.dao;

import java.util.List;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.suspecturl.data.Tag;
import com.x.apa.suspecturl.data.TagRule;

/**
 * @author liumeng
 */
public interface TagDao {

	List<Tag> queryTags();

	Page<Tag> queryTags(PageRequest page);

	Tag queryTag(String id);

	Tag saveTag(Tag tag);

	int updateTag(Tag tag);

	int changeTagStatus(String id, int status);

	int deleteTag(String id);

	List<TagRule> queryTagRules();

	List<TagRule> queryTagRules(String tagId);

	int[] saveTagRules(List<TagRule> tagRules);

	int deleteTagRules(String tagId);

	int deleteTagRules(List<String> tagRuleIds);

}
