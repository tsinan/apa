package com.x.apa.common.metric;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;
import com.x.apa.common.util.DateUtils;

/**
 * @author liumeng
 */
@Component
public class MetricCenter {

	@Autowired
	private static StringRedisTemplate redisTemplate;

	@Resource(name = "stringRedisTemplate")
	public void setRedisTemplate(StringRedisTemplate stringRedisTemplate) {
		MetricCenter.redisTemplate = stringRedisTemplate;
	}

	public static int countUrlPageVisit(String url) {
		if (redisTemplate == null) {
			return 0;
		}

		String key = "SU_URL_PAGE_VISIT_COUNT_" + DateUtils.toString_YYYY_MM_DD(new Date());
		double count = redisTemplate.opsForZSet().incrementScore(key, url, 1d);
		redisTemplate.expire(key, 3, TimeUnit.DAYS);
		return Double.valueOf(count).intValue();
	}

	public static int countUrlNohtmlVisit(String url) {
		if (redisTemplate == null) {
			return 0;
		}

		String key = "SU_URL_NOHTML_VISIT_COUNT_" + DateUtils.toString_YYYY_MM_DD(new Date());
		double count = redisTemplate.opsForZSet().incrementScore(key, url, 1d);
		redisTemplate.expire(key, 3, TimeUnit.DAYS);
		return Double.valueOf(count).intValue();
	}

	public static int countUrlNohtmlVisitSize(String url, int size) {
		if (redisTemplate == null) {
			return 0;
		}

		String key = "SU_URL_NOHTML_VISIT_SIZE_" + DateUtils.toString_YYYY_MM_DD(new Date());
		double count = redisTemplate.opsForZSet().incrementScore(key, url, size);
		redisTemplate.expire(key, 3, TimeUnit.DAYS);
		return Double.valueOf(count).intValue();
	}

	public static List<TypedTuple<String>> topUrlVisit() {

		List<TypedTuple<String>> topList = Lists.newLinkedList();

		String key = "SU_URL_PAGE_VISIT_COUNT_" + DateUtils.toString_YYYY_MM_DD(new Date());
		Set<TypedTuple<String>> tupleSet = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 5);
		topList.addAll(tupleSet);

		key = "SU_URL_NOHTML_VISIT_COUNT_" + DateUtils.toString_YYYY_MM_DD(new Date());
		tupleSet = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 5);
		topList.addAll(tupleSet);

		Collections.sort(topList, new Comparator<TypedTuple<String>>() {
			@Override
			public int compare(TypedTuple<String> o1, TypedTuple<String> o2) {
				return -(o1.getScore().compareTo(o2.getScore()));
			}
		});
		return topList;
	}

	public static Set<TypedTuple<String>> topUrlVisitSize() {

		String key = "SU_URL_NOHTML_VISIT_SIZE_" + DateUtils.toString_YYYY_MM_DD(new Date());
		return redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 5);
	}

	public static int countTrustDomainVisit(String url) {
		if (redisTemplate == null) {
			return 0;
		}

		String key = "SU_TRUST_DOMAIN_VISIT_COUNT_" + DateUtils.toString_YYYY_MM_DD(new Date());
		double count = redisTemplate.opsForZSet().incrementScore(key, url, 1l);
		redisTemplate.expire(key, 3, TimeUnit.DAYS);
		return Double.valueOf(count).intValue();
	}

	public static int countSuspectUrlMatch(String strategy) {
		if (redisTemplate == null) {
			return 0;
		}
		String key = "SU_SUSPECT_URL_MATCH_" + DateUtils.toString_YYYY_MM_DD(new Date());
		double count = redisTemplate.opsForHash().increment(key, strategy, 1d);
		redisTemplate.expire(key, 3, TimeUnit.DAYS);
		return Double.valueOf(count).intValue();
	}

	public static Map<Object, Object> getSuspectUrlMatchMetric() {
		Map<Object, Object> metric = Maps.newHashMap();
		if (redisTemplate == null) {
			return metric;
		}

		String key = "SU_SUSPECT_URL_MATCH_" + DateUtils.toString_YYYY_MM_DD(new Date());
		metric = redisTemplate.opsForHash().entries(key);
		return metric;
	}

}
