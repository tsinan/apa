package com.x.apa.common.algorithm;

import java.util.List;

import org.apache.commons.lang.CharUtils;

/**
 * @author liumeng
 */
public class LCSSimilarity {

	/**
	 * 通过不断计算域名中子串（必须以敏感词首字符开头）的LCS距离，找出是否满足LCS距离条件的子串存在
	 * 
	 * 目标是排除满足LCS距离的字符过于离散，例如1eeee0eeee0eee8eeee6。因为不断处理LCS计算过于复杂，暂时放弃
	 * 
	 * @param detectDomainName
	 * @param word
	 * @param length
	 * @param coefficientLength
	 * @return
	 */
	@Deprecated
	public static boolean discrete(String detectDomainName, String word, int length, int coefficientLength,
			int tolerate) {
		// 检查域名的每个N元子串内是否包含敏感词，避免敏感词在域名中出现的过于分散
		boolean found = false;
		boolean goon = true;
		String sensitiveFirstChar = CharUtils.toString(word.charAt(0));
		int tolerateLength = word.length() + 2;
		StringBuilder detectDomain = new StringBuilder(detectDomainName);
		while (goon) {
			// 如果被检测字符串不包含敏感词首字符，则退出
			int firstCharIndex = detectDomain.indexOf(sensitiveFirstChar);
			if (firstCharIndex < 0) {
				break;
			}

			// 从第一个word首字符开始，截取子串
			detectDomain = detectDomain.delete(0, firstCharIndex);
			String slice = "";
			if (detectDomain.length() >= tolerateLength) {
				// 从首字符开始截取容忍长度的子串，进行敏感词长度计算
				slice = detectDomain.substring(0, tolerateLength);
			} else if (detectDomain.length() >= coefficientLength && detectDomain.length() < tolerateLength) {
				slice = detectDomain.toString();
			} else {
				// 如果子串长度小于targetLength长度
				break;
			}

			// 从首字符开始截取容忍长度的子串，进行敏感词长度计算
			int sliceLength = DomainSensitiveDetector.length(slice, word, coefficientLength, tolerate);
			if (sliceLength < coefficientLength) {
				// 如果计算结果小于targetLength，继续向后移位计算，把当前首字符去掉
				detectDomain = detectDomain.deleteCharAt(0);
				continue;
			} else if (sliceLength < length) {
				// 没有找到全部匹配长度，继续找
				detectDomain = detectDomain.deleteCharAt(0);
				found = true;
				continue;
			} else {
				// 找到了，退出
				found = true;
				goon = false;
				break;
			}
		}
		return found;
	}

	/**
	 * 通过回溯LCS矩阵，试图找出满足LCS距离要求的子串，但是回溯路径太多，暂时放弃
	 * 
	 * @param x
	 * @param y
	 * @param c
	 * @param resultList
	 */
	@Deprecated
	public static void extractString(char[] x, char[] y, int[][] c, List<String> resultList) {
		int s1_length = x.length;
		int s2_length = y.length;

		for (int j = 0; j <= s2_length; j++) {
			for (int i = 0; i <= s1_length; i++) {
				System.out.print(c[i][j] + " ");
			}
			System.out.println("\n");
		}

		StringBuilder builder = new StringBuilder();
		StringBuilder builder2 = new StringBuilder();
		int i = s1_length;
		int j = s2_length;
		while (true) {
			if (i == 0 && j == 0) {
				break;
			}

			// 字符相等时
			if (x[i - 1] == y[j - 1] || isReplaceChar(x[i - 1], y[j - 1])) {
				if (i == 1 && j != 1) {
					builder.insert(0, '_');
					builder2.insert(0, x[i - 1]);

					j--;
					continue;
				} else if (i != 1 && j == 1) {
					builder.insert(0, x[i - 1]);
					builder2.insert(0, '_');
					i--;
					continue;
				} else {
					builder.insert(0, x[i - 1]);
					builder2.insert(0, x[i - 1]);
					i--;
					j--;
					continue;
				}
			}

			// 不相等时
			if (i == 1 && j != 1) {
				builder.insert(0, '_');
				builder2.insert(0, x[i - 1]);

				j--;
				continue;
			} else if (i != 1 && j == 1) {
				builder.insert(0, x[i - 1]);
				builder2.insert(0, '_');
				i--;
				continue;
			} else {
				int max = Math.max(Math.max(c[i - 1][j - 1], c[i][j - 1]), c[i - 1][j]);
				if (max == c[i - 1][j - 1]) {
					builder.insert(0, x[i - 1]);
					builder2.insert(0, x[i - 1]);
					i--;
					j--;
					continue;
				} else if (max == c[i - 1][j]) {
					builder.insert(0, x[i - 1]);
					builder2.insert(0, '_');
					i--;
					continue;
				} else if (max == c[i][j - 1]) {
					builder.insert(0, '_');
					builder2.insert(0, x[i - 1]);
					j--;
					continue;
				}
			}

		}
		resultList.add(builder.toString());
		resultList.add(builder2.toString());
	}

	private static boolean isReplaceChar(char c1, char c2) {

		switch (c1) {
		case '1':
			return c2 == 'l' || c2 == 'i';
		case '2':
			return c2 == 'z';
		case '5':
			return c2 == 's';
		case '7':
			return c2 == 't';
		case '9':
			return c2 == 'q' || c2 == 'g';
		case '0':
			return c2 == 'o';
		case 'l':
			return c2 == '1' || c2 == 'i';
		case 'i':
			return c2 == '1' || c2 == 'l';
		case 'z':
			return c2 == '2';
		case 's':
			return c2 == '5';
		case 't':
			return c2 == '7';
		case 'q':
			return c2 == '9' || c2 == 'g';
		case 'g':
			return c2 == '9' || c2 == 'q';
		case 'o':
			return c2 == '0';
		default:
			return false;
		}
	}

}
