package com.x.apa.loader.dao;

import java.util.List;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.loader.data.DomainSensitiveWord;

/**
 * @author liumeng
 */
public interface DomainSensitiveWordDao {

	List<DomainSensitiveWord> queryDomainSensitiveWords();
	

	Page<DomainSensitiveWord> queryDomainSensitiveWords(PageRequest page);
	
	DomainSensitiveWord queryDomainSensitiveWord(String id);
	
	DomainSensitiveWord saveDomainSensitiveWord(DomainSensitiveWord domainSensitiveWord);
	
	int updateDomainSensitiveWord(DomainSensitiveWord domainSensitiveWord);
	
	int changeDomainSensitiveWordStatus(String id, int status);
	
	int deleteDomainSensitiveWord(String id);
	
}
