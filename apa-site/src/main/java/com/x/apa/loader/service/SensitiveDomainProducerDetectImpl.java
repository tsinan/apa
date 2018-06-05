package com.x.apa.loader.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.x.apa.common.Constant;
import com.x.apa.common.algorithm.DomainSensitiveDetector;
import com.x.apa.common.data.RawDomain;
import com.x.apa.common.util.HotPropertiesAccessor;
import com.x.apa.common.util.StringUtils;
import com.x.apa.loader.data.DomainSensitiveWord;
import com.x.apa.loader.data.SensitiveDomain;

/**
 * @author liumeng
 */
@Service
public class SensitiveDomainProducerDetectImpl implements SensitiveDomainProducer, Constant {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private List<DomainSensitiveWord> domainSensitiveWordList;

	@Autowired
	private RawDomainService rawDomainService;

	@Autowired
	private DomainSensitiveWordService domainSensitiveWordService;

	@Autowired
	private SensitiveDomainService sensitiveDomainService;

	/**
	 * @see com.x.apa.loader.service.SensitiveDomainProducer#produce(java.util.Date)
	 */
	public int produce(Date date) {

		int totalCount = 0;

		boolean produce = BooleanUtils
				.toBoolean(HotPropertiesAccessor.getProperty("ld.sensitive.produce.daily.detect"));
		if (!produce) {
			logger.info("produce sensitive domain daily by detect escape...");
			return totalCount;
		}

		// 读取域名敏感词
		loadDomainSensitiveWords();

		// 取相似系数、容忍距离
		double coefficient = Double.parseDouble(HotPropertiesAccessor.getProperty("ld.sensitive.coefficient"));
		int tolerate = Integer.parseInt(HotPropertiesAccessor.getProperty("ld.sensitive.tolerate"));

		// 定义循环和统计变量
		int pageNumber = 0;
		int pageSize = 5000;
		int count = 0;
		int matchCount = 0;
		boolean going = true;
		while (going) {
			// 分页读取原始域名
			List<RawDomain> rawDomainList = rawDomainService.queryRawDomains(pageNumber * pageSize, pageSize, date);
			for (RawDomain rawDomain : rawDomainList) {

				// 如果原始域名已经添加到敏感域名，则不需要重复监测
				if (rawDomain.getIsSensitive() == DOMAIN_SENSITIVE) {
					continue;
				}

				// 以xn--开头的都是汉字域名，不匹配，跳过
				if (rawDomain.getDomainName().startsWith("xn--")) {
					continue;
				}

				SensitiveDomain sensitiveDomain = new SensitiveDomain();
				sensitiveDomain.setCategory(SENSITIVE_DOMAIN_CATEGORY_LEARNING);
				sensitiveDomain.setDomainName(rawDomain.getDomainName());
				sensitiveDomain.setRegistrationDate(rawDomain.getRegistrationDate());

				// 使用-切割域名，每两个子串作为一个词送入检测
				String detectDomainName = rawDomain.getDomainName().substring(0,
						rawDomain.getDomainName().indexOf("."));
				String[] subNames = detectDomainName.split("-");
				if (subNames.length <= 2) {
					detectDomainName(detectDomainName, sensitiveDomain, coefficient, tolerate);
				} else {
					int i = 0;
					while (i < subNames.length - 1) {
						detectDomainName = subNames[i] + subNames[i + 1];
						detectDomainName(detectDomainName, sensitiveDomain, coefficient, tolerate);
						i++;
					}
					detectDomainName = subNames[i];
					detectDomainName(detectDomainName, sensitiveDomain, coefficient, tolerate);
				}

				// 如果匹配，则记录敏感域名
				if (sensitiveDomain.getLcsLength() > 0) {

					matchCount++;
					sensitiveDomainService.createSensitiveDomain(sensitiveDomain);
				}
			}

			// 统计信息输出
			int actualSize = rawDomainList.size();
			totalCount += actualSize;
			count += actualSize;
			pageNumber++;

			if (actualSize < pageSize) {
				going = false;
				logger.info("produce {}/{}-{} sensitive domain will finish...", matchCount, count, totalCount);
			} else if (count >= pageSize * 10) {
				logger.info("produce {}/{}-{} sensitive domain already...", matchCount, count, totalCount);
				count = 0;
				matchCount = 0;
			}

		}

		return totalCount;
	}

	private void detectDomainName(String detectDomainName, SensitiveDomain sensitiveDomain, double coefficient,
			int tolerate) {

		for (DomainSensitiveWord sensitiveWord : domainSensitiveWordList) {

			String[] words = sensitiveWord.getWord().split("\n");
			for (String word : words) {

				// targetLength;
				int coefficientLength = (int) Math.ceil(word.length() * coefficient);

				// 域名太长，不匹配，跳过
				if (detectDomainName.length() > word.length() * 3) {
					continue;
				}

				// 域名中除目标词字符之外的字符数量过多，不匹配，跳过
				if (detectDomainName.length() > word.length()) {
					int charDifferSize = StringUtils.computeCharDifferSize(detectDomainName, word);
					if (charDifferSize > 6) {
						continue;
					}
				}

				// 计算最长匹配长度
				int length = DomainSensitiveDetector.length(detectDomainName, word, coefficientLength, tolerate);

				// 如果最长匹配长度小于min(域名,目标词)长度的coefficient，不匹配，跳过
				if (length < coefficientLength) {
					continue;
				}

				// 匹配，是否是最大匹配长度
				if (sensitiveDomain.getLcsLength() < length) {
					sensitiveDomain.setSensitiveWordName(sensitiveWord.getName());
					sensitiveDomain.setSensitiveWord(word);
					sensitiveDomain.setLcsLength(length);
				}
			}
		}

		return;
	}

	private void loadDomainSensitiveWords() {
		domainSensitiveWordList = domainSensitiveWordService.queryDomainSensitiveWords();
	}
}
