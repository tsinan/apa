package com.x.apa.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.google.common.collect.Sets;
import com.x.apa.common.pageable.Sort;

/**
 * @author liumeng
 */
public class LuceneUtils {

	private static Logger logger = LoggerFactory.getLogger(LuceneUtils.class);

	private static final Set<String> PHISHING_WORD_SET = Sets.newLinkedHashSet();
	private static final String FILE_LINE_COMMENT = "//";

	static {
		try {
			BufferedReader br = null;
			try {
				br = (BufferedReader) IOUtils.getDecodingReader(LuceneUtils.class, "/lucene/ext_phishing.dic",
						StandardCharsets.UTF_8);
				String word = null;
				while ((word = br.readLine()) != null) {
					if (word.startsWith(FILE_LINE_COMMENT) == false) {
						PHISHING_WORD_SET.add(word.trim());
					}
				}
			} finally {
				IOUtils.close(br);
			}
		} catch (IOException e) {
			logger.warn("load phishing dic failed.", e);
		}
	}

	public static Analyzer getAnalyzer() {

		String analyzerType = HotPropertiesAccessor.getProperty("common.lucene.analyzer");
		Analyzer analyzer = null;
		if (StringUtils.equalsIgnoreCase(analyzerType, "ik-smart")) {
			analyzer = new IKAnalyzer(true);
		} else {
			analyzer = new IKAnalyzer(false);
		}
		return analyzer;
	}

	public static String getKeyword(String text) {

		String keyword = "";

		Map<String, Integer> wordCountMap = Maps.newHashMap();
		IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(text), true);
		try {
			Lexeme lexeme = ikSegmenter.next();
			while (lexeme != null) {
				String word = lexeme.getLexemeText();
				Integer count = wordCountMap.get(word);
				if (count == null) {
					count = new Integer(1);
				} else {
					count = count + 1;
				}
				wordCountMap.put(word, count);

				lexeme = ikSegmenter.next();
			}
		} catch (IOException e) {
			logger.warn("get keyword for " + text + " failed", e);
		}

		if (wordCountMap.size() > 0) {
			// 按词频排序
			Map<String, Integer> frequencys = MapUtils.sortByValue(wordCountMap, Sort.Direction.DESC);

			// 优先使用反钓鱼词库的词
			for (Map.Entry<String, Integer> entry : frequencys.entrySet()) {
				if (PHISHING_WORD_SET.contains(entry.getKey())) {
					keyword = entry.getKey();
					break;
				}
			}
			if (StringUtils.isNotBlank(keyword)) {
				return keyword;
			}

			// 没有就从词频选最高出现的词
			for (Map.Entry<String, Integer> entry : frequencys.entrySet()) {
				if (entry.getKey().length() > 1) {
					keyword = entry.getKey();
					break;
				} else if (keyword.length() == 0) {
					keyword = entry.getKey();
				}
			}
		}
		return keyword;
	}

	public static String getTokens(String text, boolean withFrequency) {
		Map<String, Integer> wordCountMap = Maps.newHashMap();
		List<String> wordList = Lists.newArrayList();

		IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(text), true);
		try {
			Lexeme lexeme = ikSegmenter.next();
			while (lexeme != null) {
				String word = lexeme.getLexemeText();
				wordList.add("[" + word + "]");

				Integer count = wordCountMap.get(word);
				if (count == null) {
					count = new Integer(1);
				} else {
					count = count + 1;
				}
				wordCountMap.put(word, count);

				lexeme = ikSegmenter.next();
			}
		} catch (IOException e) {
			logger.warn("get keyword for " + text + " failed", e);
		}

		if (!withFrequency) {
			String words = String.join(",", wordList);
			logger.debug("[词] -> {}", words);
			return words;
		} else {

			Map<String, Integer> frequencys = MapUtils.sortByValue(wordCountMap, Sort.Direction.DESC);
			List<String> wordWithF = Lists.newArrayList();
			for (Map.Entry<String, Integer> frequency : frequencys.entrySet()) {
				wordWithF.add("[" + frequency.getKey() + "|" + frequency.getValue() + "]");
			}

			String wordFreq = String.join(",", wordWithF);
			logger.info("[词频] -> {}", wordFreq);
			return wordFreq;
		}

	}

}
