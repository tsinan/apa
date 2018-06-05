package com.x.apa.loader.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.x.apa.common.pageable.Page;
import com.x.apa.common.pageable.PageRequest;
import com.x.apa.loader.dao.DomainSensitiveWordDao;
import com.x.apa.loader.data.DomainSensitiveWord;

/**
 * @author liumeng
 */
@Component
public class DomainSensitiveWordServiceImpl implements DomainSensitiveWordService {

	@Autowired
	private DomainSensitiveWordDao domainSensitiveWordDao;

	/**
	 * @see com.x.apa.loader.service.DomainSensitiveWordService#queryDomainSensitiveWords()
	 */
	@Override
	public List<DomainSensitiveWord> queryDomainSensitiveWords() {
		return domainSensitiveWordDao.queryDomainSensitiveWords();
	}

	/**
	 * @see com.x.apa.loader.service.DomainSensitiveWordService#queryDomainSensitiveWords(com.x.apa.common.pageable.PageRequest)
	 */
	@Override
	public Page<DomainSensitiveWord> queryDomainSensitiveWords(PageRequest page) {
		return domainSensitiveWordDao.queryDomainSensitiveWords(page);
	}

	/**
	 * @see com.x.apa.loader.service.DomainSensitiveWordService#createDomainSensitiveWord(com.x.apa.loader.data.DomainSensitiveWord)
	 */
	@Override
	public void createDomainSensitiveWord(DomainSensitiveWord domainSensitiveWord) {
		domainSensitiveWordDao.saveDomainSensitiveWord(domainSensitiveWord);
	}

	/**
	 * @see com.x.apa.loader.service.DomainSensitiveWordService#updateDomainSensitiveWord(com.x.apa.loader.data.DomainSensitiveWord)
	 */
	@Override
	public int updateDomainSensitiveWord(DomainSensitiveWord domainSensitiveWord) {
		return domainSensitiveWordDao.updateDomainSensitiveWord(domainSensitiveWord);
	}

	/**
	 * @see com.x.apa.loader.service.DomainSensitiveWordService#changeDomainSensitiveWordStatus(java.lang.String,
	 *      int)
	 */
	@Override
	public int changeDomainSensitiveWordStatus(String id, int status) {
		return domainSensitiveWordDao.changeDomainSensitiveWordStatus(id, status);
	}

	/**
	 * @see com.x.apa.loader.service.DomainSensitiveWordService#deleteDomainSensitiveWord(java.lang.String)
	 */
	@Override
	public int deleteDomainSensitiveWord(String id) {
		return domainSensitiveWordDao.deleteDomainSensitiveWord(id);
	}

}
