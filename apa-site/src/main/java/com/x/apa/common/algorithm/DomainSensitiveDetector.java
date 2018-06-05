package com.x.apa.common.algorithm;

import org.apache.commons.lang.CharUtils;

/**
 * @author liumeng
 */
public class DomainSensitiveDetector {

	public static int length(String domainName, String sensitiveWord, int coefficientLength, int tolerate) {

		// 将被检测域名字符进行替换
		StringBuilder domain = convert(domainName);
		StringBuilder word = convert(sensitiveWord);

		// 目标词首字符
		String firstChar = CharUtils.toString(word.charAt(0));

		// 目标词的首字符必须存在，否则判断为0
		if (domain.indexOf(firstChar) < 0) {
			return 0;
		}

		// 目标词首字符之前的域名部分忽略，由此必须以目标词首字符为起始计算length
		domain.delete(0, domain.indexOf(firstChar));

		int s1_length = domain.length();
		int s2_length = word.length();
		char[] x = domain.toString().toCharArray();
		char[] y = word.toString().toCharArray();

		int[][] c = new int[s1_length + 1][s2_length + 1];
		for (int i = 1; i <= s1_length; i++) {
			for (int j = 1; j <= s2_length; j++) {
				if (x[i - 1] == y[j - 1]) {
					c[i][j] = c[i - 1][j - 1] + 1;

				} else {
					c[i][j] = Math.max(c[i][j - 1], c[i - 1][j]);
				}
			}
		}

		// 如果最长匹配长度小于min(域名,目标词)长度的coefficient，不匹配
		if (c[s1_length][s2_length] < coefficientLength) {
			return 0;
		}

		// 以首字符为锚点，检查字符相似度
		boolean similarity = false;
		int tolerateLength = word.length() + tolerate; // 检查宽度为敏感词长度+1
		String slice = ""; // 每次进行相似性计算的分片
		while (true) {
			// 如果被检测字符串不包含敏感词首字符，则退出
			int firstCharIndex = domain.indexOf(firstChar);
			if (firstCharIndex < 0) {
				break;
			}

			// 从第一个word首字符开始，截取子串
			domain = domain.delete(0, firstCharIndex);
			if (domain.length() >= tolerateLength) {
				// 从首字符开始截取容忍长度的子串，进行敏感词长度计算
				slice = domain.substring(0, tolerateLength);
			} else if (domain.length() >= coefficientLength && domain.length() < tolerateLength) {
				slice = domain.toString();
			} else {
				// 如果子串长度小于coefficientLength长度
				break;
			}

			// Damerau距离计算相似度，如果目标词和敏感词通过tolerate次编辑可以相等，则认为相似
			similarity = DamerauSimilarity.distance(slice, word.toString()) <= tolerate;
			if (similarity) {
				break;
			} else {
				// 继续向后移位计算，把当前首字符去掉
				domain = domain.deleteCharAt(0);
				continue;
			}
		}
		return similarity ? c[s1_length][s2_length] : 0;

	}

	private static StringBuilder convert(String domainName) {
		StringBuilder domain = new StringBuilder(domainName);

		for (int i = 0; i < domain.length(); i++) {
			switch (domain.charAt(i)) {
			case 'l':
			case 'i':
				domain.setCharAt(i, '1');
				break;
			case 'z':
				domain.setCharAt(i, '2');
				break;
			case 's':
				domain.setCharAt(i, '5');
				break;
			case 'b':
				domain.setCharAt(i, '6');
				break;
			case 't':
				domain.setCharAt(i, '7');
				break;
			// case 'q':
			// case 'g':
			// domain.setCharAt(i, '9');
			// break;// 误判太多
			case 'o':
				domain.setCharAt(i, '0');
				break;
			}
		}
		return domain;
	}

}
