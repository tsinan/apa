package com.x.apa.common.algorithm;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

/**
 * @author liumeng
 */
public class JaccardSimilarity {

	/**
	 * 改进的Jaccard相似度计算
	 * 
	 * 两个字符串的K元词集，交集数量必须大于等于min(s1词集,s2词集).size-2【近似目标是，只允许向长词中添加1个位置的混淆字符】
	 * 在10086这种五个字符的处理过程中，效果不好
	 * @param s1
	 * @param s2
	 * @param k
	 * @return
	 */
	public static boolean similarity(String s1, String s2, int k) {

		if (s1.equals(s2)) {
			return true;
		}

		Map<String, Integer> profile1 = getProfile(s1, k);
		Map<String, Integer> profile2 = getProfile(s2, k);

		Set<String> union = new HashSet<String>();
		union.addAll(profile1.keySet());
		union.addAll(profile2.keySet());

		boolean isProfile1Bigger = profile1.keySet().size() >= profile2.keySet().size();
		Map<String, Integer> smallSet = isProfile1Bigger ? profile2 : profile1;

		// 交集的数量需要大于等于短词的词数-2；在敏感词内部一个位置添加混淆字符之后，最多会影响三元组的两个词
		int inter = profile1.keySet().size() + profile2.keySet().size() - union.size();
		int smallerSize = smallSet.keySet().size();
		if (smallerSize < 3) {
			// 小于三个词时，交集数量必须等于词数量
			return inter == smallerSize;
		} /*
			 * else if (smallerSize == 3) { // 等于三个词时，交集数量必须大于等于两个词 if (inter ==
			 * 0) { return false; } else if (inter == 1) { //
			 * 交集为1，要看是不是首尾词，如果是首尾词，也返回true Set<String> interSet =
			 * Sets.newLinkedHashSet(profile1.keySet());
			 * interSet.retainAll(profile2.keySet()); String interString =
			 * interSet.iterator().next();
			 * 
			 * List<String> smallList = Lists.newArrayList(smallSet.keySet());
			 * return smallList.indexOf(interString) == 0 ||
			 * smallList.lastIndexOf(interString) == smallList.size() - 1; }
			 * else { // 交集为2，返回true return true; } }
			 */else {
			// 多于三个词时，交集数量必须大于等于词数量-2
			return inter >= smallerSize - 2;
		}
	}

	private static Map<String, Integer> getProfile(String string, int k) {
		HashMap<String, Integer> shingles = Maps.newLinkedHashMap();

		for (int i = 0; i < (string.length() - k + 1); i++) {
			String shingle = string.substring(i, i + k);
			Integer old = shingles.get(shingle);
			if (old != null) {
				shingles.put(shingle, old + 1);
			} else {
				shingles.put(shingle, 1);
			}
		}

		return Collections.unmodifiableMap(shingles);
	}
}
