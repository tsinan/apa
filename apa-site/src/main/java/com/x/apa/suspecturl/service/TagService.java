package com.x.apa.suspecturl.service;

import java.util.List;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.suspecturl.data.Tag;

/**
 * @author liumeng
 */
public interface TagService {

	Page<Tag> queryTags(PageRequest page);

	List<Tag> queryTags();

	void createTag(Tag tag);

	int updateTag(Tag tag);

	int changeTagStatus(String id, int status);

	int deleteTag(String id);
}
