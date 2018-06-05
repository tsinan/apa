package com.x.apa.text;

import com.x.apa.common.algorithm.DamerauSimilarity;
import com.x.apa.common.algorithm.DomainSensitiveDetector;

/**
 * @author liumeng
 */
public class SimilarityTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String domainName = "100z86";
		String sensitiveWord = "10086";
		int l = DomainSensitiveDetector.length(domainName, sensitiveWord, 5, 1);
		System.out.println(l);

		double d = DamerauSimilarity.distance(domainName, sensitiveWord);
		System.out.println(d);
	}

}
